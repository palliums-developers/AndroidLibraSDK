package org.palliums.libracore.meta

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.palliums.libracore.serialization.hexToBytes
import org.palliums.libracore.serialization.toHex
import org.palliums.libracore.transaction.AccountAddress
import org.palliums.libracore.transaction.TransactionMetadata
import org.palliums.libracore.wallet.SubAddress


@RunWith(AndroidJUnit4::class)
class TransactionMetadata {

    val ACCOUNT_ADDRESS = "f72589b71ff4f8d139674a3f7369c69b"

    private lateinit var accountAddress: AccountAddress

    @Before
    fun setUp() {
        accountAddress = AccountAddress(ACCOUNT_ADDRESS.hexToBytes())
    }

    @Test
    fun test_travel_rule_metadata() {
        val transactionMetadata: TransactionMetadata =
            TransactionMetadata.createTravelRuleMetadata(
                "off chain reference id",
                accountAddress,
                1000
            )
        Assert.assertEquals(
            "020001166f666620636861696e207265666572656e6365206964",
            transactionMetadata.metadata.toHex().toLowerCase()
        )
        Assert.assertEquals(
            "020001166f666620636861696e207265666572656e6365206964f72589b71ff4f8d139674a3f7369c69be803000000000000404024244c494252415f41545445535424244040",
            transactionMetadata.signatureMessage.toHex().toLowerCase()
        )
    }

    @Test
    fun test_general_metadata_to_subAddress() {
        val subAddress = SubAddress("8f8b82153010a1bd")
        val metadata: TransactionMetadata =
            TransactionMetadata.createGeneralMetadataToSubAddress(
                subAddress
            )
        val newGeneralMetadataToSubAddress: ByteArray = metadata.metadata
        Assert.assertEquals(
            "010001088f8b82153010a1bd0000",
            newGeneralMetadataToSubAddress.toHex().toLowerCase()
        )
        Assert.assertArrayEquals(ByteArray(0), metadata.signatureMessage)
    }

    @Test
    fun test_general_metadata_from_subAddress() {
        val subAddress = SubAddress("8f8b82153010a1bd")
        val metadata: TransactionMetadata =
            TransactionMetadata.createGeneralMetadataFromSubAddress(
                subAddress
            )
        val newGeneralMetadataToSubAddress: ByteArray = metadata.metadata
        Assert.assertEquals(
            "01000001088f8b82153010a1bd00",
            newGeneralMetadataToSubAddress.toHex().toLowerCase()
        )
        Assert.assertArrayEquals(ByteArray(0), metadata.signatureMessage)
    }

    @Test
    fun test_general_metadata_from_to_subAddresses() {
        val createGeneralMetadataWithFromToSubAddresses =
            TransactionMetadata.createGeneralMetadataWithFromToSubAddresses(
                SubAddress("8f8b82153010a1bd"),
                SubAddress("111111153010a111")
            )
        val metadata = createGeneralMetadataWithFromToSubAddresses.metadata
        Assert.assertArrayEquals(
            "01000108111111153010a11101088f8b82153010a1bd00".hexToBytes(),
            metadata
        )
    }
}
