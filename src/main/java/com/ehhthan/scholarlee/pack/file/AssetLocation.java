package com.ehhthan.scholarlee.pack.file;

import org.jetbrains.annotations.NotNull;

public interface AssetLocation {
    String getPath();

    String getExtension();

    default String appendExtension(@NotNull String path) {
        if (!path.endsWith(getExtension()))
            path = path + getExtension();

        return path;
    }
}
