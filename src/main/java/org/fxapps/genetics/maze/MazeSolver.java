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
 * Utility methods to escape the given maze
 * 
 * @author wsiqueir
 *
 */
public class MazeSolver {

	static Random random = new Random();

	private Maze maze;

	public MazeSolver(Maze maze) {
		this.maze = maze;
	}


	public Genotype<AnyGene<Direction>> newGenotype(int numberOfGenes) {
		return Genotype.of(AnyChromosome.of(this::generateRandomDirection, numberOfGenes));
	}

	public Direction[] evolveToDirections(Genotype<AnyGene<Direction>> genotype, int limit, int population) {
		Genotype<AnyGene<Direction>> result = evolveToGenotype(genotype, limit, population);
		return getDirections(result.getChromosome());
	}

	public Genotype<AnyGene<Direction>> evolveToGenotype(Genotype<AnyGene<Direction>> genotype, int limit, int population) {
		final Engine<AnyGene<Direction>, Integer> engine = Engine.builder(this::fitness, genotype)
				.offspringSelector(new RouletteWheelSelector<>())
				.populationSize(population)
				.build();
		return engine.stream().limit(limit).collect(EvolutionResult.toBestGenotype());
	}

	private Direction generateRandomDirection() {
		int totalDirections = Direction.values().length;
		int i = random.nextInt(totalDirections);
		return Direction.values()[i];
	}

	private Integer fitness(Genotype<AnyGene<Direction>> dir) {
		int sum = 0;
		Direction[] directions = getDirections(dir.getChromosome());
		MazeWalkingInfo info = maze.getInfoForDirections(directions);
		sum -= info.getStepsToReachTarget();
		sum -= info.getSteps();
		sum += info.getStepsToReachTarget() == 0 ? Math.abs(sum) + 1 : 0;
		if(info.getStepsToReachTarget() == 0 && info.getSteps() < directions.length) {
			sum += sum;
		}
		return sum;

	}
	
	public static Direction[] getDirections(Genotype<AnyGene<Direction>> genotype) {
		return getDirections(genotype.getChromosome());
	}

	public static Direction[] getDirections(Chromosome<AnyGene<Direction>> chromosome) {
		Direction[] directions = new Direction[chromosome.length()];
		for (int i = 0; i < directions.length; i++) {
			directions[i] = chromosome.getGene(i).getAllele();
		}
		return directions;
	}

}
