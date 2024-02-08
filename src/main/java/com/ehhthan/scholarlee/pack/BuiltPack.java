package com.ehhthan.scholarlee.pack;

import com.ehhthan.scholarlee.api.NamespacedKey;
import com.ehhthan.scholarlee.pack.font.PackFont;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class BuiltPack {

    public BuiltPack(ResourcePack pack) {

    }

    private Map<NamespacedKey, PackFont> buildFonts(ResourcePack pack) {
        Map<NamespacedKey, PackFont> fonts = new HashMap<>();



        for (String namespace : pack.getNamespaces()) {
            File fontDirectory = new File(pack.getAssetsDirectory(), String.format("%s/font", namespace));
            try {
                Files.walk(fontDirectory.toPath()).forEach(path -> {
                    File file = path.toFile();
                    if (file.isFile()) {
                        pack.getJsonFile(file)
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return fonts;
    }
}
