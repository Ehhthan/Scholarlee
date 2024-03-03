package com.ehhthan.scholarlee.pack.assets.font.provider;

import com.ehhthan.scholarlee.pack.key.NamespacedKey;
import com.ehhthan.scholarlee.api.texture.TrimData;
import com.ehhthan.scholarlee.pack.ResourcePack;
import com.ehhthan.scholarlee.pack.assets.font.character.SizedCharacter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class UnihexFontProvider implements FontProvider {
    private final String hexFile;
    private final List<SizeOverride> sizeOverrides;

    private UnihexFontProvider(Builder builder) {
        this.hexFile = builder.hexFile;
        this.sizeOverrides = builder.sizeOverrides;
    }

    public static UnihexFontProvider.Builder builder(String hexFile) {
        return new UnihexFontProvider.Builder(hexFile);
    }

    public static UnihexFontProvider.Builder json(JsonObject json) {
        return new UnihexFontProvider.Builder(json);
    }

    @Override
    public Type getType() {
        return Type.UNIHEX;
    }

    public String getHexFile() {
        return hexFile;
    }

    public List<SizeOverride> getSizeOverrides() {
        return sizeOverrides;
    }

    @Override
    public Map<Integer, SizedCharacter> buildSizedCharacters(ResourcePack pack) {
        Map<Integer, SizedCharacter> characters = new HashMap<>();

        try {
            ZipFile zipFile = pack.getZipFile(NamespacedKey.fromString(hexFile));

            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry.getName().endsWith(".hex")) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(zipFile.getInputStream(entry), StandardCharsets.UTF_8));

                    String line;
                    while ((line = br.readLine()) != null) {
                        // Process each line here
                        String[] split = line.split(":");
                        if (split.length == 2) {
                            int index = 0;
                            int indexIncrease = (split[1].length() / 16);
                            boolean[][] bools = new boolean[16][];
                            while (index < split[1].length()) {
                                byte[] bytes = HexFormat.of().parseHex(split[1].substring(index, Math.min(index + indexIncrease, split[1].length())));
                                boolean[] bits = new boolean[bytes.length * 8];

                                for (int i = 0; i < bytes.length * 8; i++) {
                                    if ((bytes[i / 8] & (1 << (7 - (i % 8)))) > 0)
                                        bits[i] = true;
                                }

                                bools[index / indexIncrease] = bits;
                                index += indexIncrease;
                            }

                            TrimData data = TrimData.get(bools);
                            int codepoint = Integer.parseInt(split[0], 16);

                            characters.put(codepoint, new SizedCharacter(codepoint, data.getLeftIndent() + data.getWidth(), data.getHeight()));

                        }
                    }
                    br.close();
                }
            }
            zipFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return characters;
    }

    @Override
    public JsonElement toJson() {
        JsonObject json = new JsonObject();

        json.addProperty("type", getType().toString());
        json.addProperty("hex_file", hexFile);

        JsonArray overrides = new JsonArray();
        for (SizeOverride override : sizeOverrides) {
            JsonObject overrideObject = new JsonObject();

            overrideObject.addProperty("from", Character.toString(override.from));
            overrideObject.addProperty("to", Character.toString(override.to));

            overrideObject.addProperty("left", override.left);
            overrideObject.addProperty("right", override.right);

            overrides.add(overrideObject);
        }

        json.add("size_overrides", overrides);

        return json;
    }

    @Override
    public Builder toBuilder() {
        return builder(hexFile).sizeOverrides(sizeOverrides);
    }

    static class SizeOverride {
        int from, to;

        int left, right;

        private SizeOverride(JsonObject json) {
            this.from = json.get("from").getAsString().codePoints().toArray()[0];
            this.to = json.get("to").getAsString().codePoints().toArray()[0];

            this.left = json.get("left").getAsInt();
            this.right = json.get("right").getAsInt();
        }

        public boolean contains(int codepoint) {
            return codepoint >= from && codepoint <= to;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SizeOverride override = (SizeOverride) o;
            return from == override.from && to == override.to && left == override.left && right == override.right;
        }

        @Override
        public int hashCode() {
            return Objects.hash(from, to, left, right);
        }
    }

    public static class Builder implements FontProvider.Builder {
        private final String hexFile;

        private List<SizeOverride> sizeOverrides = new ArrayList<>();

        private Builder(String hexFile) {
            this.hexFile = hexFile;
        }

        private Builder(JsonObject json) {
            if (!json.has("hex_file"))
                throw new IllegalArgumentException("No 'hex_file' is defined.");

            this.hexFile = json.get("hex_file").getAsString();

            for (JsonElement overrideJson : json.get("size_overrides").getAsJsonArray()) {
                sizeOverrides.add(new SizeOverride(overrideJson.getAsJsonObject()));
            }
        }

        public String hexFile() {
            return hexFile;
        }

        public Builder sizeOverrides(List<SizeOverride> sizeOverrides) {
            this.sizeOverrides = sizeOverrides;

            return this;
        }

        public List<SizeOverride> sizeOverrides() {
            return sizeOverrides;
        }

        public Builder sizeOverride(SizeOverride sizeOverride) {
            this.sizeOverrides.add(sizeOverride);

            return this;
        }

        public UnihexFontProvider build() {
            return new UnihexFontProvider(this);
        }
    }

}
