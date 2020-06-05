package fr.iut.tetris.enums;

public enum Direction {
	LEFT(-1),
	RIGHT(1),
	TURN_LEFT(-1),
	TURN_RIGHT(1);

	public final int step;
	Direction(int step) {
		this.step = step;
	}
}
