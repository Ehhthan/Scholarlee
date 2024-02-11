package com.ehhthan.scholarlee.pack.build;

import com.ehhthan.scholarlee.pack.ResourcePack;

import java.io.File;

public class PackOptions {
    private final boolean useDefaultAssets;
    private final String defaultAssetVersion;

    private PackOptions(Builder builder) {
        this.useDefaultAssets = builder.useDefaultAssets;
        this.defaultAssetVersion = builder.assetVersion;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getDefaultAssetVersion() {
        return defaultAssetVersion;
    }

    public boolean isUsingDefaultAssets() {
        return useDefaultAssets;
    }

    public static class Builder {
        private boolean useDefaultAssets = true;
        private String assetVersion = "1_20_4";

        Builder() {}

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

        public PackOptions build() {
            return new PackOptions(this);
        }
    }
}
