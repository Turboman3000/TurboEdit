package turboedit.shared.project

@JvmRecord
data class TimelineClip(val file: Int, val position: Long, val startTime: Long, val endTime: Long)
