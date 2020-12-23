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
        val seed = Seed.fromMnemonic(generateMnemonic())
        val seedHexStr = Hex.toHexString(seed.data)
        println()
        println("Wallet seed: $seedHexStr")

        Assert.assertEquals(
            seedHexStr,
            "4e8366c85f3414d0af36152230e7bbeb49929e4ac48e2fe860853883884244db"
        )
    }

    @Test
    fun testMasterPrk() {
        val keyFactory = KeyFactory(
            Seed.fromMnemonic(generateMnemonic())
        )
        val masterPrkStr = Hex.toHexString(keyFactory.masterPrk)
        println()
        println("Master Private Key: $masterPrkStr")

        Assert.assertEquals(
            masterPrkStr,
            "66ae6b767defe3ea0c646f10bf31ad3b36f822064d3923adada7676703a350c0"
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
            "732bc883893c716f320c01864709ca9f16f8f30342a1de42144bfcc2ddb7af10"
        )
        Assert.assertEquals(
            publicKey,
            "eeb106043cc01f9fe6440a1490552b1b17c4f0b399a76ccdfcf686a3c73e4b58"
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
            "8af6591e6ecd0a120fd6cc3c8451be0d3f02d179bf8acd109e62f3ec2fe637b4338eeddc23e5f5dfa52887b9e1a416ad166afd8c0effb99ec1426fffd7abda00"
        )
    }

    @Test
    fun testNewAccount() {
        val mnemonicWords = generateMnemonic()

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
            account1.keyPair.getPrivateKey().toHex(),
            "732bc883893c716f320c01864709ca9f16f8f30342a1de42144bfcc2ddb7af10"
        )
        Assert.assertEquals(
            account2.keyPair.getPrivateKey().toHex(),
            "f6b472bd0941e315d3c34c3ac679d610d2b9e1abe85128752d04bb0f042f3391"
        )
        Assert.assertEquals(
            address1,
            "5c52cff595f5c0df90a5bcde5ec81663"
        )
        Assert.assertEquals(
            address2,
            "ddd824775c3e4416debf8592f83ae60b"
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
            "legal winner thank year wave sausage worth useful legal winner thank year wave sausage worth useful legal will"
//            "trouble menu nephew group alert recipe hotel fatigue wet shadow say fold huge olive solution enjoy garden appear vague joy great keep cactus melt"
        val mnemonicWords = mnemonic.split(" ")

        return mnemonicWords
    }
}