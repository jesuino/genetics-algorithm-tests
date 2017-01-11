package org.fxapps.genetics.maze;

import org.jenetics.AnyGene;
import org.jenetics.Genotype;

/**
 * 
 * Will learn how to write the sentence you chose
 * 
 * @author wsiqueir
 *
 */
public class MazeApp {

	private static final int POPULATION = 50;
	private static int MAZE_WIDTH = 60;
	private static int MAZE_HEIGHT = 20;
	private static int NUMBER_OF_BLOCKS = 145;
	
	// increase the maze size and the number of blocks and 
	// you may have to increase the evolution stream limit and number of genes
	private static int EVOLUTION_STREAM_LIMIT = 2000;
	// high number of genes may lead to not best solutions
	private static int NUMBER_OF_GENES = (MAZE_WIDTH + MAZE_HEIGHT) *  4;

	static Maze maze;

	public static void main(String[] args) {
		Maze maze = new Maze(MAZE_WIDTH, MAZE_HEIGHT, NUMBER_OF_BLOCKS);
		MazeSolver solver = new MazeSolver(maze);
		Genotype<AnyGene<Direction>> chromosomes = solver.newGenotype(NUMBER_OF_GENES);
		System.out.println("\t Initial Maze");
		maze.printMaze(MazeSolver.getDirections(chromosomes));
		Direction[] evolvedDirections = solver.evolveToDirections(chromosomes, EVOLUTION_STREAM_LIMIT, POPULATION);
		System.out.println("\t Evolved Maze");
		maze.printMaze(evolvedDirections);
	
	}

	

}
