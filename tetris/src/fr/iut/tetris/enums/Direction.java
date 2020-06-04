package fr.iut.tetris.enums;

public enum Direction {
	LEFT(-1),
	RIGHT(1),
	TURN_LEFT(99),
	TURN_RIGHT(101);

	public final int step;
	private Direction(int step) {
		this.step = step;
	}
}
