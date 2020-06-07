package fr.iut.tetris;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Config {
	String rootPath;
	String appConfigPath;
	Properties config;

	private static volatile Config inst;
	public static Config getInstance() {
		if (inst == null) {
			synchronized (Config.class) {
				if (inst == null) {
					inst = new Config();
				}
			}
		}
		return inst;
	}

	Map<String, Font> fonts = new HashMap<>();

	public Config() {
		rootPath = System.getProperty("user.home")+"/";
		appConfigPath = rootPath + "tetris_projtut.properties";


		config = new Properties();
		try {
			config.load(new FileInputStream(appConfigPath));
			Log.info(this,"Loaded config at: "+appConfigPath);
		} catch (IOException e) {
			Log.warning(this,"Failed to load config file");
			config = defaultConfig();
			try {
				config.store(new FileWriter(appConfigPath),null);
				Log.info(this,"Generated default config file");
			} catch (IOException ex) {
				Log.critical(this,"Failed to save default config file");
			}
		}

		Font font = new JLabel().getFont();
		try {
			InputStream is = Main.class.getResourceAsStream("/res/retro.ttf");
			font = Font.createFont(Font.TRUETYPE_FONT, is);
			Log.info(this,"Loaded font: /res/retro.ttf");
		} catch (Exception e) {
			e.printStackTrace();
		}

		fonts.put("FONT_ULTRABIG",font.deriveFont((float)this.getInt("FONT_ULTRABIG")));
		fonts.put("FONT_BIG",font.deriveFont((float)this.getInt("FONT_BIG")));
		fonts.put("FONT_NORMAL",font.deriveFont((float)this.getInt("FONT_NORMAL")));
		fonts.put("FONT_TINY",font.deriveFont((float)this.getInt("FONT_TINY")));
		GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(fonts.get("FONT_NORMAL"));

		inst = this;
	}

	public Font getFont(String key) {
		if(fonts.containsKey(key)) {
			return fonts.get(key);
		}
		return fonts.get("FONT_NORMAL");
	}

	public void saveAsync() {
		new Thread(new Runnable() {
			@Override public void run() {
				try {
					config.store(new FileWriter(appConfigPath),null);
					Log.info(this,"Config saved");
				} catch (IOException e) {
					Log.critical(this,"Failed to save config");
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
		p.put("KEYCODE_P1_ROTATE"  ,"38"); // Enter key
		p.put("KEYCODE_GOBACK"     ,"27"); // Esc key
		p.put("KEYCODE_STARTGAME"  ,"32"); // Space key
		p.put("VOLUME_MUSIC", "0"); //0Gain
		p.put("VOLUME_SFX"  , "0"); //0Gain
		p.put("SCORE_SOLO_BEST" ,"0"); // Enter key
		p.put("WINDOW_HEIGHT"  ,"870");
		p.put("WINDOW_WIDTH"   ,"640");
		p.put("FONT_ULTRABIG" ,"72");
		p.put("FONT_BIG"      ,"48");
		p.put("FONT_NORMAL"   ,"32");
		p.put("FONT_TINY"     ,"16");
		return p;
	}
}
