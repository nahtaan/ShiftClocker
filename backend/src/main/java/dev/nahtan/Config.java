package dev.nahtan;

import org.tinylog.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.net.URL;
import java.util.Map;

@SuppressWarnings("ALL")
public class Config {
    private String db_username;
    private String db_password;
    private String db_name;
    private String db_host;
    private Integer db_port;

    public boolean loadDefaultFile() {
        File externalFile = new File(Main.getContainerPath() + "/config.yml");

        // Simply load from the file if the file already exists.
        if(externalFile.exists() && !externalFile.isDirectory()) {
            loadFromFile();
            return true;
        }

        // extract the file if it is not already present
        String internalFileName = "jar:file:" + Main.getJarPath() + "!/config.yml";
        try {
            @SuppressWarnings("deprecation")
            InputStream is = new URL(internalFileName).openStream();
            FileOutputStream fos = new FileOutputStream(externalFile);
            fos.write(is.readAllBytes());
            fos.close();
            is.close();
            Logger.info("Created a new configuration file. Please change the values and then start the server again.");
            return false;
        }catch (IOException e) {
            Logger.error("Could not load config.yml from internal file. {}", e.getMessage());
            return false;
        }
    }

    public void loadFromFile(){
        try {
            File externalFile = new File(Main.getContainerPath() + "/config.yml");
            Yaml yaml = new Yaml();
            InputStream inputStream = new FileInputStream(externalFile);
            Map<String, Object> loaded = yaml.load(inputStream);
            for(String entry: loaded.keySet()) {
                try {
                    switch (entry) {
                        case "db-username" -> {
                            db_username = (String) loaded.get(entry);
                        }
                        case "db-password" -> {
                            db_password = (String) loaded.get(entry);
                        }
                        case "db-name" -> {
                            db_name = (String) loaded.get(entry);
                        }
                        case "db-host" -> {
                            db_host = (String) loaded.get(entry);
                        }
                        case "db-port" -> {
                            db_port = (Integer) loaded.get(entry);
                        }
                    }
                }catch (ClassCastException e) {
                    Logger.error("Config option {} was of the wrong type! {}", entry, e.getMessage());
                }
            }
        }catch (FileNotFoundException e) {
            Logger.error("Could not load config.yml from external file. {}", e.getMessage());
        }
    }

    public String getDb_username() {
        return db_username;
    }

    public String getDb_password() {
        return db_password;
    }

    public String getDb_name() {
        return db_name;
    }

    public String getDb_host() {
        return db_host;
    }

    public Integer getDb_port() {
        return db_port;
    }
}
