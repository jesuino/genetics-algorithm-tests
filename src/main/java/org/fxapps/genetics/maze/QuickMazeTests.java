package org.fxapps.genetics.maze;

import java.util.Random;

/**
 * 
 * If we try random, it never reaches the target!
 * 
 * @author wsiqueir
 *
 */
public class QuickMazeTests {
	static int w = 10, h = 10;
	public static void main(String[] args) {
		Maze m = new Maze(w, h);
		Direction[] directions = new Direction[ w + h ];
		Random r = new Random();
		int totalValues = Direction.values().length;
		for (int i = 0; i < directions.length; i++) {
			directions[i] = Direction.values()[r.nextInt(totalValues)];
		}
		
		MazeWalkingInfo info = m.getInfoForDirections(directions);
		m.printMaze(directions);
		System.out.println(info.getSteps()+ " " + info.getStepsToReachTarget());
		directions = new Direction[w];
		for (int i = 0; i < directions.length; i++) { 
			directions[i] = Direction.RIGHT;
		}
		info = m.getInfoForDirections(directions);
		m.printMaze(directions);
		System.out.println(info.getSteps() + " " +info.getStepsToReachTarget());

	}

}
