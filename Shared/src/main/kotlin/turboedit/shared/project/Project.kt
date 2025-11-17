package turboedit.shared.project

@JvmRecord
data class Project(
    val path: String?, val name: String, val fileVersion: Int, val files: ArrayList<ProjectFile>,
    val timelines: ArrayList<TimelineData>
)
