package com.ehhthan.scholarlee;

import com.ehhthan.scholarlee.pack.FileResourcePack;
import com.ehhthan.scholarlee.pack.ResourcePack;
import net.lingala.zip4j.ZipFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

public class ScholarleeAPI {
    private static final System.Logger LOGGER = System.getLogger("ScholarleeAPI");
    private static ScholarleeAPI INSTANCE;

    private final Map<ProvidedVersion, ResourcePack> provided = new HashMap<>();

    private ScholarleeAPI() {
        try {
            File workingDir = Files.createTempDirectory("scholarlee").toFile();

            File versionsDir = new File(workingDir, "versions");
            versionsDir.mkdirs();

            File providedDir = new File(workingDir, "provided");
            providedDir.mkdirs();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                deleteDir(workingDir);
            }));

            // Generate MC Assets
            for (ProvidedVersion value : ProvidedVersion.values()) {
                File provided = new File(workingDir, value.path);

                InputStream stream = getClass().getResourceAsStream(value.path);

                if (stream != null)
                    Files.copy(stream, provided.toPath(), StandardCopyOption.REPLACE_EXISTING);

                File extract = new File(providedDir, value.name());
                extract.mkdirs();

                new ZipFile(provided).extractAll(extract.getPath());
                this.provided.put(value, new FileResourcePack(extract));
            }


        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Provided assets could not be created.");
        }
    }

    public static ScholarleeAPI get() {
        if (INSTANCE == null) {
            INSTANCE = new ScholarleeAPI();
        }

        return INSTANCE;
    }

    public static System.Logger getLogger() {
        return LOGGER;
    }

    public ResourcePack readPackFromFile(File file) {
        return new FileResourcePack(file);
    }

    public ResourcePack getProvided(ProvidedVersion version) {
        return provided.get(version);
    }

    private void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                if (! Files.isSymbolicLink(f.toPath())) {
                    deleteDir(f);
                }
            }
        }
        file.delete();
    }

    public enum ProvidedVersion {
        V1_20_4("/versions/1_20_4.zip");

        private final String path;

        ProvidedVersion(String path) {
            this.path = path;
        }
    }
}
