package com.ehhthan.scholarlee.pack.build;

import com.ehhthan.scholarlee.api.NamespacedKey;
import com.ehhthan.scholarlee.pack.ResourcePack;
import com.ehhthan.scholarlee.pack.assets.font.PackFont;
import com.ehhthan.scholarlee.pack.file.InternalLocation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class BuiltPack {
    private final ResourcePack pack;

    private final Map<NamespacedKey, PackFont> fonts;

    public BuiltPack(ResourcePack pack) {
        this.pack = pack;
        this.fonts = buildFonts();
    }

    public ResourcePack getPack() {
        return pack;
    }

    public Map<NamespacedKey, PackFont> getFontMap() {
        return fonts;
    }

    public boolean hasFont(String key) {
        return hasFont(NamespacedKey.fromString(key));
    }

    public boolean hasFont(NamespacedKey namespacedKey) {
        return fonts.containsKey(namespacedKey);
    }

    public PackFont getFont(String key) {
        return getFont(NamespacedKey.fromString(key));
    }

    public PackFont getFont(NamespacedKey namespacedKey) {
        return fonts.get(namespacedKey);
    }

    private Map<NamespacedKey, PackFont> buildFonts() {
        Map<NamespacedKey, PackFont> fonts = new HashMap<>();

        // default fonts initialized before custom, allows for overrides to operate as expected
        if (pack.getOptions().isUsingProvided()) {
            fonts.putAll(readFonts(pack.getProvided(), NamespacedKey.MINECRAFT));
        }

        for (String namespace : pack.getNamespaces()) {
            fonts.putAll(readFonts(pack.getAssetsDirectory(), namespace));
        }

        return fonts;
    }

    private Map<NamespacedKey, PackFont> readFonts(File directory, String namespace) {
        Map<NamespacedKey, PackFont> fonts = new HashMap<>();

        File fontDirectory = new File(directory, String.format("%s/font", namespace));
        if (fontDirectory.exists()) {
            try {
                Files.walk(fontDirectory.toPath()).forEach(path -> {
                    File file = path.toFile();
                    if (file.isFile() && path.toString().endsWith(InternalLocation.FONT.getExtension())) {
                        NamespacedKey namespacedKey = pack.getNamespacedKey(file);
                        fonts.put(namespacedKey, new PackFont(pack, namespacedKey));
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return fonts;
    }
}
