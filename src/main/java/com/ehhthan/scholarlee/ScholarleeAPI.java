package com.ehhthan.scholarlee;

import com.ehhthan.scholarlee.pack.FileResourcePack;
import com.ehhthan.scholarlee.pack.ResourcePack;
import com.ehhthan.scholarlee.pack.build.BuiltPack;
import com.ehhthan.scholarlee.pack.build.PackOptions;

import java.io.File;
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

    public BuiltPack readPackFromFile(String key, File packFile, PackOptions options) {
        ResourcePack pack = new FileResourcePack(packFile, options);
        BuiltPack builtPack = new BuiltPack(pack);
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
