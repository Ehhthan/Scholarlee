package com.ehhthan.scholarlee.pack.assets.font.provider;

import com.ehhthan.scholarlee.pack.ResourcePack;
import com.ehhthan.scholarlee.pack.assets.font.character.SizedCharacter;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.util.Locale;
import java.util.Map;

public interface FontProvider {
    Type getType();

    Map<Integer, SizedCharacter> buildSizedCharacters(ResourcePack pack);

    JsonElement toJson();

    Builder toBuilder();

    enum Type {
        BITMAP {
            @Override
            BitmapFontProvider json(JsonObject json) {
                return BitmapFontProvider.json(json).build();
            }
        },
        REFERENCE {
            @Override
            ReferenceFontProvider json(JsonObject json) {
                return ReferenceFontProvider.json(json).build();
            }
        },
        SPACE {
            @Override
            SpaceFontProvider json(JsonObject json) {
                return SpaceFontProvider.json(json).build();
            }
        },
        UNIHEX {
            @Override
            UnihexFontProvider json(JsonObject json) {
                return UnihexFontProvider.json(json).build();
            }
        };

        abstract FontProvider json(JsonObject json);

        @Override
        public String toString() {
            return name().toLowerCase(Locale.ROOT);
        }


        public static class Adapter implements JsonDeserializer<FontProvider>, JsonSerializer<FontProvider> {
            public Adapter() {}

            @Override
            public FontProvider deserialize(JsonElement json, java.lang.reflect.Type type, JsonDeserializationContext context) throws JsonParseException {
                JsonObject jsonObject = json.getAsJsonObject();
                return Type.valueOf(jsonObject.get("type").getAsString().toUpperCase(Locale.ROOT)).json(jsonObject);
            }

            @Override
            public JsonElement serialize(FontProvider provider, java.lang.reflect.Type typeOfSrc, JsonSerializationContext context) {
                return provider.toJson();
            }
        }
    }

    interface Builder {
        FontProvider build();
    }
}
