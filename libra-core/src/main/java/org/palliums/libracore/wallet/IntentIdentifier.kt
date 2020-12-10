package org.palliums.libracore.wallet

import androidx.core.net.toUri
import java.net.URI
import java.net.URISyntaxException
import java.util.*
import kotlin.collections.HashMap

class IntentIdentifier(
    private val accountIdentifier: AccountIdentifier,
    private val currency: String? = null,
    private val amount: Long? = null
) {

    interface NameValuePair {
        /**
         * Gets the name of this pair.
         *
         * @return the name of this pair, never `null`.
         */
        val name: String?

        /**
         * Gets the value of this pair.
         *
         * @return the value of this pair, may be `null`.
         */
        val value: String?
    }

    companion object {
        const val LIBRA_SCHEME = "diem"
        const val CURRENCY_PARAM_NAME = "c"
        const val AMOUNT_PARAM_NAME = "am"

        @Throws(IllegalArgumentException::class)
        fun decode(intentIdentifier: String): IntentIdentifier {
            val uri: URI
            try {
                uri = URI(intentIdentifier)
            } catch (e: URISyntaxException) {
                throw IllegalArgumentException(e)
            }
            val scheme: String = uri.getScheme()
            require(LIBRA_SCHEME == scheme) {
                String.format(
                    "invalid intent identifier scheme: %s",
                    scheme
                )
            }

            val part = uri.query?.split("&")?.map {
                val split = it.trim().split("=")
                try {
                    Pair(split[0], split[1])
                } catch (e: Exception) {
                    null
                }
            }

            val collect = HashMap<String, String>(part?.size ?: 0)
            part?.forEach {
                it?.let {
                    collect[it.first] = it.second
                }
            }
            val currency = collect.get(CURRENCY_PARAM_NAME)
            val amount = collect.get(AMOUNT_PARAM_NAME)?.toLong()

            val accountIdentifier = AccountIdentifier.decode(uri.getHost())
            return IntentIdentifier(accountIdentifier, currency, amount)
        }
    }

    fun getAccountIdentifier(): AccountIdentifier {
        return accountIdentifier
    }

    fun getCurrency(): String? {
        return currency
    }

    fun getAmount(): Long {
        return amount ?: 0
    }

    /**
     * Encode intent identifier
     *
     * @return encoded intent identifier string
     */
    @Throws(RuntimeException::class)
    fun encode(): String? {
        val encodedAccount = accountIdentifier.encodeV1()
        return try {
            val url = "$LIBRA_SCHEME://$encodedAccount"
            val uriBuilder = url.toUri().buildUpon()
            if (getAmount() > 0) {
                uriBuilder.appendQueryParameter(AMOUNT_PARAM_NAME, amount.toString())
            }
            if (currency != null) {
                uriBuilder.appendQueryParameter(CURRENCY_PARAM_NAME, currency)
            }
            uriBuilder.build().toString()
        } catch (e: URISyntaxException) {
            throw RuntimeException(e)
        }
    }

    override fun toString(): String {
        return "IntentIdentifier{" +
                "accountIdentifier=" + accountIdentifier +
                ", currency='" + currency + '\'' +
                ", amount=" + getAmount() +
                '}'
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as IntentIdentifier
        return amount === that.amount &&
                Objects.equals(accountIdentifier, that.accountIdentifier) &&
                Objects.equals(currency, that.currency)
    }

    override fun hashCode(): Int {
        return Objects.hash(accountIdentifier, currency, amount)
    }
}