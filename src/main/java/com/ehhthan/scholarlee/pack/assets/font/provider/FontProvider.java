package com.ehhthan.scholarlee.pack.assets.font.provider;

import com.ehhthan.scholarlee.pack.ResourcePack;
import com.ehhthan.scholarlee.pack.assets.font.character.SizedCharacter;
import com.ehhthan.scholarlee.pack.build.BuiltPack;
import com.google.gson.JsonObject;

import java.util.Locale;
import java.util.Map;

public interface FontProvider {
    Map<Integer, SizedCharacter> getCharacters();

    Type getType();

    static FontProvider get(ResourcePack pack, JsonObject json) {
        if (!json.has("type")) {
            throw new IllegalArgumentException("No type specified.");
        }

        return switch (Type.valueOf(json.get("type").getAsString().toUpperCase(Locale.ROOT))) {
            case SPACE ->  new SpaceFontProvider(json);
            case BITMAP -> new BitmapFontProvider(pack, json);
            case REFERENCE -> new ReferenceFontProvider(pack, json);
            case UNIHEX -> new UnihexFontProvider(pack, json);
            default -> null;
        };
    }

    enum Type {
        BITMAP,
        SPACE,
        UNIHEX,
        REFERENCE
    }
}
