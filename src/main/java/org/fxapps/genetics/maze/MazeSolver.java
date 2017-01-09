package org.fxapps.genetics.maze;

import java.util.Random;

import org.jenetics.AnyChromosome;
import org.jenetics.AnyGene;
import org.jenetics.Chromosome;
import org.jenetics.Genotype;
import org.jenetics.RouletteWheelSelector;
import org.jenetics.engine.Engine;
import org.jenetics.engine.EvolutionResult;

/**
 * 
 * Will learn how to write the sentence you chose
 * 
 * @author wsiqueir
 *
 */
public class MazeSolver {

	private static int MAZE_WIDTH = 100;
	private static int MAZE_HEIGHT = 40;
	private static int NUMBER_OF_BLOCKS = 80;
	
	// increase the maze size and the number of blocks and 
	// you may have to increase the population and number of genes
	private static int POPULATION = 5000;
	// high number of genes may lead to not best solutions
	private static int NUMBER_OF_GENES = (MAZE_WIDTH + MAZE_HEIGHT) * 2;

	static Random r = new Random();

	static Maze maze;

	public static void main(String[] args) {
		// change the number of blocks to find a faster solution.
		// if you want a really big maze with a high number of blocks you must
		// increase the cromosome size (number of genes) and the population
		maze = new Maze(MAZE_WIDTH, MAZE_HEIGHT, NUMBER_OF_BLOCKS);

		// each gene is a direction - you may want to increase the number of
		// genes

		AnyChromosome<Direction> chromosome = AnyChromosome.of(MazeSolver::generateRandomDirection, NUMBER_OF_GENES);

		System.out.println(" == INITIAL DIRECTIONS ==");
		maze.printMaze(getDirectionsFromChromosome(chromosome));

		final Engine<AnyGene<Direction>, Integer> engine = Engine.builder(MazeSolver::fitness, chromosome)
				.offspringSelector(new RouletteWheelSelector<>()).build();

		System.out.println(" == FINAL DIRECTIONS==");
		// you may want to increase the population if you increase the maze size
		Genotype<AnyGene<Direction>> result = engine.stream().limit(POPULATION)
				.collect(EvolutionResult.toBestGenotype());

		maze.printMaze(getDirectionsFromChromosome(result.getChromosome()));
	}

	public static Direction generateRandomDirection() {
		int totalDirections = Direction.values().length;
		int i = r.nextInt(totalDirections);
		return Direction.values()[i];
	}

	private static Integer fitness(Genotype<AnyGene<Direction>> dir) {
		int sum = 0;
		Direction[] directions = getDirectionsFromChromosome(dir.getChromosome());
		MazeWalkingInfo info = maze.getInfoForDirections(directions);
		sum += maze.width + maze.height - info.getStepsToReachTarget();
		sum += info.getStepsToReachTarget() == 0 ? 1 : 0;
		sum += maze.width + maze.height - info.getSteps();
		return sum;

	}

	private static Direction[] getDirectionsFromChromosome(Chromosome<AnyGene<Direction>> chromosome) {
		Direction[] directions = new Direction[chromosome.length()];
		for (int i = 0; i < directions.length; i++) {
			directions[i] = chromosome.getGene(i).getAllele();
		}
		return directions;
	}

}
