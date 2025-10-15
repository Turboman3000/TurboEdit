package org.turbomedia.turboedit.editor.renderer;

import static org.turbomedia.turboedit.editor.misc.Locale.GetText;

public enum FileResolveMethod {
    STREAMING("file_resolve_method.streaming"),
    MAPPING("file_resolve_method.mapping"),
    UPLOAD("file_resolve_method.upload");

    public final String translationKey;

    @Override
    public String toString() {
        return GetText(translationKey);
    }

    FileResolveMethod(String translationKey) {
        this.translationKey = translationKey;
    }
}
