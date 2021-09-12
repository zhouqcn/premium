package com.finmind.premium;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class Config {
    @JsonProperty("Config")
    private HashMap<String, InsurerConfig> insurerConfigs;

    public static Config readConfig(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        try (
                InputStream s = Config.class.getClassLoader().getResourceAsStream(filePath);
                JsonParser parser = objectMapper.getFactory().createParser(s)
                ) {
            return parser.readValueAs(Config.class);
        }
    }

    public HashMap<String, InsurerConfig> getInsurerConfigs() {
        return this.insurerConfigs;
    }
}
