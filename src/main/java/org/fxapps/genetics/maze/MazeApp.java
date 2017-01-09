package org.fxapps.genetics.maze;

import org.jenetics.AnyChromosome;

/**
 * 
 * Will learn how to write the sentence you chose
 * 
 * @author wsiqueir
 *
 */
public class MazeApp {

	private static int MAZE_WIDTH = 40;
	private static int MAZE_HEIGHT = 20;
	private static int NUMBER_OF_BLOCKS = 10;
	
	// increase the maze size and the number of blocks and 
	// you may have to increase the evolution stream limit and number of genes
	private static int EVOLUTION_STREAM_LIMIT = 5000;
	// high number of genes may lead to not best solutions
	private static int NUMBER_OF_GENES = (MAZE_WIDTH + MAZE_HEIGHT);

	static Maze maze;

	public static void main(String[] args) {
		Maze maze = new Maze(MAZE_WIDTH, MAZE_HEIGHT, NUMBER_OF_BLOCKS);
		MazeSolver solver = new MazeSolver(maze);
		AnyChromosome<Direction> chromosomes = solver.newChromosomes(NUMBER_OF_GENES);
		System.out.println("\t Initial Maze");
		maze.printMaze(Utils.getDirectionsFromChromosome(chromosomes));
		Direction[] evolvedDirections = solver.evolve(chromosomes, EVOLUTION_STREAM_LIMIT);
		System.out.println("\t Evolved Maze");
		maze.printMaze(evolvedDirections);
		System.out.println(evolvedDirections.length);
	
	}

	

}
