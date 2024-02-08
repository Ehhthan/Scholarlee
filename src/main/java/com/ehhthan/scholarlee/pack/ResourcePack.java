package com.ehhthan.scholarlee.pack;

import com.google.gson.JsonObject;

import java.awt.image.BufferedImage;
import java.io.File;

public interface ResourcePack {
    static String DEFAULT_NAMESPACE = "minecraft";

    BufferedImage getImageFile(File file);

    JsonObject getJsonFile(File file);

    File getFile(String namespace, String type, String path);

    default BufferedImage getImageFile(String path) {
        return getImageFile(getFileFromPath(path, "textures"));
    };

    default File getFileFromPath(String path, String type) {
        String[] split = path.split(":");
        if (split.length == 1) {
            return getFile(DEFAULT_NAMESPACE, type, split[0]);
        } else if (split.length == 2) {
            return getFile(split[0], type, split[1]);
        } else {
            throw new IllegalArgumentException(String.format("Path is incorrectly formatted: %s", path));
        }
    }

    default BufferedImage getImageFile(String namespace, String type, String path) {
        return getImageFile(getFile(namespace, type, path));
    }

    default JsonObject getJsonFile(String namespace, String type, String path) {
        return getJsonFile(getFile(namespace, type, path));
    }
}
