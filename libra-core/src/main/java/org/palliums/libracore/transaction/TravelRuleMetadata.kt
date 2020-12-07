package org.palliums.libracore.transaction

import org.palliums.libracore.serialization.LCSInputStream
import org.palliums.libracore.serialization.LCSOutputStream
import java.lang.IllegalArgumentException

abstract class TravelRuleMetadata {
    companion object {
        @Throws(IllegalArgumentException::class)
        fun decode(input: LCSInputStream): TravelRuleMetadata {
            val index = input.readIntAsLEB128()
            return when (index) {
                0 -> TravelRuleMetadataVersion0.decode(input)
                else -> throw IllegalArgumentException("Unknown variant index for TravelRuleMetadata: $index")
            }
        }
    }

    abstract fun serialize(): ByteArray
}

class TravelRuleMetadataVersion0(val value: TravelRuleMetadataV0) : TravelRuleMetadata() {
    companion object {
        fun decode(input: LCSInputStream): TravelRuleMetadataVersion0 {
            return TravelRuleMetadataVersion0(TravelRuleMetadataV0.decode(input))
        }
    }

    override fun serialize(): ByteArray {
        val output =LCSOutputStream()
        output.writeIntAsLEB128(0)
        output.write(value.serialize())
        return output.toByteArray()
    }

}