package org.palliums.libracore

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.palliums.libracore.serialization.hexToBytes
import org.palliums.libracore.transaction.AccountAddress
import org.palliums.libracore.wallet.AccountIdentifier
import org.palliums.libracore.wallet.IntentIdentifier
import org.palliums.libracore.wallet.SubAddress

@RunWith(AndroidJUnit4::class)
class ScanUrlUnitTest {
    @Test
    fun test_decode_url() {
        val url = "libra://lbr1p7ujcndcl7nudzwt8fglhx6wxn08kgs5tm6mz4usw5p72t?c=lbr&am=3000"

        val decode = IntentIdentifier.decode(url)
        val accountIdentifier = decode.getAccountIdentifier()
        val amount = decode.getAmount()
        val currency = decode.getCurrency()

        Log.e("--:--", accountIdentifier.toString())
        Log.e("--:--", amount.toString())
        Log.e("--:--", currency.toString())
    }

    @Test
    fun test_encode_url() {
        val url = "libra://lbr1p7ujcndcl7nudzwt8fglhx6wxn08kgs5tm6mz4usw5p72t?am=3000&c=lbr"

        val encode = IntentIdentifier(
            AccountIdentifier(
                AccountIdentifier.NetworkPrefix.MainnetPrefix,
                AccountAddress("f72589b71ff4f8d139674a3f7369c69b".hexToBytes()),
                SubAddress("cf64428bdeb62af2")
            ),
            "lbr",
            3000
        ).encode()

        Assert.assertEquals(encode, url)
    }

    @Test
    fun test_decode_url1() {
        kotlin.run {
            val url = "libra://lbr1p7ujcndcl7nudzwt8fglhx6wxn08kgs5tm6mz4usw5p72t?c=lbr&am=3000"
            val decode = IntentIdentifier.decode(url)
            Log.e("--:--", decode.toString())
        }
        kotlin.run {
            val url = "libra://lbr1p7ujcndcl7nudzwt8fglhx6wxn08kgs5tm6mz4usw5p72t"
            val decode = IntentIdentifier.decode(url)
            Log.e("--:--", decode.toString())
        }
        kotlin.run {
            val url = "libra://lbr1p7ujcndcl7nudzwt8fglhx6wxn08kgs5tm6mz4usw5p72t?c"
            val decode = IntentIdentifier.decode(url)
            Log.e("--:--", decode.toString())
        }
        kotlin.run {
            val url = "libra://lbr1p7ujcndcl7nudzwt8fglhx6wxn08kgs5tm6mz4usw5p72t?c=lbr"
            val decode = IntentIdentifier.decode(url)
            Log.e("--:--", decode.toString())
        }
        kotlin.run {
            val url = "libra://lbr1p7ujcndcl7nudzwt8fglhx6wxn08kgs5tm6mz4usw5p72t?c=lbr&am"
            val decode = IntentIdentifier.decode(url)
            Log.e("--:--", decode.toString())
        }
    }

    @Test
    fun test_encode_url1() {
        kotlin.run {
            val url = "libra://lbr1p7ujcndcl7nudzwt8fglhx6wxn08kgs5tm6mz4usw5p72t?am=3000&c=lbr"

            val encode = IntentIdentifier(
                AccountIdentifier(
                    AccountIdentifier.NetworkPrefix.MainnetPrefix,
                    AccountAddress("f72589b71ff4f8d139674a3f7369c69b".hexToBytes()),
                    SubAddress("cf64428bdeb62af2")
                ),
                "lbr",
                3000
            ).encode()

            Assert.assertEquals(encode, url)
        }
        kotlin.run {
            val url = "libra://lbr1p7ujcndcl7nudzwt8fglhx6wxn08kgs5tm6mz4usw5p72t"

            val encode = IntentIdentifier(
                AccountIdentifier(
                    AccountIdentifier.NetworkPrefix.MainnetPrefix,
                    AccountAddress("f72589b71ff4f8d139674a3f7369c69b".hexToBytes()),
                    SubAddress("cf64428bdeb62af2")
                )
            ).encode()

            Assert.assertEquals(encode, url)
        }

        kotlin.run {
            val url = "libra://lbr1p7ujcndcl7nudzwt8fglhx6wxn08kgs5tm6mz4usw5p72t?c=lbr"

            val encode = IntentIdentifier(
                AccountIdentifier(
                    AccountIdentifier.NetworkPrefix.MainnetPrefix,
                    AccountAddress("f72589b71ff4f8d139674a3f7369c69b".hexToBytes()),
                    SubAddress("cf64428bdeb62af2")
                ),
                "lbr"
            ).encode()

            Assert.assertEquals(encode, url)
        }

        kotlin.run {
            val url = "libra://lbr1p7ujcndcl7nudzwt8fglhx6wxn08kgs5tm6mz4usw5p72t?am=3000"

            val encode = IntentIdentifier(
                AccountIdentifier(
                    AccountIdentifier.NetworkPrefix.MainnetPrefix,
                    AccountAddress("f72589b71ff4f8d139674a3f7369c69b".hexToBytes()),
                    SubAddress("cf64428bdeb62af2")
                ),
                amount = 3000
            ).encode()

            Assert.assertEquals(encode, url)
        }
    }
}