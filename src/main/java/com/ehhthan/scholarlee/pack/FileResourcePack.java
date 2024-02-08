package com.ehhthan.scholarlee.pack;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;

public class FileResourcePack implements ResourcePack {
    private final Gson gson;
    private final File packDirectory;
    private final File assetsDirectory;

    private final Set<String> namespaces = new HashSet<>();

    public FileResourcePack(File pack) {
        this.gson = new Gson();
        this.packDirectory = pack;
        if (!packDirectory.exists() || !packDirectory.isDirectory()) {
            throw new IllegalArgumentException("Pack directory does not exist.");
        }

        this.assetsDirectory = new File(pack, "assets");
        if (!assetsDirectory.exists() || !assetsDirectory.isDirectory()) {
            throw new IllegalArgumentException("No assets directory exists.");
        }

        File[] namespaceFiles = assetsDirectory.listFiles();
        if (namespaceFiles != null)
            for (File file : namespaceFiles) {
                if (file.isDirectory())
                    namespaces.add(file.getName());
            }
    }

    public Set<String> getNamespaces() {
        return namespaces;
    }

    @Override
    public BufferedImage getImageFile(File file) {

        BufferedImage image;
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            throw new IllegalArgumentException("Image file cannot be read: " + file.getPath());
        }
        return image;
    }

    @Override
    public JsonObject getJsonFile(File file) {
        BufferedReader reader;
        try {
            reader = Files.newBufferedReader(file.toPath());
        } catch (IOException e) {
            throw new IllegalArgumentException("Json file cannot be read: " + file.toPath());
        }

        return gson.fromJson(reader, JsonObject.class);
    }

    @Override
    public File getFile(String namespace, String type, String path) {
        if (!namespaces.contains(namespace)) {
            throw new IllegalArgumentException(String.format("Namespace '%s' does not exist.", namespace));
        }

        return new File(assetsDirectory, String.format("%s/%s/%s", namespace, type, path));
    }
}
