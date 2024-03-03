package com.ehhthan.scholarlee.pack.assets.font.provider;

import com.ehhthan.scholarlee.pack.key.NamespacedKey;
import com.ehhthan.scholarlee.api.texture.TrimData;
import com.ehhthan.scholarlee.pack.ResourcePack;
import com.ehhthan.scholarlee.pack.assets.font.character.AscentedCharacter;
import com.ehhthan.scholarlee.pack.assets.font.character.SizedCharacter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class BitmapFontProvider implements FontProvider {
    private final String file;
    private final int ascent, height;
    private final String[] chars;

    private BitmapFontProvider(BitmapFontProvider.Builder builder) {
        this.file = builder.file;
        this.ascent = builder.ascent;
        this.height = builder.height;
        this.chars = builder.chars;
    }

    public static BitmapFontProvider.Builder builder(String file) {
        return new BitmapFontProvider.Builder(file);
    }

    public static BitmapFontProvider.Builder json(JsonObject json) {
        return new BitmapFontProvider.Builder(json);
    }

    @Override
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

    public String[] getChars() {
        return chars;
    }

    @Override
    public Map<Integer, SizedCharacter> buildSizedCharacters(ResourcePack pack) {
        Map<Integer, SizedCharacter> sizedCharacters = new HashMap<>();

        int[][] codepoints = new int[chars.length][];
        for (int i = 0; i < chars.length; i++)
            codepoints[i] = chars[i].codePoints().toArray();

        BufferedImage base = pack.getTexture(NamespacedKey.fromString(file));
        int rows = codepoints.length, columns = codepoints[0].length;
        TrimData[][] tiles = tiles(base, rows, columns);
        int tileHeight = base.getHeight() / rows;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                int codepoint = codepoints[i][j];
                TrimData image = tiles[i][j];
                if (codepoint != 0 && image != null) {
                    // scale in double format.
                    double coefficient = ((double) height) / tileHeight;
                    int width = (int) Math.round((image.getLeftIndent() + image.getWidth()) * coefficient);
                    int height = (int) Math.round(image.getHeight() * coefficient);
                    sizedCharacters.put(codepoint, new AscentedCharacter(codepoint, width, height, ascent));
                }
            }
        }

        return sizedCharacters;
    }

    @Override
    public JsonElement toJson() {
        JsonObject json = new JsonObject();

        json.addProperty("type", getType().toString());
        json.addProperty("file", file);
        json.addProperty("ascent", ascent);
        json.addProperty("height", height);

        JsonArray chars = new JsonArray();
        for (String charLine : this.chars) {
            chars.add(charLine);
        }

        json.add("chars", chars);

        return json;
    }

    @Override
    public Builder toBuilder() {
        return builder(file).ascent(ascent).height(height).chars(chars);
    }

    private static TrimData[][] tiles(BufferedImage image, int rows, int columns) {
        final TrimData[][] tiles = new TrimData[rows][columns];

        int tileWidth = image.getWidth() / columns;
        int tileHeight = image.getHeight() / rows;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                tiles[i][j] = TrimData.get(image.getSubimage(tileWidth * j, tileHeight * i, tileWidth, tileHeight));
            }
        }

        return tiles;
    }

    public static class Builder implements FontProvider.Builder {
        private final String file;

        private int ascent = 7, height = 8;
        private String[] chars = new String[0];

        private Builder(@NotNull String file) {
            this.file = file;
        }

        private Builder(JsonObject json) {
            if (!json.has("file"))
                throw new IllegalArgumentException("No 'file' path defined.");

            this.file = json.get("file").getAsString();

            if (json.has("ascent"))
                this.ascent = json.get("ascent").getAsInt();

            if (json.has("height"))
                this.height = json.get("height").getAsInt();

            if (json.has("chars")) {
                JsonArray jsonChars = json.getAsJsonArray("chars");

                this.chars = new String[jsonChars.size()];
                for (int i = 0; i < jsonChars.size(); i++) {
                    chars[i] = jsonChars.get(i).getAsString();
                }
            }
        }

        public String file() {
            return file;
        }

        public Builder ascent(int ascent) {
            this.ascent = ascent;

            return this;
        }

        public int ascent() {
            return ascent;
        }

        public Builder height(int height) {
            this.height = height;

            return this;
        }

        public int height() {
            return height;
        }

        public Builder chars(String[] chars) {
            this.chars = chars;

            return this;
        }

        public String[] chars() {
            return chars;
        }

        public Builder chars(String chars) {
            return chars(new String[]{chars});
        }

        public BitmapFontProvider build() {
            return new BitmapFontProvider(this);
        }
    }
}
