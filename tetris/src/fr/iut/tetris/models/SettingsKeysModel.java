package fr.iut.tetris.models;

import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class SettingsKeysModel {
	public Map<Integer,String> keycodes = new HashMap<>();

	/**
	 * Generate an hashmap for the Keycodes-Keyname
	 */
	public SettingsKeysModel() {
		//Create a map of keycodes / keyname
		Field[] fields = java.awt.event.KeyEvent.class.getDeclaredFields();
		for (Field f : fields) {
			if (Modifier.isStatic(f.getModifiers()) && Modifier.isPublic(f.getModifiers()) && (f.getName().startsWith("KEY_") || f.getName().startsWith("VK_"))) {
				try {
					keycodes.put((Integer) f.get(KeyEvent.class),f.getName().split("_")[1]);
				} catch (IllegalAccessException ignored) {}
			}
		}
	}
}
