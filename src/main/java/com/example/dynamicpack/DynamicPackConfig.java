package com.example.dynamicpack;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import net.fabricmc.loader.api.FabricLoader;

final class DynamicPackConfig {
    private static final String DEFAULT_ENDPOINT = "http://localhost:8080/resource-pack.sha1";
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("dynamic-pack-mod.properties");

    private static volatile String hashEndpoint = DEFAULT_ENDPOINT;

    private DynamicPackConfig() {
    }

    static void load() {
        Properties properties = new Properties();
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            if (Files.notExists(CONFIG_PATH)) {
                properties.setProperty("hashEndpoint", DEFAULT_ENDPOINT);
                try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
                    properties.store(writer, "Dynamic Pack Mod configuration");
                }
            } else {
                try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
                    properties.load(reader);
                }
            }
            hashEndpoint = properties.getProperty("hashEndpoint", DEFAULT_ENDPOINT).trim();
        } catch (IOException exception) {
            hashEndpoint = DEFAULT_ENDPOINT;
            System.err.println("[Dynamic Pack Mod] Could not load configuration: " + exception.getMessage());
        }
    }

    static String hashEndpoint() {
        return hashEndpoint;
    }
}
