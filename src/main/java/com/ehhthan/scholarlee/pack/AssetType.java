package com.ehhthan.scholarlee.pack;

import org.jetbrains.annotations.NotNull;

public enum AssetType {
    FONT("font", ".json"),
    TEXTURE("texture", ".png");

    private final String path;
    private final String extension;

    AssetType(@NotNull String path, @NotNull String extension) {
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
