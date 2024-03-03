package com.ehhthan.scholarlee.pack.assets.font.provider;

import com.ehhthan.scholarlee.pack.ResourcePack;
import com.ehhthan.scholarlee.pack.assets.font.character.SizedCharacter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class SpaceFontProvider implements FontProvider {
    private final Map<String, Integer> advances;

    private SpaceFontProvider(SpaceFontProvider.Builder builder) {
        this.advances = builder.advances;
    }

    public static SpaceFontProvider.Builder builder() {
        return new SpaceFontProvider.Builder();
    }

    public static SpaceFontProvider.Builder json(JsonObject json) {
        return new SpaceFontProvider.Builder(json);
    }

    @Override
    public Type getType() {
        return Type.SPACE;
    }

    public Map<String, Integer> getAdvances() {
        return advances;
    }

    @Override
    public Map<Integer, SizedCharacter> buildSizedCharacters(ResourcePack pack) {
        Map<Integer, SizedCharacter> characters = new HashMap<>();

        for (Map.Entry<String, Integer> entry : advances.entrySet()) {
            int width = entry.getValue();

            for (int codepoint : entry.getKey().codePoints().toArray()) {
                characters.put(codepoint, new SizedCharacter(codepoint, width, 0));
            }
        }

        return characters;
    }

    @Override
    public JsonElement toJson() {
        JsonObject json = new JsonObject();

        json.addProperty("type", getType().toString());

        JsonObject advances = new JsonObject();
        for (Map.Entry<String, Integer> entry : this.advances.entrySet()) {
            advances.addProperty(entry.getKey(), entry.getValue());
        }

        json.add("advances", advances);

        return json;
    }

    @Override
    public Builder toBuilder() {
        return builder().advances(advances);
    }

    public static class Builder implements FontProvider.Builder {
        private Map<String, Integer> advances = new HashMap<>();

        private Builder() {}

        private Builder(JsonObject json) {
            JsonObject jsonAdvances = json.getAsJsonObject("advances");
            for (Map.Entry<String, JsonElement> entry : jsonAdvances.entrySet()) {
                this.advances.put(entry.getKey(), entry.getValue().getAsInt());
            }
        }

        public Builder advances(Map<String, Integer> advances) {
            this.advances = advances;

            return this;
        }

        public Map<String, Integer> advances() {
            return advances;
        }

        public Builder advance(String codepoints, int width) {
            this.advances.put(codepoints, width);

            return this;
        }

        public SpaceFontProvider build() {
            return new SpaceFontProvider(this);
        }
    }
}
