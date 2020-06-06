package fr.iut.tetris;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class Config {
	String rootPath;
	String appConfigPath;
	Properties config;

	public Config() {
		rootPath = System.getProperty("user.home")+"/";
		appConfigPath = rootPath + "tetris_projtut.properties";


		config = new Properties();
		try {
			config.load(new FileInputStream(appConfigPath));
			System.out.println("Loaded config at: "+appConfigPath);
		} catch (IOException e) {
			System.out.println("Failed to load config file");
			config = defaultConfig();
			try {
				config.store(new FileWriter(appConfigPath),null);
				System.out.println("Generated default config file");
			} catch (IOException ex) {
				System.out.println("Failed to save default config file");
			}
		}
	}

	public void saveAsync() {
		new Thread(new Runnable() {
			@Override public void run() {
				try {
					config.store(new FileWriter(appConfigPath),null);
				} catch (IOException e) {
					System.out.println("Failed to save config");
				}
			}
		}).start();
	}
	public void putString(String key, String value) {
		config.put(key,value);
	}
	public void putInt(String key, int value) {
		config.put(key,String.valueOf(value));
	}
	public String getString(String key) {
		if(config.containsKey(key)) {
			return (String)config.get(key);
		}
		return "";
	}
	public int getInt(String key) {
		if(config.containsKey(key)) {
			return Integer.parseInt((String)config.get(key));
		}
		return 0;
	}

	static Properties defaultConfig() {
		Properties p = new Properties();
		p.put("KEYCODE_P1_LEFT"    ,"37"); // Left key
		p.put("KEYCODE_P1_RIGHT"   ,"39"); // Right key
		p.put("KEYCODE_P1_DOWN"    ,"40"); // Down key
		p.put("KEYCODE_P1_FASTDOWN","10"); // Enter key
		p.put("KEYCODE_GOBACK"     ,"27"); // Esc key
		p.put("KEYCODE_STARTGAME"  ,"32"); // Space key
		p.put("VOLUME_MUSIC", "0"); //0Gain
		p.put("VOLUME_SFX"  , "0"); //0Gain
		return p;
	}
}
