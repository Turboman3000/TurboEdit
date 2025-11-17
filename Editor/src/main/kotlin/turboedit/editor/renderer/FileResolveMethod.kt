package turboedit.editor.renderer

import turboedit.editor.misc.Locale

enum class FileResolveMethod(val translationKey: String) {
    STREAMING("file_resolve_method.streaming"),
    MAPPING("file_resolve_method.mapping"),
    UPLOAD("file_resolve_method.upload");

    override fun toString(): String {
        return Locale.getText(translationKey)
    }
}
