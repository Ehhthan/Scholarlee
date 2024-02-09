package com.ehhthan.scholarlee.pack.assets.font;

import com.ehhthan.scholarlee.ScholarleeAPI;
import com.ehhthan.scholarlee.api.NamespacedKey;
import com.ehhthan.scholarlee.pack.ResourcePack;
import com.ehhthan.scholarlee.pack.assets.font.character.SizedCharacter;
import com.ehhthan.scholarlee.pack.assets.font.provider.FontProvider;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PackFont {
    private final NamespacedKey namespacedKey;

    private final List<FontProvider> providers = new ArrayList<>();
    private final Map<Integer, SizedCharacter> characters = new HashMap<>();

    public PackFont(ResourcePack pack, NamespacedKey namespacedKey) {
        this.namespacedKey = namespacedKey;
        for (JsonElement jsonProvider : pack.getFontFile(namespacedKey).get("providers").getAsJsonArray()) {
            try {
                FontProvider fontProvider = FontProvider.get(pack, jsonProvider.getAsJsonObject());
                if (fontProvider != null) {
                    providers.add(fontProvider);
                    characters.putAll(fontProvider.getCharacters());
                }
            } catch (IllegalArgumentException e) {
                ScholarleeAPI.get().getLogger().log(System.Logger.Level.ERROR, String.format("Could not load a font provider for font '%s': %s", namespacedKey, e.getMessage()));
            }
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
