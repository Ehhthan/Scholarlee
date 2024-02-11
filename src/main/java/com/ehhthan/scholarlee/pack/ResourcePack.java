package com.ehhthan.scholarlee.pack;

import com.ehhthan.scholarlee.api.NamespacedKey;
import com.ehhthan.scholarlee.pack.assets.font.provider.FontProvider;
import com.ehhthan.scholarlee.pack.file.AssetLocation;
import com.ehhthan.scholarlee.pack.file.InternalLocation;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.Set;

public interface ResourcePack {
    Set<String> getNamespaces();

    File getAssetsDirectory();

    default boolean isUsingDefaultAssets() {
        return getDefaultAssetsDirectory() != null;
    }

    File getDefaultAssetsDirectory();

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
