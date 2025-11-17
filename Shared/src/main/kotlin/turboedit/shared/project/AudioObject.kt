package turboedit.shared.project

@JvmRecord
data class AudioObject(val layer: Int, val layerName: String, val waveform: ByteArray) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AudioObject

        if (layer != other.layer) return false
        if (layerName != other.layerName) return false
        if (!waveform.contentEquals(other.waveform)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = layer
        result = 31 * result + layerName.hashCode()
        result = 31 * result + waveform.contentHashCode()
        return result
    }

}
