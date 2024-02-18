package com.ehhthan.scholarlee.pack.assets.font.provider;

import com.ehhthan.scholarlee.api.NamespacedKey;
import com.ehhthan.scholarlee.pack.ResourcePack;
import com.ehhthan.scholarlee.pack.assets.font.character.SizedCharacter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class ReferenceFontProvider implements FontProvider {
    private final String id;

    private final Map<Integer, SizedCharacter> characters = new HashMap<>();

    public ReferenceFontProvider(ResourcePack pack, JsonObject json) {
        this.id = json.get("id").getAsString();
        for (JsonElement jsonProvider : pack.getFontFile(NamespacedKey.fromString(id)).get("providers").getAsJsonArray()) {
            FontProvider fontProvider = FontProvider.get(pack, jsonProvider.getAsJsonObject());

            if (fontProvider instanceof ReferenceFontProvider reference && reference.getId().equals(id)) {
                throw new IllegalStateException("ReferenceFontProvider cannot reference itself.");
            }

            if (fontProvider != null && fontProvider.getCharacters() != null)
                characters.putAll(fontProvider.getCharacters());
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
