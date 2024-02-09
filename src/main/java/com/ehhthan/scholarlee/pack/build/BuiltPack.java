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
    private final Map<NamespacedKey, PackFont> fonts;

    public BuiltPack(BuiltPackOptions options) {
        this.fonts = buildFonts(options.getPack());
    }

    public Map<NamespacedKey, PackFont> getFonts() {
        return fonts;
    }

    private Map<NamespacedKey, PackFont> buildFonts(ResourcePack pack) {
        Map<NamespacedKey, PackFont> fonts = new HashMap<>();

        for (String namespace : pack.getNamespaces()) {
            File fontDirectory = new File(pack.getAssetsDirectory(), String.format("%s/font", namespace));
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
        }

        return fonts;
    }
}
