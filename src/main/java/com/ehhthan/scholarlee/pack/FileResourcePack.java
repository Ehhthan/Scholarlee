package com.ehhthan.scholarlee.pack;

import com.ehhthan.scholarlee.pack.assets.font.PackFont;
import com.ehhthan.scholarlee.pack.key.NamespacedKey;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipFile;

public class FileResourcePack implements ResourcePack {
    private final File packFile;

    public FileResourcePack(File pack) {
        if (!pack.exists() || !pack.isDirectory()) {
            throw new IllegalArgumentException("Pack directory does not exist.");
        }

        this.packFile = new File(pack, "assets");

        if (!packFile.exists() || !packFile.isDirectory()) {
            throw new IllegalArgumentException("Assets directory does not exist.");
        }
    }

    @Override
    public BufferedImage getTexture(NamespacedKey namespacedKey) {
        File file = getFile(namespacedKey, AssetType.TEXTURE);

        try {
            return ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Image file cannot be read: " + file.getPath());
        }
    }

    @Override
    public PackFont getFont(NamespacedKey namespacedKey) {
        Path path = getFile(namespacedKey, AssetType.FONT).toPath();

        BufferedReader reader;
        try {
            reader = Files.newBufferedReader(path);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Json file cannot be read: " + path);
        }

        return GSON.fromJson(reader, PackFont.class);
    }

    @Override
    public ZipFile getZipFile(NamespacedKey namespacedKey) {
        File file = getFile(namespacedKey, AssetType.ZIP_FILE);
        try {
            return new ZipFile(file);
        } catch (IOException e) {
            throw new IllegalArgumentException("Zip file cannot be read: " + file.getPath());
        }
    }

    private File getFile(NamespacedKey key, AssetType type) {
        return new File(packFile, key.asPath(type));
    }
}
