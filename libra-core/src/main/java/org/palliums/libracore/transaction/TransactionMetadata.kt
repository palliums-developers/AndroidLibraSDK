package org.palliums.libracore.transaction

import org.palliums.libracore.serialization.LCSOutputStream
import org.palliums.libracore.wallet.SubAddress


/**
 * <p>Utils for creating peer to peer transaction metadata.</p>
 *
 * @see <a href="https://github.com/libra/lip/blob/master/lips/lip-4.md">LIP-4 Transaction Metadata Specification</a>
 */
class TransactionMetadata(
    val metadata: ByteArray,
    val signatureMessage: ByteArray
) {
    companion object {
        /**
         * createTravelRuleMetadata creates metadata and signature message for given
         * offChainReferenceID.
         * This is used for peer to peer transfer between 2 custodial accounts when the amount is over threshold.
         *
         * @param offChainReferenceId  Get this id from off-chain API communication.
         * @param senderAccountAddress sender account address
         * @param amount               transfer amount
         * @return TransactionMetadata
         */
        fun createTravelRuleMetadata(
            offChainReferenceId: String?,
            senderAccountAddress: AccountAddress, amount: Long
        ): TransactionMetadata {
            val travelRuleMetadata: Metadata.TravelRuleMetadataDefine =
                Metadata.TravelRuleMetadataDefine(
                    TravelRuleMetadataVersion0(
                        TravelRuleMetadataV0(offChainReferenceId)
                    )
                )

            // receiver_lcs_data = lcs(metadata, sender_address, amount, "@@$$LIBRA_ATTEST$$@@" /*ASCII-encoded string*/);

            val metadataBytes: ByteArray = travelRuleMetadata.serialize()

            val output = LCSOutputStream()
            output.write(travelRuleMetadata.serialize())
            output.write(senderAccountAddress.toByteArray())
            output.writeLong(amount)
            val signatureMessage: ByteArray =
                output.toByteArray() + "@@$\$LIBRA_ATTEST$$@@".toByteArray()
            return TransactionMetadata(metadataBytes, signatureMessage)
        }

        /**
         * creates metadata for creating peer to peer transaction script with toSubAddress.
         * This is used for peer to peer transfer from non-custodial account to custodial account.
         *
         * @param toSubAddress
         * @return TransactionMetadata
         */
        fun createGeneralMetadataToSubAddress(toSubAddress: SubAddress): TransactionMetadata {
            return createGeneralMetadata(
                null,
                toSubAddress.getBytes(),
                null
            )
        }

        /**
         * Creates metadata for creating peer to peer transaction script with fromSubAddress.
         * This is used for peer to peer transfer from custodial account to non-custodial account.
         *
         * @param fromSubAddress
         * @return TransactionMetadata
         */
        fun createGeneralMetadataFromSubAddress(fromSubAddress: SubAddress): TransactionMetadata {
            return createGeneralMetadata(
                fromSubAddress.getBytes(),
                null, null
            )
        }

        /**
         * Creates metadata for creating peer to peer transaction script with fromSubAddress and toSubAddress.
         * Use this function to create metadata with from and to sub-addresses for peer to peer transfer
         * from custodial account to custodial account under travel rule threshold.
         * @param fromSubAddress
         * @param toSubAddress
         * @return TransactionMetadata
         */
        fun createGeneralMetadataWithFromToSubAddresses(
            fromSubAddress: SubAddress,
            toSubAddress: SubAddress
        ): TransactionMetadata {
            return createGeneralMetadata(
                fromSubAddress.getBytes(),
                toSubAddress.getBytes(),
                null
            )
        }

        fun createGeneralMetadata(
            byteFromSubAddress: ByteArray?,
            toSubAddress: ByteArray?,
            referencedEvent: Long?
        ): TransactionMetadata {
            val generalMetadata = Metadata.GeneralMetadataDefine(
                GeneralMetadataVersion0(
                    GeneralMetadataV0(toSubAddress, byteFromSubAddress, referencedEvent)
                )
            )
            return TransactionMetadata(generalMetadata.serialize(), ByteArray(0))
        }
    }
}