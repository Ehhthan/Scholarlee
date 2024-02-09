package com.ehhthan.scholarlee.pack.assets.font.provider;

import com.ehhthan.scholarlee.api.NamespacedKey;
import com.ehhthan.scholarlee.api.image.TrimmedImage;
import com.ehhthan.scholarlee.pack.ResourcePack;
import com.ehhthan.scholarlee.pack.assets.font.character.AscentedCharacter;
import com.ehhthan.scholarlee.pack.assets.font.character.SizedCharacter;
import com.ehhthan.scholarlee.pack.assets.texture.PackTexture;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class BitmapFontProvider implements FontProvider {
    private final String file;
    private final int ascent;
    private final int height;

    private final Map<Integer, SizedCharacter> characters = new HashMap<>();

    public BitmapFontProvider(ResourcePack pack, JsonObject json) {
        this.file = json.get("file").getAsString();
        this.ascent = json.get("ascent").getAsInt();
        this.height = json.get("height").getAsInt();

        JsonArray chars = json.getAsJsonArray("chars");

        int[][] codepoints = new int[chars.size()][];
        for (int i = 0; i < chars.size(); i++)
            codepoints[i] = chars.get(i).getAsString().codePoints().toArray();

        BufferedImage base = pack.getTextureFile(NamespacedKey.fromString(file));
        int rows = codepoints.length, columns = codepoints[0].length;
        TrimmedImage[][] tiles = PackTexture.tiles(base, rows, columns);
        int tileHeight = base.getHeight() / rows;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                int codepoint = codepoints[i][j];
                TrimmedImage image = tiles[i][j];
                if (codepoint != 0 && image != null) {
                    // scale in double format.
                    double coefficient = ((double) height) / tileHeight;
                    int width = (int) Math.round((image.getLeftIndent() + image.getWidth()) * coefficient);
                    int height = (int) Math.round(image.getHeight() * coefficient);
                    characters.put(codepoint, new AscentedCharacter(codepoint, width, height, ascent));
                }
            }
        }
    }

    public Type getType() {
        return Type.BITMAP;
    }

    public String getFile() {
        return file;
    }

    public int getAscent() {
        return ascent;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public Map<Integer, SizedCharacter> getCharacters() {
        return characters;
    }
}
