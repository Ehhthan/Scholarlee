package com.ehhthan.scholarlee.pack.build;

public class PackOptions {
    private final boolean useProvided;
    private final String providedVersion;

    private PackOptions(Builder builder) {
        this.useProvided = builder.useProvided;
        this.providedVersion = builder.providedVersion;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getProvidedVersion() {
        return providedVersion;
    }

    public boolean isUsingProvided() {
        return useProvided;
    }

    public static class Builder {
        private boolean useProvided = true;
        private String providedVersion = "1_20_4";

        Builder() {}

        public boolean useProvided() {
            return useProvided;
        }

        public void useProvided(boolean useDefaultAssets) {
            this.useProvided = useDefaultAssets;
        }

        public String providedVersion() {
            return providedVersion;
        }

        public void providedVersion(String assetVersion) {
            this.providedVersion = assetVersion;
        }

        public PackOptions build() {
            return new PackOptions(this);
        }
    }
}
