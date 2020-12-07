package org.palliums.libracore.transaction

import org.palliums.libracore.serialization.LCS
import org.palliums.libracore.serialization.LCSInputStream
import org.palliums.libracore.serialization.LCSOutputStream
import java.io.InputStream
import java.lang.IllegalArgumentException
import java.util.*


abstract class Metadata {
    abstract fun serialize(): ByteArray
    class Undefined : Metadata() {
        companion object {
            fun decode(input: InputStream): Undefined {
                return Undefined()
            }
        }

        override fun serialize(): ByteArray {
            return LCS.encodeIntAsULEB128(0)
        }

        override fun equals(obj: Any?): Boolean {
            if (this === obj) return true
            if (obj == null) return false
            if (javaClass != obj.javaClass) return false
            val other = obj as Undefined
            return true
        }

        override fun hashCode(): Int {
            return 7
        }
    }

    class GeneralMetadataDefine(val value: GeneralMetadata) :
        Metadata() {
        override fun serialize(): ByteArray {
            val output = LCSOutputStream()
            output.writeIntAsLEB128(1)
            output.write(value.serialize())
            return output.toByteArray()
        }

        override fun equals(obj: Any?): Boolean {
            if (this === obj) return true
            if (obj == null) return false
            if (javaClass != obj.javaClass) return false
            val other = obj as GeneralMetadataDefine
            return value == other.value
        }

        override fun hashCode(): Int {
            var value = 7
            value = 31 * value + this.value.hashCode()
            return value
        }

        companion object {
            @Throws(IllegalArgumentException::class)
            fun decode(input: LCSInputStream): GeneralMetadataDefine {
                return GeneralMetadataDefine(
                    GeneralMetadata.decode(
                        input
                    )
                )
            }
        }
    }

    class TravelRuleMetadataDefine(val value: TravelRuleMetadata) :
        Metadata() {

        override fun serialize(): ByteArray {
            val output = LCSOutputStream()
            output.writeIntAsLEB128(2)
            output.write(value.serialize())
            return output.toByteArray()
        }

        override fun equals(obj: Any?): Boolean {
            if (this === obj) return true
            if (obj == null) return false
            if (javaClass != obj.javaClass) return false
            val other = obj as TravelRuleMetadataDefine
            return value == other.value
        }

        override fun hashCode(): Int {
            var value = 7
            value = 31 * value + this.value.hashCode()
            return value
        }

        companion object {
            fun decode(input: LCSInputStream): TravelRuleMetadataDefine {
                return TravelRuleMetadataDefine(
                    TravelRuleMetadata.decode(
                        input
                    )
                )
            }
        }
    }

    class UnstructuredBytesMetadataDefine(val value: UnstructuredBytesMetadata) :
        Metadata() {

        override fun serialize(): ByteArray {
            return LCS.encodeIntAsULEB128(3)
        }

        override fun equals(obj: Any?): Boolean {
            if (this === obj) return true
            if (obj == null) return false
            if (javaClass != obj.javaClass) return false
            val other = obj as UnstructuredBytesMetadataDefine
            return value == other.value
        }

        override fun hashCode(): Int {
            var value = 7
            value = 31 * value + this.value.hashCode()
            return value
        }

        companion object {
            fun decode(input: LCSInputStream): UnstructuredBytesMetadataDefine {
                return UnstructuredBytesMetadataDefine(UnstructuredBytesMetadata.decode(input))
            }
        }
    }

    companion object {
        @Throws(IllegalArgumentException::class)
        fun decode(input: LCSInputStream): Metadata {
            val index: Int = LCS.decodeIntAsULEB128(input)
            return when (index) {
                0 -> Undefined.decode(input)
                1 -> GeneralMetadataDefine.decode(input)
                2 -> TravelRuleMetadataDefine.decode(input)
                3 -> UnstructuredBytesMetadataDefine.decode(input)
                else -> throw IllegalArgumentException("Unknown variant index for Metadata: $index")
            }
        }
    }
}