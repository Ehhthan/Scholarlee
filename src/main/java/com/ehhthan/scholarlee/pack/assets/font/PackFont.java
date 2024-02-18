package com.ehhthan.scholarlee.pack.assets.font;

import com.ehhthan.scholarlee.api.NamespacedKey;
import com.ehhthan.scholarlee.pack.ResourcePack;
import com.ehhthan.scholarlee.pack.assets.font.character.SizedCharacter;
import com.ehhthan.scholarlee.pack.assets.font.provider.FontProvider;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class PackFont {
    private final NamespacedKey namespacedKey;

    private final List<FontProvider> providers = new ArrayList<>();
    private final Map<Integer, SizedCharacter> characters = new HashMap<>();

    public PackFont(ResourcePack pack, NamespacedKey namespacedKey) {
        this.namespacedKey = namespacedKey;
        JsonObject file = pack.getFontFile(namespacedKey);
        if (file.has("providers")) {
            for (JsonElement jsonProvider : file.get("providers").getAsJsonArray()) {
                try {
                    FontProvider fontProvider = FontProvider.get(pack, jsonProvider.getAsJsonObject());
                    if (fontProvider != null) {
                        providers.add(fontProvider);
                        characters.putAll(fontProvider.getCharacters());
                    }
                } catch (IllegalArgumentException e) {
                    pack.getAPI().getLogger().log(Level.WARNING, String.format("Could not load a font provider for font '%s': %s", namespacedKey, e.getMessage()));
                }
            }
        } else {
            throw new IllegalArgumentException("No providers defined.");
        }
    }

    public NamespacedKey getNamespacedKey() {
        return namespacedKey;
    }

    public List<FontProvider> getProviders() {
        return providers;
    }

    public Map<Integer, SizedCharacter> getCharacters() {
        return characters;
    }
}
