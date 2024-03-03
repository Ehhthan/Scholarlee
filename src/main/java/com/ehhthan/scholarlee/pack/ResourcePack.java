package com.ehhthan.scholarlee.pack;

import com.ehhthan.scholarlee.pack.assets.font.PackFont;
import com.ehhthan.scholarlee.pack.assets.font.provider.FontProvider;
import com.ehhthan.scholarlee.pack.key.NamespacedKey;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.awt.image.BufferedImage;
import java.util.zip.ZipFile;

public interface ResourcePack {
    Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(FontProvider.class, new FontProvider.Type.Adapter())
        .create();

    BufferedImage getTexture(NamespacedKey namespacedKey);

    PackFont getFont(NamespacedKey namespacedKey);

    ZipFile getZipFile(NamespacedKey namespacedKey);

    enum AssetType {
        FONT("font", ".json"),
        TEXTURE("textures", ".png"),
        ZIP_FILE(null, ".zip");

        private final String parent;
        private final String extension;

        AssetType(String parent, String extension) {
            this.parent = parent;
            this.extension = extension;
        }

        public String getParent() {
            return parent;
        }

        public String getExtension() {
            return extension;
        }
    }
}
