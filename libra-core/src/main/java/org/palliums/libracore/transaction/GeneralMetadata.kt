package org.palliums.libracore.transaction

import org.palliums.libracore.serialization.LCSInputStream
import org.palliums.libracore.serialization.LCSOutputStream
import java.lang.IllegalArgumentException
import java.util.*

abstract class GeneralMetadata {
    abstract fun serialize(): ByteArray

    companion object {
        @Throws(IllegalArgumentException::class)
        fun decode(input: LCSInputStream): GeneralMetadata {
            return when (input.readIntAsLEB128()) {
                0 -> {
                    GeneralMetadataVersion0.decode(input)
                }
                else -> {
                    throw IllegalArgumentException("The address parameter is incorrect")
                }
            }
        }
    }
}

class GeneralMetadataVersion0(val value: GeneralMetadataV0) : GeneralMetadata() {

    override fun serialize(): ByteArray {
        val output =LCSOutputStream()
        output.writeIntAsLEB128(0)
        output.write(value.serialize())
        return output.toByteArray()
    }

    override fun equals(obj: Any?): Boolean {
        if (this === obj) return true
        if (obj == null) return false
        if (javaClass != obj.javaClass) return false
        val other = obj as GeneralMetadataVersion0
        return value == other.value
    }

    override fun hashCode(): Int {
        var value = 7
        value = 31 * value + if (this.value != null) this.value.hashCode() else 0
        return value
    }

    companion object {
        fun decode(input: LCSInputStream): GeneralMetadataVersion0 {
            return GeneralMetadataVersion0(GeneralMetadataV0.decode(input))
        }
    }
}