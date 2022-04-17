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
    private static String PREFIX;
    private static String URL;
    private static String USERNAME;
    private static String PASSWORD;
    private static String DEV;

    private BotConfig() {

    }

    public static String getToken() {
        if (TOKEN == null) {
            TOKEN = getData(DataType.TOKEN);
        }
        return TOKEN;
    }

    public static String getPrefix() {
        if (PREFIX == null) {
            PREFIX = getData(DataType.PREFIX);
        }
        return PREFIX;
    }

    public static String getURL() {
        if (URL == null) {
            getDatabase();
        }
        return URL;
    }

    public static String getUsername() {
        if (USERNAME == null) {
            getDatabase();
        }
        return USERNAME;
    }

    public static String getPassword() {
        if (PASSWORD == null) {
            getDatabase();
        }
        return PASSWORD;
    }

    public static String getDEV() {
        if (DEV == null) {
            DEV = getData(DataType.DEVELOPER);
        }
        return DEV;
    }

    private static String getData(DataType dataType) {
        String data = "";

        try {
            URL url = CasinoBotApplication.class
                    .getClassLoader()
                    .getResource("config.yml");
            File file = Paths.get(url != null ? url.toURI() : new URI("")).toFile();
            HashMap<String, Object> objects = new Yaml()
                    .load(new FileReader(file.getAbsolutePath()));
            String temp = objects.get("bot").toString().split("\\{")[1];
            data = temp.split("[,]")[dataType.ordinal()]
                    .trim()
                    .substring(dataType.toString().length() + 1);
            if (dataType == DataType.DEVELOPER) {
                data = data.substring(0, data.length() - 1);
            }
        } catch (FileNotFoundException | URISyntaxException e) {
            e.printStackTrace();
        }
        return data;
    }

    private static void getDatabase() {
        try {
            URL url = CasinoBotApplication.class
                    .getClassLoader()
                    .getResource("config.yml");
            File file = Paths.get(url != null ? url.toURI() : new URI("")).toFile();
            HashMap<String, Object> objects = new Yaml()
                    .load(new FileReader(file.getAbsolutePath()));
            String temp = objects.get("database").toString().split("\\{")[1];
            URL = temp.split("[,]")[0]
                    .trim()
                    .substring(4);
            USERNAME = temp.split("[,]")[1]
                    .trim()
                    .substring(9);
            PASSWORD = temp.split("[,]")[2]
                    .trim()
                    .substring(9);
            PASSWORD = PASSWORD.substring(0, PASSWORD.length() - 1);
        } catch (FileNotFoundException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private enum DataType {
        TOKEN,
        PREFIX,
        DEVELOPER
    }

}
