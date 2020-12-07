package org.palliums.libracore.transaction

import org.palliums.libracore.serialization.LCSInputStream
import org.palliums.libracore.serialization.LCSOutputStream

class GeneralMetadataV0(
    val toSubaddress: ByteArray?,
    val fromSubaddress: ByteArray?,
    val referencedEvent: Long?
) {
    fun serialize(): ByteArray {
        val stream = LCSOutputStream()
        stream.writeBytesOrNull(toSubaddress)
        stream.writeBytesOrNull(fromSubaddress)
        stream.writeLongOrNull(referencedEvent)
        return stream.toByteArray()
    }

    override fun equals(obj: Any?): Boolean {
        if (this === obj) return true
        if (obj == null) return false
        if (javaClass != obj.javaClass) return false
        val other = obj as GeneralMetadataV0
        if (!toSubaddress.contentEquals(other.toSubaddress)) {
            return false
        }
        if (!fromSubaddress.contentEquals(other.fromSubaddress)) {
            return false
        }
        return referencedEvent == other.referencedEvent
    }

    override fun hashCode(): Int {
        var value = 7
        value = 31 * value + (toSubaddress?.hashCode() ?: 0)
        value = 31 * value + (fromSubaddress?.hashCode() ?: 0)
        value = 31 * value + (referencedEvent?.hashCode() ?: 0)
        return value
    }

    companion object {

        fun decode(input: LCSInputStream): GeneralMetadataV0 {
            val toSubaddress = input.readBytesOrNull()
            val fromSubaddress = input.readBytesOrNull()
            val referencedEvent = input.readLongOrNull()
            return GeneralMetadataV0(toSubaddress, fromSubaddress, referencedEvent)
        }
    }
}
