package fr.iut.tetris.enums;

public enum Resolution {
	BIG(1000,735,72,55,41,24),
	NORMAL(870,640,72,48,32,16),
	TINY(640,448,48,32,24,16),
	;

	public final int height;
	public final int width;
	public final int font_ultrabig;
	public final int font_big;
	public final int font_normal;
	public final int font_tiny;
	Resolution(int height,int width, int font_ultrabig, int font_big, int font_normal, int font_tiny) {
		this.height = height;
		this.width = width;
		this.font_ultrabig = font_ultrabig;
		this.font_big = font_big;
		this.font_normal = font_normal;
		this.font_tiny = font_tiny;
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

