package org.palliums.libracore.wallet

import org.junit.Assert
import org.junit.Test
import org.palliums.libracore.crypto.Ed25519Signature
import org.palliums.libracore.crypto.KeyFactory
import org.palliums.libracore.crypto.Seed
import org.palliums.libracore.crypto.sha3
import org.palliums.libracore.serialization.toHex
import org.spongycastle.util.encoders.Hex

/**
 * Created by elephant on 2019-09-23 16:07.
 * Copyright © 2019-2020. All rights reserved.
 * <p>
 * desc:
 */
class WalletTest {

    @Test
    fun testGenerateSeed() {
        val seed = Seed.fromMnemonic(generateMnemonic(), "LIBRA")
        val seedHexStr = Hex.toHexString(seed.data)
        println()
        println("Wallet seed: $seedHexStr")

        Assert.assertEquals(
            seedHexStr,
            "9fca0ead4ba52cfe1514785ff0a808f575cb72b74e5cb1e0686c991f058874b0"
        )
    }

    @Test
    fun testMasterPrk() {
        val keyFactory = KeyFactory(
            Seed.fromMnemonic(
                generateMnemonic(),
                "LIBRA"
            )
        )
        val masterPrkStr = Hex.toHexString(keyFactory.masterPrk)
        println()
        println("Master Private Key: $masterPrkStr")

        Assert.assertEquals(
            masterPrkStr,
            "5f6b55c1d40eaa008a807d534d2a840f10243c2c68b0d208ddd5e00f56f945e1"
        )
    }

    @Test
    fun testGenerateKey() {
        val libraWallet = LibraWallet(WalletConfig(generateMnemonic()))
        val account = libraWallet.newAccount()
        val privateKey = account.keyPair.getPrivateKey().toHex()
        val publicKey = account.keyPair.getPublicKey().toHex()
        println()
        println("Private Key: $privateKey")
        println("Public Key: $publicKey")

        Assert.assertEquals(
            privateKey,
            "7d2626e62d6b513c54a05d2104b58f6c5d645d1ddc62292350f6ca7fed4c2b58"
        )
        Assert.assertEquals(
            publicKey,
            "3c0485e4ac6bcbd7f970f2e5e5ab24a3c467c7cf0d2c2143e0497e16a780cdb5"
        )
    }

    @Test
    fun testSign() {
        val libraWallet = LibraWallet(WalletConfig(generateMnemonic()))
        val account = libraWallet.newAccount()
        val signHexStr = account.keyPair.signMessage(Hex.decode("1234567890")).toHex()
        println()
        println("message sign: $signHexStr")

        Assert.assertEquals(
            signHexStr,
            "ccd697367b79f3bc7b448cbf95d1c3e1c10584ac2e1a3b95df0f0071cc70d0346db2895768ab2c0eaef9d8867c2506ee1798be6a66ff6b40f67fde82f1a1390c"
        )
    }

    @Test
    fun testNewAccount() {
        //        val mnemonic =
//            "school problem vibrant royal invite that never key thunder pizza mesh punch"
        val mnemonic =
            "velvet version sea near truly open blanket exchange leaf cupboard shine poem"
//        val mnemonic =
//            "key shoulder focus dish donate inmate move weekend hold regret peanut link"

        val mnemonicWords = mnemonic.split(" ")

        val libraWallet = LibraWallet(WalletConfig(mnemonicWords))

        val account1 = libraWallet.newAccount()
        val account2 = libraWallet.newAccount()

        val authenticationKey1 = account1.getAuthenticationKey().toHex()
        val address1 = account1.getAddress().toHex()

        val authenticationKey2 = account2.getAuthenticationKey().toHex()
        val address2 = account2.getAddress().toHex()
        println()
        println("Account 1 private key: ${account1.keyPair.getPrivateKey().toHex()}")
        println("Account 2 private key: ${account2.keyPair.getPrivateKey().toHex()}")
        println()
        println("Account 1 public key: ${account1.keyPair.getPublicKey().toHex()}")
        println("Account 2 public key: ${account2.keyPair.getPublicKey().toHex()}")
        println()
        println("Account 1 AuthenticationKey: $authenticationKey1")
        println("Account 2 AuthenticationKey: $authenticationKey2")
        println()
        println("Account 1 address: $address1")
        println("Account 2 address: $address2")

        Assert.assertEquals(
            address1,
            "53e59e4b4fa3c35770846f6c87ca2d35"
        )
        Assert.assertEquals(
            address2,
            "77c3734e451f4fe9c105ed1c6af53d3c"
        )
    }

    @Test
    fun testSignSimpleAndVerifySimple() {
        val libraWallet = LibraWallet(WalletConfig(generateMnemonic()))
        val account = libraWallet.newAccount()

        val data = "session_id_123"
        val signedData = account.keyPair.signMessage(data.toByteArray().sha3())
        println()
        println("data sign simple: ${signedData.toHex()}")

        val result = account.keyPair.verify(signedData, data.toByteArray().sha3())

        Assert.assertEquals(
            result,
            true
        )
    }

    private fun generateMnemonic(): List<String> {
        val mnemonic =
            "velvet version sea near truly open blanket exchange leaf cupboard shine poem"
        val mnemonicWords = mnemonic.split(" ")

        return mnemonicWords
    }
}