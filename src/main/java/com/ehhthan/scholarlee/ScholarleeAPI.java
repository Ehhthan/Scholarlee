package com.ehhthan.scholarlee;

import com.ehhthan.scholarlee.pack.build.BuiltPack;
import com.ehhthan.scholarlee.pack.build.BuiltPackOptions;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ScholarleeAPI {
    private static ScholarleeAPI INSTANCE;

    private final System.Logger logger;

    private final Map<String, BuiltPack> packs = new HashMap<>();

    private ScholarleeAPI() {
        this.logger = System.getLogger(this.getClass().getName());
    }

    public static ScholarleeAPI get() {
        if (INSTANCE == null)
            INSTANCE = new ScholarleeAPI();

        return INSTANCE;
    }

    public System.Logger getLogger() {
        return logger;
    }

    public BuiltPack readPack(String key, BuiltPackOptions.Builder builder) {
        BuiltPack builtPack = new BuiltPack(builder.build());
        this.packs.put(key, builtPack);

        return builtPack;
    }

    public BuiltPack readPack(String key, BuiltPackOptions options) {
        BuiltPack builtPack = new BuiltPack(options);
        this.packs.put(key, builtPack);

        return builtPack;
    }

    public BuiltPack getPack(String key) {
        return packs.get(key);
    }

    public boolean hasPack(String key) {
        return packs.containsKey(key);
    }

    public Collection<BuiltPack> getPacks() {
        return packs.values();
    }

    public Set<String> getPackKeys() {
        return packs.keySet();
    }
}
