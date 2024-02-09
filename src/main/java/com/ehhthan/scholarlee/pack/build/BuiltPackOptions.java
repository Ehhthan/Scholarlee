package com.ehhthan.scholarlee.pack.build;

import com.ehhthan.scholarlee.pack.FileResourcePack;
import com.ehhthan.scholarlee.pack.ResourcePack;

import java.io.File;

public class BuiltPackOptions {
    private final ResourcePack pack;
    private final boolean useDefaultAssets;

    private BuiltPackOptions(Builder builder) {
        this.useDefaultAssets = builder.useDefaultAssets;
        this.pack = new FileResourcePack(builder.packFile, (useDefaultAssets) ? builder.assetVersion : null);
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
        private final File packFile;
        private boolean useDefaultAssets = true;
        private String assetVersion = "1_20_4";

        Builder(File file) {
            this.packFile = file;
        }

        public File packFile() {
            return packFile;
        }

        public boolean useDefaultAssets() {
            return useDefaultAssets;
        }

        public void useDefaultAssets(boolean useDefaultAssets) {
            this.useDefaultAssets = useDefaultAssets;
        }

        public String assetVersion() {
            return assetVersion;
        }

        public void assetVersion(String assetVersion) {
            this.assetVersion = assetVersion;
        }

        public BuiltPackOptions build() {
            return new BuiltPackOptions(this);
        }
    }
}
