package fr.iut.tetris;

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

	private static volatile Config inst;
	/**
	 * Return the first instance of the config instead of re instantiation every time which would defeat the purpose of the caching mechanism
	 * @return Config
	 */
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

	/**
	 * Load the config and generate fonts used by the program
	 */
	public Config() {
		rootPath = System.getProperty("user.home")+"/";
		appConfigPath = rootPath + "tetris_projtut.properties";

		Properties defConf = defaultConfig();
		config = new Properties();
		try {
			config.load(new FileInputStream(appConfigPath));
			if(config.get("CONFIG_VERSION") != null && config.get("CONFIG_VERSION").equals(defConf.get("CONFIG_VERSION"))) {
				Log.info(this,"Loaded config at: "+appConfigPath);
			} else {
				Log.warning(this,"The stored config file has not the same version number as the default config file, overwriting it (L:"+config.get("CONFIG_VERSION")+" D:"+defConf.get("CONFIG_VERSION")+")");
				//Merge the two configs
				defConf.forEach((key, value) -> config.merge(key, value, (v1, v2) -> v1) );
				config.put("CONFIG_VERSION",defConf.get("CONFIG_VERSION"));
				try {
					config.store(new FileWriter(appConfigPath),null);
					Log.info(this,"Saved the new config");
				} catch (IOException ex) {
					Log.critical(this,"Failed to saved the new config");
				}
			}
		} catch (IOException e) {
			Log.warning(this,"Failed to load config file");
			config = defConf;
			try {
				config.store(new FileWriter(appConfigPath),null);
				Log.info(this,"Generated default config file");
			} catch (IOException ex) {
				Log.critical(this,"Failed to save default config file");
			}
		}

		this.reloadFonts();

		inst = this;
	}


	/**
	 * Put an object into memory
	 * @param key a string which you can use to retrive the key later
	 * @param obj the object in question
	 */
	public void cacheObject(String key, Object obj) {
		Log.info(this,"Saving object to cache : "+key);
		cachedRessources.put(key,obj);
	}

	/**
	 * Read an object from memory
	 * @param key the key used when using cacheObject
	 * @return the object that have been cached
	 */
	public Object getCachedObject(String key) {
		Log.info(this,"Loading cached object: "+key);
		if(cachedRessources.containsKey(key)) {
			return cachedRessources.get(key);
		}
		return null;
	}

	/**
	 * Load an image into memory for faster loading time later if the image is already cached when loading it if the image is not found a dummy will be loaded instead
	 * @param name the path of the image you wann read
	 * @return The image at the path
	 */
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

				BufferedImage b = null;

				b = new BufferedImage(5,5,BufferedImage.TYPE_INT_RGB);
				Graphics2D g2d = (Graphics2D) b.getGraphics();
				g2d.setColor(Color.BLACK);
				g2d.fillRect(0,0,5,5);
				g2d.setColor(Color.MAGENTA);
				for (int i = 0; i < 5*5; i++) {
					if(i%2==0)
						g2d.fillRect(i/5,i%5,1,1);
				}

				cachedRessources.put(name,b);
				return b;
			}
		}
	}

	/**
	 * Reload fonts (used after a resolution change)
	 */
	public void reloadFonts() {
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
		fonts.put("FONT_VERYTINY",font.deriveFont((float)this.getInt("FONT_VERYTINY")));
		GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(fonts.get("FONT_NORMAL"));
	}

	/**
	 * Return a font with correct size specified in the config
	 * @param key name of the font you want to get
	 * @return the font
	 */
	public Font getFont(String key) {
		if(fonts.containsKey(key)) {
			return fonts.get(key);
		}
		//Make sure the wasn't an udpate but only if it fails the first time
		reloadFonts();
		if(fonts.containsKey(key)) {
			return fonts.get(key);
		}
		return fonts.get("FONT_NORMAL");
	}

	/**
	 * Save the config asynchronously to a file
	 */
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

	/**
	 * Save a string a the emplacement "key" in th config file
	 * @param key The emplacement of the string
	 * @param value The string
	 */
	public void putString(String key, String value) {
		config.put(key,value);
	}
	/**
	 * Save a int a the emplacement "key" in th config file
	 * @param key The emplacement of the int
	 * @param value The int
	 */
	public void putInt(String key, int value) {
		config.put(key,String.valueOf(value));
	}
	/**
	 * Save a boolean a the emplacement "key" in th config file
	 * @param key The emplacement of the boolean
	 * @param value The boolean
	 */
	public void putBool(String key, boolean value) {
		config.put(key,String.valueOf(value));
	}

	/**
	 * Read a string from the config file
	 * @param key
	 * @return the string a the emplacement "key"
	 */
	public String getString(String key) {
		if(config.containsKey(key)) {
			return (String)config.get(key);
		}
		return "";
	}

	/**
	 * Read a int from the config file
	 * @param key
	 * @return the int a the emplacement "key"
	 */
	public int getInt(String key) {
		if(config.containsKey(key)) {
			return Integer.parseInt((String)config.get(key));
		}
		return 0;
	}

	/**
	 * Read a boolean from the config file
	 * @param key
	 * @return the boolean a the emplacement "key"
	 */
	public boolean getBool(String key) {
		if(config.containsKey(key)) {
			return ((String)config.get(key)).equals("true");
		}
		return false;
	}

	/**
	 * Generate a default config in case the user don't have one
	 * @return the default config
	 */
	static Properties defaultConfig() {
		Properties p = new Properties();
		p.put("CONFIG_VERSION"     ,"11");

		p.put("KEYCODE_P1_LEFT"    ,"37"); // Left key
		p.put("KEYCODE_P1_RIGHT"   ,"39"); // Right key
		p.put("KEYCODE_P1_DOWN"    ,"40"); // Down key
		p.put("KEYCODE_P1_FASTDOWN","10"); // Enter key
		p.put("KEYCODE_P1_ROTATE"  ,"38"); // Enter key

		p.put("KEYCODE_P2_LEFT"    ,"81"); // Q key
		p.put("KEYCODE_P2_RIGHT"   ,"68"); // D key
		p.put("KEYCODE_P2_DOWN"    ,"83"); // S key
		p.put("KEYCODE_P2_FASTDOWN","65"); // A key
		p.put("KEYCODE_P2_ROTATE"  ,"90"); // Z key

		p.put("KEYCODE_GOBACK"     ,"27"); // Esc key
		p.put("KEYCODE_STARTGAME"  ,"32"); // Space key

		p.put("VOLUME_MUSIC", "-11"); //-11dB
		p.put("VOLUME_SFX"  , "-21"); //-21dB

		p.put("SCORE_SOLO_BEST" ,"0");
		p.put("SCORE_COOP_BEST" ,"0");

		p.put("SCORE_SOLO_BEST_LEGACY" ,"0");
		p.put("SCORE_COOP_BEST_LEGACY" ,"0");

		p.put("WINDOW_HEIGHT"  ,"870");
		p.put("WINDOW_WIDTH"   ,"640");

		p.put("BORDER_SIZES"  ,"10");

		p.put("FONT_ULTRABIG" ,"72");
		p.put("FONT_BIG"      ,"48");
		p.put("FONT_NORMAL"   ,"32");
		p.put("FONT_TINY"     ,"16");
		p.put("FONT_VERYTINY" ,"16");

		p.put("LEGACY_PIECES" ,"false");
		p.put("VERSUS_EFFECTS","true");

		p.put("VERSUS_EFFECT_EVERY","80");


		p.put("EFFECT_DURATION_BONUS_SPEED","15");
		p.put("EFFECT_DURATION_MALUS_SPEED","15");
		p.put("EFFECT_DURATION_MALUS_BLIND","25");
		p.put("EFFECT_DURATION_MALUS_REVERSE","15");
		p.put("EFFECT_DURATION_MALUS_ROTATE","10");


		p.put("START_SPEED","1000");

		return p;
	}
}
