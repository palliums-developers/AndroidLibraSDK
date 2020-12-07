package org.palliums.libracore.transaction

import org.palliums.libracore.serialization.LCSInputStream
import org.palliums.libracore.serialization.LCSOutputStream


class TravelRuleMetadataV0(val off_chain_reference_id: String? = null) {
    companion object {
        fun decode(input: LCSInputStream): TravelRuleMetadataV0 {
            return TravelRuleMetadataV0(input.readStringOrNull())
        }
    }

    fun serialize(): ByteArray {
        val output = LCSOutputStream()
        output.writeStringOrNull(off_chain_reference_id)
        return output.toByteArray()
    }

    override fun equals(obj: Any?): Boolean {
        if (this === obj) return true
        if (obj == null) return false
        if (javaClass != obj.javaClass) return false
        val other = obj as TravelRuleMetadataV0
        return off_chain_reference_id == other.off_chain_reference_id
    }

    override fun hashCode(): Int {
        var value = 7
        value =
            31 * value + if (off_chain_reference_id != null) off_chain_reference_id.hashCode() else 0
        return value
    }
}