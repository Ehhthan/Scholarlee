package com.ehhthan.scholarlee.pack.assets.font.provider;

import com.ehhthan.scholarlee.pack.key.NamespacedKey;
import com.ehhthan.scholarlee.pack.ResourcePack;
import com.ehhthan.scholarlee.pack.assets.font.character.SizedCharacter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class ReferenceFontProvider implements FontProvider {
    private final String id;

    public ReferenceFontProvider(Builder builder) {
        this.id = builder.id;
    }

    public static Builder builder(String id) {
        return new Builder(id);
    }

    public static Builder json(JsonObject json) {
        return new Builder(json);
    }

    @Override
    public Type getType() {
        return Type.REFERENCE;
    }

    public String getId() {
        return id;
    }

    @Override
    public Map<Integer, SizedCharacter> buildSizedCharacters(ResourcePack pack) {
        Map<Integer, SizedCharacter> characters = new HashMap<>();

        for (FontProvider fontProvider : pack.getFont(NamespacedKey.fromString(id)).getProviders()) {

            if (fontProvider instanceof ReferenceFontProvider reference && reference.getId().equals(id)) {
                throw new IllegalStateException("ReferenceFontProvider cannot reference itself.");
            }

            if (fontProvider != null && fontProvider.buildSizedCharacters(pack) != null)
                characters.putAll(fontProvider.buildSizedCharacters(pack));
        }

        return characters;
    }

    @Override
    public JsonElement toJson() {
        JsonObject json = new JsonObject();

        json.addProperty("type", getType().toString());
        json.addProperty("id", id);

        return json;
    }

    @Override
    public Builder toBuilder() {
        return builder(id);
    }

    public static class Builder implements FontProvider.Builder {
        private final String id;

        private Builder(String id) {
            this.id = id;
        }

        private Builder(JsonObject json) {
            if (!json.has("id"))
                throw new IllegalArgumentException("No id is defined.");

            this.id = json.get("id").getAsString();
        }

        public String id() {
            return id;
        }

        public ReferenceFontProvider build() {
            return new ReferenceFontProvider(this);
        }
    }
}
