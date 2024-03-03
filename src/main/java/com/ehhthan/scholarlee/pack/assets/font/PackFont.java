package com.ehhthan.scholarlee.pack.assets.font;

import com.ehhthan.scholarlee.pack.ResourcePack;
import com.ehhthan.scholarlee.pack.assets.font.character.SizedCharacter;
import com.ehhthan.scholarlee.pack.assets.font.provider.FontProvider;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PackFont {
    private final List<FontProvider> providers;

    private PackFont(Builder builder) {
        this.providers = builder.providers;
    }

    public static PackFont.Builder builder() {
        return new PackFont.Builder();
    }

    public List<FontProvider> getProviders() {
        return providers;
    }

    public Map<Integer, SizedCharacter> buildSizedCharacters(ResourcePack pack) {
        Map<Integer, SizedCharacter> characters = new HashMap<>();

        for (FontProvider provider : providers) {
            characters.putAll(provider.buildSizedCharacters(pack));
        }

        return characters;
    }

    public static class Builder {
        private List<FontProvider> providers = new LinkedList<>();

        private Builder() {}

        public Builder providers(List<FontProvider> providers) {
            this.providers = providers;
            return this;
        }

        public List<FontProvider> providers() {
            return providers;
        }

        public Builder fontProvider(FontProvider providers) {
            this.providers.add(providers);
            return this;
        }

        public PackFont build() {
            return new PackFont(this);
        }
    }
}
