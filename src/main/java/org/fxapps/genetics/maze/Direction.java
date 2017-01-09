package org.fxapps.genetics.maze;

public enum Direction {
	
	UP('↑'), DOWN('↓'), LEFT('←'), RIGHT('→');

	private Character ch;

	private Direction(Character ch) {
		this.ch = ch;
	}
	
	@Override
	public String toString() {
		return this.ch.toString();
	}
	
}
