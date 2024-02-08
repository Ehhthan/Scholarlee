package com.ehhthan.scholarlee.pack.font.provider;

import com.ehhthan.scholarlee.api.NamespacedKey;
import com.ehhthan.scholarlee.pack.ResourcePack;
import com.ehhthan.scholarlee.pack.font.character.SizedCharacter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class ReferenceFontProvider implements FontProvider {
    private final Map<Integer, SizedCharacter> characters = new HashMap<>();
    private final String id;

    public ReferenceFontProvider(ResourcePack pack, JsonObject json) {
        this.id = json.get("id").getAsString();
        for (JsonElement provider : pack.getFontFile(NamespacedKey.fromString(id)).get("providers").getAsJsonArray()) {
            characters.putAll(FontProvider.get(pack, provider.getAsJsonObject()).getCharacters());
        }
    }

    public String getId() {
        return id;
    }

    @Override
    public Map<Integer, SizedCharacter> getCharacters() {
        return characters;
    }

    @Override
    public Type getType() {
        return Type.REFERENCE;
    }
}
