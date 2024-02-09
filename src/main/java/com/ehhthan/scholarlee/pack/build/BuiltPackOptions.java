package com.ehhthan.scholarlee.pack.build;

import com.ehhthan.scholarlee.pack.FileResourcePack;
import com.ehhthan.scholarlee.pack.ResourcePack;

import java.io.File;

public class BuiltPackOptions {
    private final ResourcePack pack;
    private final boolean useDefaultAssets;

    private BuiltPackOptions(Builder builder) {
        this.pack = builder.pack;
        this.useDefaultAssets = builder.useDefaultAssets;
    }

    public static Builder builder(ResourcePack pack) {
        return new Builder(pack);
    }

    public static Builder file(File file) {
        return new Builder(file);
    }

    public ResourcePack getPack() {
        return pack;
    }

    public boolean isUsingDefaultAssets() {
        return useDefaultAssets;
    }

    public static class Builder {
        private final ResourcePack pack;
        private boolean useDefaultAssets = true;

        Builder(ResourcePack pack) {
            this.pack = pack;
        }

        Builder(File file) {
            this.pack = new FileResourcePack(file);
        }

        public ResourcePack getPack() {
            return pack;
        }

        public boolean isUsingDefaultAssets() {
            return useDefaultAssets;
        }

        public void useDefaultAssets(boolean useDefaultAssets) {
            this.useDefaultAssets = useDefaultAssets;
        }

        public BuiltPackOptions build() {
            return new BuiltPackOptions(this);
        }
    }
}
