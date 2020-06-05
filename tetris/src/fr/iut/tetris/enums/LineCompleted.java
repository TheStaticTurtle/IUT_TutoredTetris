package fr.iut.tetris.enums;

public enum LineCompleted {
	NO_LINE(0),

	NORMAL_LINE(1),
	DOUBLE_LINE(2),
	TRIPLE_LINE(3),
	QUAD_LINE(4),

	BOTTOM_NORMAL_LINE(2),
	BOTTOM_DOUBLE_LINE(3),
	BOTTOM_TRIPLE_LINE(4),
	BOTTOM_QUAD_LINE(5);

	public final int pointMultiplier;
	LineCompleted(int pointMultiplier) {
		this.pointMultiplier = pointMultiplier;
	}

	static public LineCompleted getScore(int lineCount, int lastLineY, int gameHeight) {
		if(lineCount == 0) return NO_LINE;
		if(lastLineY == gameHeight) {
			switch (lineCount) {
				case 1:
					return BOTTOM_NORMAL_LINE;
				case 2:
					return BOTTOM_DOUBLE_LINE;
				case 3:
					return BOTTOM_TRIPLE_LINE;
				case 4:
					return BOTTOM_QUAD_LINE;
			}
		} else {
			switch (lineCount) {
				case 1:
					return NORMAL_LINE;
				case 2:
					return DOUBLE_LINE;
				case 3:
					return TRIPLE_LINE;
				case 4:
					return QUAD_LINE;
			}
		}
		return NO_LINE; //Unkown state
	}
}
