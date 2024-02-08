package com.ehhthan.scholarlee.pack;

import com.ehhthan.scholarlee.api.NamespacedKey;
import com.google.gson.JsonObject;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Set;

public interface ResourcePack {
    static String DEFAULT_NAMESPACE = "minecraft";

    Set<String> getNamespaces();

    File getAssetsDirectory();

    BufferedImage getImageFile(File file);

    JsonObject getJsonFile(File file);

    File getFile(NamespacedKey namespacedKey, AssetType type);

    default BufferedImage getTextureFile(NamespacedKey namespacedKey) {
        return getImageFile(getFile(namespacedKey, AssetType.TEXTURE));
    };

    default JsonObject getFontFile(NamespacedKey namespacedKey) {
        return getJsonFile(getFile(namespacedKey, AssetType.FONT));
    };
}
