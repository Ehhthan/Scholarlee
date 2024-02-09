package com.ehhthan.scholarlee.pack.assets.font.provider;

import com.ehhthan.scholarlee.pack.assets.font.character.SizedCharacter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class SpaceFontProvider implements FontProvider {
    private final Map<Integer, SizedCharacter> characters = new HashMap<>();

    public SpaceFontProvider(JsonObject json) {
        JsonObject advances = json.getAsJsonObject("advances");
        for (Map.Entry<String, JsonElement> entry : advances.entrySet()) {
            int width = entry.getValue().getAsInt();
            for (int codepoint : entry.getKey().codePoints().toArray()) {
                characters.put(codepoint, new SizedCharacter(codepoint, width, 0));
            }
        }
    }

    public Type getType() {
        return Type.SPACE;
    }

    @Override
    public Map<Integer, SizedCharacter> getCharacters() {
        return characters;
    }
}
