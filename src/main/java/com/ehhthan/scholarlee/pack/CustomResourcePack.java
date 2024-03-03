package com.ehhthan.scholarlee.pack;

import com.ehhthan.scholarlee.pack.assets.font.PackFont;
import com.ehhthan.scholarlee.pack.key.NamespacedKey;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipFile;

public class CustomResourcePack implements BuildableResourcePack  {
    private final Map<NamespacedKey, PackFont> fonts = new HashMap<>();
    private final Map<NamespacedKey, BufferedImage> textures = new HashMap<>();
    private final Map<NamespacedKey, File> zipFiles = new HashMap<>();

    private CustomResourcePack() {}

    public static CustomResourcePack builder() {
        return new CustomResourcePack();
    }

    @Override
    public BufferedImage getTexture(NamespacedKey namespacedKey) {
        return textures.get(namespacedKey);
    }

    @Override
    public PackFont getFont(NamespacedKey namespacedKey) {
        return fonts.get(namespacedKey);
    }

    @Override
    public ZipFile getZipFile(NamespacedKey namespacedKey) {
        try {
            return new ZipFile(zipFiles.get(namespacedKey));
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not load zip file: " + namespacedKey);
        }
    }

    public Map<NamespacedKey, PackFont> fonts() {
        return fonts;
    }

    public CustomResourcePack font(NamespacedKey key, PackFont font) {
        this.fonts.put(key, font);
        return this;
    }

    public Map<NamespacedKey, BufferedImage> textures() {
        return this.textures;
    }

    public CustomResourcePack texture(NamespacedKey key, BufferedImage texture) {
        this.textures.put(key, texture);
        return this;
    }

    public Map<NamespacedKey, File> zipFiles() {
        return this.zipFiles;
    }

    public CustomResourcePack zipFile(NamespacedKey key, File file) {
        this.zipFiles.put(key, file);
        return this;
    }
}
