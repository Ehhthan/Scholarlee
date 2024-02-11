package com.ehhthan.scholarlee.pack;

import com.ehhthan.scholarlee.api.NamespacedKey;
import com.ehhthan.scholarlee.pack.build.PackOptions;
import com.ehhthan.scholarlee.pack.file.AssetLocation;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class FileResourcePack implements ResourcePack {
    private final PackOptions options;
    private final Gson gson;
    private final File packDirectory;
    private final File assetsDirectory;
    private final File defaultAssetsDirectory;

    private final Set<String> namespaces = new HashSet<>();

    public FileResourcePack(File pack, PackOptions options) {
        this.options = options;
        this.gson = new Gson();
        this.packDirectory = pack;
        if (!packDirectory.exists() || !packDirectory.isDirectory()) {
            throw new IllegalArgumentException("Pack directory does not exist.");
        }

        this.assetsDirectory = new File(pack, "assets");
        if (!assetsDirectory.exists() || !assetsDirectory.isDirectory()) {
            throw new IllegalArgumentException("No assets directory exists.");
        }

        if (options.isUsingDefaultAssets()) {
            URL url = this.getClass().getClassLoader().getResource(String.format("mc/%s/assets", options.getDefaultAssetVersion()));

            File resource;
            try {
                resource = new File(url.toURI());
            } catch (URISyntaxException e) {
                resource = new File(url.getPath());
            }

            this.defaultAssetsDirectory = resource;
        } else {
            this.defaultAssetsDirectory = null;
        }

        File[] namespaceFiles = assetsDirectory.listFiles();
        if (namespaceFiles != null)
            for (File file : namespaceFiles) {
                if (file.isDirectory())
                    namespaces.add(file.getName());
            }
    }

    @Override
    public PackOptions getOptions() {
        return options;
    }

    @Override
    public Set<String> getNamespaces() {
        return namespaces;
    }

    public File getPackDirectory() {
        return packDirectory;
    }

    @Override
    public File getAssetsDirectory() {
        return assetsDirectory;
    }

    @Override
    public File getDefaultAssetsDirectory() {
        return defaultAssetsDirectory;
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
    public File getFile(NamespacedKey key, AssetLocation type) {
        if (!namespaces.contains(key.getNamespace())) {
            throw new IllegalArgumentException(String.format("Namespace '%s' does not exist.", key.getNamespace()));
        }

        String path = String.format("%s/%s/%s", key.getNamespace(), type.getPath(), type.appendExtension(key.getKey()));
        File file = new File(assetsDirectory, path);

        if (!file.exists() && key.getNamespace().equals(NamespacedKey.MINECRAFT) && options.isUsingDefaultAssets())
            file = new File(defaultAssetsDirectory, path);

        return file;
    }

    @Override
    public NamespacedKey getNamespacedKey(File file) {
        String[] split = file.getPath().split(Pattern.quote("assets"), 2);

        if (split.length != 2) {
            throw new IllegalArgumentException(String.format("Could not generate NamespacedKey for file '%s'", file.getPath()));
        }

        Path relativePath = Path.of(split[1]);

        String namespace = relativePath.getName(0).toString();
        String key = relativePath.subpath(2, relativePath.getNameCount()).toString();

        return new NamespacedKey(namespace, key);
    }
}
