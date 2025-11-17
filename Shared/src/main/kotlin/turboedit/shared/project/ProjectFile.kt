package turboedit.shared.project

@JvmRecord
data class ProjectFile(
    val name: String,
    val path: String,
    val type: String,
    val previewImage: ByteArray?,
    val hash: String,
    val size: Long,
    val isVideo: Boolean,
    val videoWidth: Int?,
    val videoHeight: Int?,
    val videoFPS: Int?,
    val videoCodec: String?,
    val audio: ArrayList<AudioObject>?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProjectFile

        if (size != other.size) return false
        if (isVideo != other.isVideo) return false
        if (videoWidth != other.videoWidth) return false
        if (videoHeight != other.videoHeight) return false
        if (videoFPS != other.videoFPS) return false
        if (name != other.name) return false
        if (path != other.path) return false
        if (type != other.type) return false
        if (!previewImage.contentEquals(other.previewImage)) return false
        if (hash != other.hash) return false
        if (videoCodec != other.videoCodec) return false
        if (audio != other.audio) return false

        return true
    }

    override fun hashCode(): Int {
        var result = size.hashCode()
        result = 31 * result + isVideo.hashCode()
        result = 31 * result + (videoWidth ?: 0)
        result = 31 * result + (videoHeight ?: 0)
        result = 31 * result + (videoFPS ?: 0)
        result = 31 * result + name.hashCode()
        result = 31 * result + path.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + previewImage.contentHashCode()
        result = 31 * result + hash.hashCode()
        result = 31 * result + (videoCodec?.hashCode() ?: 0)
        result = 31 * result + (audio?.hashCode() ?: 0)
        return result
    }
}
