package org.palliums.libracore

import org.junit.Test
import org.junit.Assert.assertEquals
import org.palliums.libracore.utils.HexUtils
import org.palliums.libracore.wallet.Account
import org.palliums.libracore.crypto.KeyPair


class KeyPairTest {
    @Test
    fun test_generate_mnemonics_keypair() {
        val mnemonics = arrayListOf(
            "museum",
            "liquid",
            "spider",
            "explain",
            "vicious",
            "pave",
            "silent",
            "allow",
            "depth",
            "you",
            "adjust",
            "begin"
        )

        val keyPair = KeyPair.fromMnemonic(mnemonics)

        println("private key: ${keyPair.getPrivateKey().toHex()}")
        println("public key: ${keyPair.getPublicKey().toHex()}")
        println("address: ${Account(keyPair).getAddress().toHex()}")
        assertEquals(
            keyPair.getPrivateKey().toHex(),
            "abfd325f4a5bb45f99b0fbd601922ca5467d7814ab63a01f7a83699cfc765a8b"
        )
        assertEquals(
            keyPair.getPublicKey().toHex(),
            "f7259d7016ac5efdb5c90a203cf7ff39905b6c5a3190b3b89958d32a03dc7600"
        )
        assertEquals(
            Account(keyPair).getAddress().toHex(),
            "32a616558102813269d6e3ab8e7f2333"
        )
    }

    @Test
    fun test_sign() {
        val keyPair =
            KeyPair.fromSecretKey(HexUtils.fromHex("ed7cc526bc39db7c754f1f90fbb5b7f7ce3499bee04e7525c3c599fcaa46aaea"))
        val message = byteArrayOf(0x1)
        val messageSign = keyPair.signMessage(message)
        assertEquals(
            messageSign.toHex(),
            "738ef56acbc0b13fe9c6acb6c300abc03da5554e7eda7b2fde0eff01095ade9b68038703bb7019e747a75d3f03308cc22c72cb27ea85b9e1080168b384b04f05"
        )
    }
}