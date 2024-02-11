package com.ehhthan.scholarlee.pack.file;

import org.jetbrains.annotations.NotNull;

public enum InternalLocation implements AssetLocation {
    FONT("font", ".json"),
    FONT_HEX_FILE("", ".zip"),
    TEXTURES("textures", ".png");

    private final String path;
    private final String extension;

    InternalLocation(@NotNull String path, @NotNull String extension) {
        this.path = path;
        this.extension = extension;
    }

    public String getPath() {
        return path;
    }

    public String getExtension() {
        return extension;
    }

    public String appendExtension(@NotNull String path) {
        if (!path.endsWith(extension))
            path = path + extension;

        return path;
    }
}
