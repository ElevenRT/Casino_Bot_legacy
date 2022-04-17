package com.eleven.casinobot.config;

import com.eleven.casinobot.CasinoBotApplication;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;

public class BotConfig {

    private static String TOKEN;

    private BotConfig() {

    }

    public static String getTOKEN() {
        if (TOKEN == null) {
            TOKEN = getToken();
        }
        return TOKEN;
    }

    private static String getToken() {
        String token = "";

        try {
            URL url = CasinoBotApplication.class
                    .getClassLoader()
                    .getResource("config.yml");
            File file = Paths.get(url != null ? url.toURI() : new URI("")).toFile();
            HashMap<String, Object> objects = new Yaml()
                    .load(new FileReader(file.getAbsolutePath()));
            String temp = objects.get("bot").toString().split("\\{")[1];
            token = temp.substring(6, temp.length()-1);
        } catch (FileNotFoundException | URISyntaxException e) {
            e.printStackTrace();
        }
        return token;
    }

}
