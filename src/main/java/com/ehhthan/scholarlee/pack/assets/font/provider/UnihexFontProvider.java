package com.ehhthan.scholarlee.pack.assets.font.provider;

import com.ehhthan.scholarlee.api.NamespacedKey;
import com.ehhthan.scholarlee.api.unihex.TrimData;
import com.ehhthan.scholarlee.pack.ResourcePack;
import com.ehhthan.scholarlee.pack.assets.font.character.SizedCharacter;
import com.ehhthan.scholarlee.pack.file.InternalLocation;
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
    private final List<SizeOverride> sizeOverrides = new ArrayList<>();

    private final Map<Integer, SizedCharacter> characters = new HashMap<>();

    public UnihexFontProvider(ResourcePack pack, JsonObject json) {
        this.hexFile = json.get("hex_file").getAsString();

        for (JsonElement overrideJson : json.get("size_overrides").getAsJsonArray()) {
            sizeOverrides.add(new SizeOverride(overrideJson.getAsJsonObject()));
        }

        try {
            ZipFile zipFile = new ZipFile(pack.getFile(NamespacedKey.fromString(hexFile), InternalLocation.FONT_HEX_FILE));
            Enumeration<? extends ZipEntry> entries = zipFile.entries();

            while(entries.hasMoreElements()){
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

                                bools[index/indexIncrease] = bits;
                                index += indexIncrease;
                            }

                            TrimData data = TrimData.get(bools);
                            int codepoint = Integer.parseInt(split[0], 16);

                            characters.put(codepoint, new SizedCharacter(codepoint, data.getLeftIndent() + data.getWidth(), data.getHeight()));
                        }
                    }
                    zipFile.close();
                    br.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
    public Map<Integer, SizedCharacter> getCharacters() {
        return characters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnihexFontProvider provider = (UnihexFontProvider) o;
        return hexFile.equals(provider.hexFile) && sizeOverrides.equals(provider.sizeOverrides);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hexFile, sizeOverrides);
    }

    static class SizeOverride {
        int fromCodepoint, toCodepoint;

        int left, right;

        public SizeOverride(JsonObject json) {
            this.fromCodepoint = json.get("from").getAsString().codePoints().toArray()[0];
            this.toCodepoint = json.get("to").getAsString().codePoints().toArray()[0];

            this.left = json.get("left").getAsInt();
            this.right = json.get("right").getAsInt();
        }

        public boolean contains(int codepoint) {
            return codepoint >= fromCodepoint && codepoint <= toCodepoint;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SizeOverride override = (SizeOverride) o;
            return fromCodepoint == override.fromCodepoint && toCodepoint == override.toCodepoint && left == override.left && right == override.right;
        }

        @Override
        public int hashCode() {
            return Objects.hash(fromCodepoint, toCodepoint, left, right);
        }
    }
}
