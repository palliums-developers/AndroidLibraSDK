package org.palliums.libracore.transaction

import org.palliums.libracore.serialization.LCSInputStream
import org.palliums.libracore.serialization.LCSOutputStream

class UnstructuredBytesMetadata(val metadata: ByteArray?) {

    fun serialize(): ByteArray {
        val output = LCSOutputStream()
        output.writeBytesOrNull(metadata)
        return output.toByteArray()
    }

    override fun equals(obj: Any?): Boolean {
        if (this === obj) return true
        if (obj == null) return false
        if (javaClass != obj.javaClass) return false
        val other = obj as UnstructuredBytesMetadata
        return metadata.contentEquals(other.metadata)
    }

    override fun hashCode(): Int {
        var value = 7
        value = 31 * value + if (metadata != null) metadata.hashCode() else 0
        return value
    }

    companion object {
        fun decode(input: LCSInputStream): UnstructuredBytesMetadata {
            val metadata = input.readBytesOrNull()
            return UnstructuredBytesMetadata(metadata)
        }
    }
}