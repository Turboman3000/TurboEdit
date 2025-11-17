package turboedit.shared.project

@JvmRecord
data class TimelineData(
    val name: String, val length: Long, val videoLayers: ArrayList<TimelineLayer>,
    val audioLayers: ArrayList<TimelineLayer>, val clips: ArrayList<TimelineClip>
)
