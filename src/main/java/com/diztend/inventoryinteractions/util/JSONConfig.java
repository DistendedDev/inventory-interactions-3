package com.diztend.inventoryinteractions.util;

import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

public class JSONConfig {

    private final Logger logger;
    private JsonObject config;

    public JSONConfig(String filename, JsonObject defaultConfig, Logger logger) {
        this.logger = logger;
        File file = FabricLoader.getInstance().getConfigDir().resolve(filename + ".json").toFile();
        try {
            if (file.createNewFile()) {
                this.config = defaultConfig;
                writeConfig(file, defaultConfig);
            } else {
                loadConfig(file);
            }
        } catch (Exception e) {
            logger.info("Error creating or writing config file, using default configs. " +
                    "To fix, please check the file for syntax errors, or delete the file to restore the original");
            this.config = defaultConfig;
        }
        boolean changed = false;
        for (Map.Entry<String, JsonElement> entry: defaultConfig.entrySet()) {
            if (!this.config.has(entry.getKey())) {
                this.config.add(entry.getKey(), entry.getValue());
                changed = true;
            }
        }
        if (changed) {
            try {
                writeConfig(file, config);
            } catch (Exception e) {
                logger.info("unable to overwrite config");
            }
        }
    }

    public boolean getBoolean(String key) {
        if (config.has(key)) {
            return config.get(key).getAsBoolean();
        }
        return false;
    }

    private void loadConfig(File config) throws FileNotFoundException {
        Scanner scanner = new Scanner(config);
        StringBuilder content = new StringBuilder();
        while (scanner.hasNext()) {
            content.append(scanner.next());
        }
        this.config = JsonParser.parseString(content.toString()).getAsJsonObject();
        logger.info(this.config);
    }

    private void writeConfig(File file, JsonObject config) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(config));
            logger.info("Config created");
        }
    }

}
