package com.ehhthan.scholarlee.pack;

import com.ehhthan.scholarlee.api.NamespacedKey;
import com.ehhthan.scholarlee.pack.file.AssetLocation;
import com.ehhthan.scholarlee.pack.file.InternalLocation;
import com.google.gson.JsonObject;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Set;

public interface ResourcePack {
    String DEFAULT_NAMESPACE = "minecraft";

    Set<String> getNamespaces();

    File getAssetsDirectory();

    BufferedImage getImageFile(File file);

    JsonObject getJsonFile(File file);

    File getFile(NamespacedKey namespacedKey, AssetLocation type);

    NamespacedKey getNamespacedKey(File file);

    default BufferedImage getTextureFile(NamespacedKey namespacedKey) {
        return getImageFile(getFile(namespacedKey, InternalLocation.TEXTURES));
    };

    default JsonObject getFontFile(NamespacedKey namespacedKey) {
        return getJsonFile(getFile(namespacedKey, InternalLocation.FONT));
    };
}
