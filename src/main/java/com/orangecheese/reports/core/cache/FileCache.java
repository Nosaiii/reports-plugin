package com.orangecheese.reports.core.cache;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

public class FileCache<T> {
    private final JavaPlugin plugin;

    private final String fileName;

    private final boolean keepFile;

    private final Class<T> contentClass;

    public FileCache(JavaPlugin plugin, String fileName, boolean keepFile, Class<T> contentClass) {
        this.plugin = plugin;
        this.fileName = fileName;
        this.keepFile = keepFile;
        this.contentClass = contentClass;
    }

    public void serialize(T instance) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(instance);

        File file = getFile();

        if(!file.getParentFile().exists()) {
            try {
                if(!file.getParentFile().mkdirs())
                    throw new IOException("Unable to create cache file");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if(!file.exists()) {
            try {
                if(!file.createNewFile())
                    throw new IOException("Unable to create cache file");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deserialize(Consumer<T> onDeserialization) {
        File file = getFile();

        if(!file.exists())
            return;

        Gson gson = new Gson();
        T content;

        try (FileReader reader = new FileReader(file)) {
            StringBuilder contentString = new StringBuilder();

            int currentCharacter;
            while((currentCharacter = reader.read()) != -1)
                contentString.append((char) currentCharacter);

            String jsonContent = contentString.toString();
            content = gson.fromJson(jsonContent, contentClass);
        } catch(IOException e) {
            throw new RuntimeException(e);
        }

        onDeserialization.accept(content);

        if(!keepFile)
            file.delete();
    }

    public boolean exists() {
        File file = getFile();
        return file.exists();
    }

    private File getFile() {
        String fileNameWithExtension = fileName + ".dat";
        Path filePath = Paths.get(plugin.getDataFolder().getAbsolutePath(), ".cache", fileNameWithExtension);
        return new File(filePath.toString());
    }
}