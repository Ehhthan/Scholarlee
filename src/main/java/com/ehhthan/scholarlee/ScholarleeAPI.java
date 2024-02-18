package com.ehhthan.scholarlee;

import com.ehhthan.scholarlee.file.DirectoryCopyFileVisitor;
import com.ehhthan.scholarlee.pack.FileResourcePack;
import com.ehhthan.scholarlee.pack.ResourcePack;
import com.ehhthan.scholarlee.pack.build.BuiltPack;
import com.ehhthan.scholarlee.pack.build.PackOptions;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class ScholarleeAPI {
    private static final Map<Plugin, ScholarleeAPI> API_INSTANCES = new HashMap<>();

    private final Plugin plugin;
    private final File workingDirectory;

    private final Map<String, BuiltPack> packs = new HashMap<>();

    private ScholarleeAPI(Plugin plugin) {
        this.workingDirectory = new File(plugin.getDataFolder().getParentFile(), "Scholarlee");

        this.plugin = plugin;
        workingDirectory.mkdirs();

        try {
            new DirectoryCopyFileVisitor().copy("mc", new File(workingDirectory, "versions").toPath());
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Provided assets could not be created.");
        }

    }

    public static ScholarleeAPI get(Plugin plugin) {
        if (!API_INSTANCES.containsKey(plugin))
            API_INSTANCES.put(plugin, new ScholarleeAPI(plugin));

        return API_INSTANCES.get(plugin);
    }

    public Logger getLogger() {
        return plugin.getLogger();
    }

    public File getWorkingDirectory() {
        return workingDirectory;
    }

    public BuiltPack readPackFromFile(String key, File packFile) {
        return readPackFromFile(key, packFile, PackOptions.builder().build());
    }

    public BuiltPack readPackFromFile(String key, File packFile, PackOptions options) {
        ResourcePack pack = new FileResourcePack(this, packFile, options);
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

    public File getProvidedAssets(String version) {
        File directory = new File(workingDirectory, String.format("versions/%s/assets", version));

        if (!directory.exists())
            throw new IllegalStateException("Requested provided assets do not exist.");

        return directory;
    }
}
