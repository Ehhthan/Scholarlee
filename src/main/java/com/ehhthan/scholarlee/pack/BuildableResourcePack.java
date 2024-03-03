package com.ehhthan.scholarlee.pack;

import com.ehhthan.scholarlee.pack.assets.font.PackFont;
import com.ehhthan.scholarlee.pack.key.NamespacedKey;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Map;

public interface BuildableResourcePack extends ResourcePack {
    Map<NamespacedKey, PackFont> fonts();

    Map<NamespacedKey, BufferedImage> textures();

    Map<NamespacedKey, File> zipFiles();

    default FileResourcePack build(File destination) {
        File assetsDir = new File(destination, "assets");

        assetsDir.mkdirs();

        // create fonts
        for (Map.Entry<NamespacedKey, PackFont> entry : fonts().entrySet()) {
            File file = new File(assetsDir, entry.getKey().asPath(AssetType.FONT));

            File dir = file.getParentFile();
            if (dir != null)
                dir.mkdirs();

            try (Writer writer = new FileWriter(file, StandardCharsets.UTF_8)) {
                GSON.toJson(entry.getValue(), writer);
            } catch (IOException e) {
                throw new IllegalArgumentException(String.format("Could not write font file '%s'.", file.getPath()));
            }
        }

        // create textures
        for (Map.Entry<NamespacedKey, BufferedImage> entry : textures().entrySet()) {
            File file = new File(assetsDir, entry.getKey().asPath(AssetType.TEXTURE));

            File dir = file.getParentFile();
            if (dir != null)
                dir.mkdirs();

            try {
                ImageIO.write(entry.getValue(), "png", file);
            } catch (IOException e) {
                throw new IllegalArgumentException(String.format("Could not write texture file '%s'.", file.getPath()));
            }
        }

        // create zip files
        for (Map.Entry<NamespacedKey, File> entry : zipFiles().entrySet()) {
            File file = new File(assetsDir, entry.getKey().asPath(AssetType.ZIP_FILE));

            File dir = file.getParentFile();
            if (dir != null)
                dir.mkdirs();

            try {
                Files.copy(entry.getValue().toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new IllegalArgumentException(String.format("Could not write zip file '%s'.", file.getPath()));
            }
        }

        return new FileResourcePack(destination);
    }
}
