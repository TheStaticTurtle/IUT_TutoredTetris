package fr.iut.tetris.enums;

import java.awt.*;

public enum Resolution {
	BIG(1000,735,72,55,41,24,22,13),
	NORMAL(870,640,72,48,32,16,18,12),
	TINY(640,448,48,32,24,16,12,9),
	;

	public final int height;
	public final int width;
	public final int font_ultrabig;
	public final int font_big;
	public final int font_normal;
	public final int font_tiny;
	public final int font_verytiny;
	public final int border_size;
	Resolution(int height,int width, int font_ultrabig, int font_big, int font_normal, int font_tiny, int font_verytiny, int border_size) {
		this.height = height;
		this.width = width;
		this.font_ultrabig = font_ultrabig;
		this.font_big = font_big;
		this.font_normal = font_normal;
		this.font_tiny = font_tiny;
		this.font_verytiny = font_verytiny;
		this.border_size = border_size;
	}

	public static Resolution getFromSize(Dimension t) {
		for (Resolution r : Resolution.values()) {
			if(r.width == t.width && r.height == t.height) return r;
		}
		return null;
	}

	@Override
	public String toString() {
		return name()+": "+width+"x"+height;
	}
}

