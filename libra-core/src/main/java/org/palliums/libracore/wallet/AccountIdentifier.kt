package org.palliums.libracore.wallet

import org.palliums.libracore.crypto.Bech32Segwit
import org.palliums.libracore.transaction.AccountAddress
import java.util.*


/**
 * AccountIdentifier implements Libra Account Identifier encoding and decoding.
 * @see <a href="https://github.com/libra/lip/blob/master/lips/lip-5.md">LIP-5 Address formatting</a>
 */
class AccountIdentifier(
    private val prefix: NetworkPrefix,
    private val accountAddress: AccountAddress,
    private val subAddress: SubAddress = SubAddress(ByteArray(SubAddress.SUB_ADDRESS_LENGTH))
) {
    companion object {
        const val VERSION_1: Byte = 1

        class DecodeResult(
            prefix: NetworkPrefix,
            version: Byte,
            address: ByteArray,
            subAddress: ByteArray
        )

        @Throws(IllegalArgumentException::class)
        fun decode(encodedAccountIdentifier: String): AccountIdentifier {
            val decodeAccountIdentifier = Bech32Segwit.decode(encodedAccountIdentifier)

            val networkPrefix = NetworkPrefix.parse(decodeAccountIdentifier.hrp)
                ?: throw  IllegalArgumentException(
                    String.format(
                        "Invalid network prefix : %s",
                        decodeAccountIdentifier.hrp
                    )
                )
            val version = decodeAccountIdentifier.data[0]
            if (version != VERSION_1) {
                throw IllegalArgumentException(
                    String.format(
                        "unknown account identifier format version: %s",
                        version.toString()
                    )
                );
            }

            val dataNoVersion =
                decodeAccountIdentifier.data.copyOfRange(1, decodeAccountIdentifier.data.size)

            val bytes = Bech32Segwit.convertBits(dataNoVersion, 0, dataNoVersion.size, 5, 8, false)

            val addressChars = bytes.copyOfRange(0, 16)
            val subAddressChars = bytes.copyOfRange(16, bytes.size)
            return AccountIdentifier(
                networkPrefix,
                AccountAddress(addressChars),
                SubAddress(subAddressChars)
            )
        }
    }

    fun getPrefix(): NetworkPrefix {
        return prefix
    }

    fun getAccountAddress(): AccountAddress {
        return accountAddress
    }

    fun getSubAddress(): SubAddress {
        return subAddress
    }

    fun encodeV1(): String {
        val accountAddressBytes = accountAddress.byte
        val subAddressBytes = subAddress.getBytes()
        val program = accountAddressBytes + subAddressBytes
        val data = Bech32Segwit.convertBits(program, 0, program.size, 8, 5, true)
        val versionByte = byteArrayOf(VERSION_1)
        val versionAndData = versionByte + data
        return Bech32Segwit.encode(prefix.value, versionAndData)
    }

    override fun toString(): String {
        return "AccountIdentifier{" +
                "prefix=" + prefix +
                ", accountAddress=" + accountAddress +
                ", subAddress=" + subAddress +
                '}'
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as AccountIdentifier
        return prefix === that.prefix &&
                Arrays.equals(accountAddress.byte, that.accountAddress.byte) &&
                Arrays.equals(subAddress.getBytes(), that.subAddress.getBytes())
    }

    override fun hashCode(): Int {
        return Objects.hash(prefix, accountAddress, subAddress)
    }

    enum class NetworkPrefix(val value: String) {
        MainnetPrefix("lbr"),
        TestnetPrefix("tlb"),
        PreMainnetPrefix("plb");

        companion object {
            fun parse(prefix: String): NetworkPrefix? {
                return when (prefix) {
                    "lbr" -> MainnetPrefix
                    "tlb" -> TestnetPrefix
                    "plb" -> PreMainnetPrefix
                    else -> null
                }
            }
        }
    }
}