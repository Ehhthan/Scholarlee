package com.ehhthan.scholarlee.api;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.regex.Pattern;

public final class NamespacedKey {
    public static final String MINECRAFT = "minecraft";

    private static final Pattern VALID_NAMESPACE = Pattern.compile("[a-z0-9._-]+");
    private static final Pattern VALID_KEY = Pattern.compile("[a-z0-9/._-]+");

    private final String namespace;
    private final String key;

    public NamespacedKey(@NotNull String namespace, @NotNull String key) {
        if (!VALID_NAMESPACE.matcher(namespace).matches())
            throw new IllegalArgumentException(String.format("Invalid namespace. Must be [a-z0-9._-]: %s", namespace));

        if (!VALID_KEY.matcher(namespace).matches())
            throw new IllegalArgumentException(String.format("Invalid key. Must be [a-z0-9/._-]: %s", key));

        this.namespace = namespace;
        this.key = key;

        if (toString().length() >= 256)
            throw new IllegalArgumentException("NamespacedKey must be less than 256 characters.");
    }

    public static NamespacedKey fromString(String value) {
        String[] split = value.split(":");
        if (split.length == 1) {
            return new NamespacedKey(MINECRAFT, split[0]);
        } else if (split.length == 2) {
            return new NamespacedKey(split[0], split[1]);
        } else {
            throw new IllegalArgumentException(String.format("NamespacedKey is incorrectly formatted: %s", value));
        }
    }

    public static NamespacedKey fromPath(Path path) {

    }

    @NotNull
    public String getNamespace() {
        return namespace;
    }

    @NotNull
    public String getKey() {
        return key;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + this.namespace.hashCode();
        hash = 47 * hash + this.key.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NamespacedKey other = (NamespacedKey) obj;
        return this.namespace.equals(other.namespace) && this.key.equals(other.key);
    }

    @Override
    public String toString() {
        return this.namespace + ":" + this.key;
    }

    @NotNull
    public static NamespacedKey minecraft(@NotNull String key) {
        return new NamespacedKey(MINECRAFT, key);
    }
}