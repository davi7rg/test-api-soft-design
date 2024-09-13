package org.softdesign.config;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;

public class ConfigReader {

    private static final String CONFIG_FILE = "config.properties";
    private static Configuration config;

    static {
        try {
            Configurations configs = new Configurations();
            config = configs.properties(CONFIG_FILE);
        } catch (Exception e) {
            throw new RuntimeException("Error loading configuration file", e);
        }
    }

    public static String getBaseUrl() {
        return config.getString("app.api.baseUrl");
    }

    public static String getEndpointAuthLogin() {
        return config.getString("app.api.endpoint.auth.login");
    }

    public static String getEndpointAuthProductsAdd() {
        return config.getString("app.api.endpoint.auth.products.add");
    }

    public static String getEndpointUsers() {
        return config.getString("app.api.endpoint.users");
    }
}
