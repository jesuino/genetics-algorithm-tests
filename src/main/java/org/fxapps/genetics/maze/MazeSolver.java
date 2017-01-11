package org.fxapps.genetics.maze;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.jenetics.AnyChromosome;
import org.jenetics.AnyGene;
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

	public Genotype<AnyGene<Direction>> newGenotype(int numberOfGenes, int numberOfChromosomes) {
		int randomBase = numberOfGenes - numberOfGenes / 10;
		List<AnyChromosome<Direction>> chromosomes = IntStream.range(0, numberOfChromosomes)
				.map(i -> numberOfGenes - random.nextInt(randomBase))
				.mapToObj(i -> AnyChromosome.of(this::generateRandomDirection, i))
				.collect(Collectors.toList());
		return Genotype.of(chromosomes);
	}

	public Genotype<AnyGene<Direction>> newGenotype(int numberOfGenes) {
		return Genotype.of(AnyChromosome.of(this::generateRandomDirection, numberOfGenes));
	}

	public Direction[] evolveToDirections(Genotype<AnyGene<Direction>> genotype, int limit) {
		Genotype<AnyGene<Direction>> result = evolveToGenotype(genotype, limit);
		return Utils.getDirections(result.getChromosome());
	}

	public Genotype<AnyGene<Direction>> evolveToGenotype(Genotype<AnyGene<Direction>> genotype, int limit) {
		final Engine<AnyGene<Direction>, Integer> engine = Engine.builder(this::fitness, genotype)
				.offspringSelector(new RouletteWheelSelector<>())
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
		Direction[] directions = Utils.getDirections(dir.getChromosome());
		MazeWalkingInfo info = maze.getInfoForDirections(directions);
		sum -= info.getStepsToReachTarget();
		sum -= info.getSteps();
		sum += info.getStepsToReachTarget() == 0 ? Math.abs(sum) + 1 : 0;
		if(info.getStepsToReachTarget() == 0 && info.getSteps() < directions.length) {
			sum += sum;
		}
		return sum;

	}

}
