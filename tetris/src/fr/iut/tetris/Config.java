package fr.iut.tetris;

import sun.awt.image.BufferedImageDevice;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Config {
	String rootPath;
	String appConfigPath;
	Properties config;

	static String no_texture = "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAIAAAAmkwkpAAADt3pUWHRSYXcgcHJvZmlsZSB0eXBlIGV4aWYAAHjarZZtduMqDIb/axWzBCQhPpaDsTnn7uAuf14wTtOmk6YtUIMtY0noQUrp+P+/Rn/QRKKQt5hCDsGh+eyzFNwkd7YyRnZ+jGfzc+b3crq9EIgUs56PKUz5AblgvUx5nXoK5HanKB/zxfb+RZmKJE0Dl0fTkPJpwE3FVKYilWl5erid23Ihp3i/hTrXt/l+hCH1rWHwGiVY4OgxenExhoz7JM5HxG3vjrYquX9n21T04ZmupQKf5FBWh9F3D/W8Cq4wxtjXqOJehoQ1nJ6ClpCLuM9XXJ+3Z57T5fpE/h71dcf/kN+RphHQ64V+IBRu86dytjc53SMd3O4sh3Cz/E4efQ/IXbuo9au1PbV2nLsrPmDLYW7q2sq4w7qtR2t8FdAjLnOpB7v3jJ6QEhXnaMch29ArZxZgbOx558KNjzFXrnDRy4EUcyJSRbkShAkwslQAZvW9c5OoWXdNAF1xHBRSufnCw2we5ionGN45EU4PQxnjk191emVRaz2hmF26xQp+Sc80eNHJMZNjLAMRbjOoNgJ89Y+tc1UQtBHmhA0Wt3UNwL8Zvx0uHaAVCw3zmcAc96kAIYIHBmdYQcAFVuPALopQZEYgEwAVuC7qZQMWNpMdTopXDYCDLIBtfBN5LBWTU4xCCBCmgTSCTdYCWN4bzk/0CWeomJo3s2DRkmUrQUPPsBBi6BW1RI0+WgwxxhRzLJQ0+WQppJhSyqlkyYqKaxn5mFPOuRQYLdBc8HXBglI22XTzm21hi1va8laqUNXqq9VQY00117LLrjvyeA973NOe93LwgaN0+MOOcMQjHfkoDUetafPNWmixpZaplRu1ifWhf4MaT2oySPWF8UYN0hgvFdzLiXVmICaeATx2Aqwk0pm5xN5LJ9eZudwLoQmctA5n504MBP3BYo1v7N7IGaFoLuFGACEryFFH9wK5R26fUdvL+KHTQainYQ+qU2RfTUXwtzW/KT5y4wll8MOs1aiMpa3u+KVKbsiLe5hbyF9opJvKX2qkRyd/ppGebPtbGumVQL6ikV4J5Csa6Vtonmikn8F+1EhPA/nVfGeRVgS6a6QVge4zrQh0V0ArAt010i/y9J1GWpH55zlakPlnri3IfDdS5JNtfzV/ZpFkUY7QikB3jWSLijaVRUWb3KKiTW1R0aayqGhTWVS06YcF8cEilUU5Qkt+HaGR/KKiTWFR0SZZVLTJLSralBYVbZJFRbuXWsV/kZn+AoNiam4+B1PfAAAAYXpUWHRSYXcgcHJvZmlsZSB0eXBlIGlwdGMAAHjaPYnLCcAwDEPvnqIjyJZx0nXqXHrLoftTY2glHugj935SjpabcLr56Qte/qVLE8ZRcdKIBoxi9JP1XLWyCCpDbH0VkBcxZBTga1DMhgAAD1RpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+Cjx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IlhNUCBDb3JlIDQuNC4wLUV4aXYyIj4KIDxyZGY6UkRGIHhtbG5zOnJkZj0iaHR0cDovL3d3dy53My5vcmcvMTk5OS8wMi8yMi1yZGYtc3ludGF4LW5zIyI+CiAgPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIKICAgIHhtbG5zOmlwdGNFeHQ9Imh0dHA6Ly9pcHRjLm9yZy9zdGQvSXB0YzR4bXBFeHQvMjAwOC0wMi0yOS8iCiAgICB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIKICAgIHhtbG5zOnN0RXZ0PSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VFdmVudCMiCiAgICB4bWxuczpwbHVzPSJodHRwOi8vbnMudXNlcGx1cy5vcmcvbGRmL3htcC8xLjAvIgogICAgeG1sbnM6R0lNUD0iaHR0cDovL3d3dy5naW1wLm9yZy94bXAvIgogICAgeG1sbnM6ZGM9Imh0dHA6Ly9wdXJsLm9yZy9kYy9lbGVtZW50cy8xLjEvIgogICAgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIgogICB4bXBNTTpEb2N1bWVudElEPSJnaW1wOmRvY2lkOmdpbXA6NDUyMTIyODktM2ZlNS00ZGZhLTlkYzQtNzc2Zjg5ZmE4Y2MzIgogICB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOjgyOWI3YTJmLWM5MjUtNDRkZi05ZjhkLTE1Nzg4NDUzYTdjNiIKICAgeG1wTU06T3JpZ2luYWxEb2N1bWVudElEPSJ4bXAuZGlkOjNhNmJmNGMzLTViMTQtNDlmYS1hNzdhLTczYzA2ZDkyOTQwNiIKICAgR0lNUDpBUEk9IjIuMCIKICAgR0lNUDpQbGF0Zm9ybT0iV2luZG93cyIKICAgR0lNUDpUaW1lU3RhbXA9IjE1OTE1NjUxNzY3NDIzNzYiCiAgIEdJTVA6VmVyc2lvbj0iMi4xMC40IgogICBkYzpGb3JtYXQ9ImltYWdlL3BuZyIKICAgeG1wOkNyZWF0b3JUb29sPSJHSU1QIDIuMTAiPgogICA8aXB0Y0V4dDpMb2NhdGlvbkNyZWF0ZWQ+CiAgICA8cmRmOkJhZy8+CiAgIDwvaXB0Y0V4dDpMb2NhdGlvbkNyZWF0ZWQ+CiAgIDxpcHRjRXh0OkxvY2F0aW9uU2hvd24+CiAgICA8cmRmOkJhZy8+CiAgIDwvaXB0Y0V4dDpMb2NhdGlvblNob3duPgogICA8aXB0Y0V4dDpBcnR3b3JrT3JPYmplY3Q+CiAgICA8cmRmOkJhZy8+CiAgIDwvaXB0Y0V4dDpBcnR3b3JrT3JPYmplY3Q+CiAgIDxpcHRjRXh0OlJlZ2lzdHJ5SWQ+CiAgICA8cmRmOkJhZy8+CiAgIDwvaXB0Y0V4dDpSZWdpc3RyeUlkPgogICA8eG1wTU06SGlzdG9yeT4KICAgIDxyZGY6U2VxPgogICAgIDxyZGY6bGkKICAgICAgc3RFdnQ6YWN0aW9uPSJzYXZlZCIKICAgICAgc3RFdnQ6Y2hhbmdlZD0iLyIKICAgICAgc3RFdnQ6aW5zdGFuY2VJRD0ieG1wLmlpZDplZDMzZmU5ZC0yMGIxLTQwZDUtOGEzYi1mNjI0OGJkMzZjZjMiCiAgICAgIHN0RXZ0OnNvZnR3YXJlQWdlbnQ9IkdpbXAgMi4xMCAoV2luZG93cykiCiAgICAgIHN0RXZ0OndoZW49IjIwMjAtMDYtMDdUMjM6MjY6MTYiLz4KICAgIDwvcmRmOlNlcT4KICAgPC94bXBNTTpIaXN0b3J5PgogICA8cGx1czpJbWFnZVN1cHBsaWVyPgogICAgPHJkZjpTZXEvPgogICA8L3BsdXM6SW1hZ2VTdXBwbGllcj4KICAgPHBsdXM6SW1hZ2VDcmVhdG9yPgogICAgPHJkZjpTZXEvPgogICA8L3BsdXM6SW1hZ2VDcmVhdG9yPgogICA8cGx1czpDb3B5cmlnaHRPd25lcj4KICAgIDxyZGY6U2VxLz4KICAgPC9wbHVzOkNvcHlyaWdodE93bmVyPgogICA8cGx1czpMaWNlbnNvcj4KICAgIDxyZGY6U2VxLz4KICAgPC9wbHVzOkxpY2Vuc29yPgogIDwvcmRmOkRlc2NyaXB0aW9uPgogPC9yZGY6UkRGPgo8L3g6eG1wbWV0YT4KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgIAo8P3hwYWNrZXQgZW5kPSJ3Ij8+JY/VBAAAAAZiS0dEAAAAAAAA+UO7fwAAAAlwSFlzAAAuIwAALiMBeKU/dgAAAAd0SU1FB+QGBxUaEOThtQkAAAAZdEVYdENvbW1lbnQAQ3JlYXRlZCB3aXRoIEdJTVBXgQ4XAAAAGElEQVQI12P4z/CfgYEBQjLAWf+hEKsMAJ6jD/E127U7AAAAAElFTkSuQmCC";

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
	Map<String, Object> cachedRessources = new HashMap<>();

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

	public void cacheObject(String key, Object obj) {
		Log.info(this,"Saving object to cache : "+key);
		cachedRessources.put(key,obj);
	}
	public Object getCachedObject(String key) {
		Log.info(this,"Loading cached object: "+key);
		if(cachedRessources.containsKey(key)) {
			return cachedRessources.get(key);
		}
		return null;
	}

	public BufferedImage getRessourceImage(String name) {
		if(cachedRessources.containsKey(name)) {
			Log.info(this,"Loading ressource: "+name+" from memory");
			return (BufferedImage) cachedRessources.get(name);
		} else {
			Log.info(this,"Loading ressource: "+name+" from file");
			URL u = getClass().getResource(name);
			try {
				BufferedImage b = ImageIO.read(u);
				cachedRessources.put(name,b);
				return b;
			} catch (IOException | IllegalArgumentException e) {
				Log.error(this,"Could not load image at location: "+name+" loading default texture");

				try {
					BufferedImage b = null;
					b = ImageIO.read(new ByteArrayInputStream(new BASE64Decoder().decodeBuffer(Config.no_texture)));
					cachedRessources.put(name,b);
					return b;
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				return null;
			}
		}
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
