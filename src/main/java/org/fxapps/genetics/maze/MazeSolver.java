package org.fxapps.genetics.maze;

import java.util.Random;

import org.jenetics.AnyChromosome;
import org.jenetics.AnyGene;
import org.jenetics.Genotype;
import org.jenetics.RouletteWheelSelector;
import org.jenetics.engine.Engine;
import org.jenetics.engine.EvolutionResult;
import org.jenetics.util.Factory;

/**
 * 
 * Will learn how to write the sentence you chose
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

	public AnyChromosome<Direction> newChromosomes(int numberOfGenes) {
		return AnyChromosome.of(this::generateRandomDirection, numberOfGenes);
	}
	
	public Factory<Genotype<AnyGene<Direction>>>  newGenotype(int numberOfGenes) {
		return Genotype.of(AnyChromosome.of(this::generateRandomDirection, numberOfGenes));
	}

	public Direction[] evolve(AnyChromosome<Direction> chromosome, int limit) {
		final Engine<AnyGene<Direction>, Integer> engine = Engine.builder(this::fitness, chromosome)
				.offspringSelector(new RouletteWheelSelector<>()).build();
		Genotype<AnyGene<Direction>> result = engine.stream().limit(limit).collect(EvolutionResult.toBestGenotype());
		return Utils.getDirectionsFromChromosome(result.getChromosome());
	}

	private Direction generateRandomDirection() {
		int totalDirections = Direction.values().length;
		int i = random.nextInt(totalDirections);
		return Direction.values()[i];
	}

	private Integer fitness(Genotype<AnyGene<Direction>> dir) {
		int sum = 0;
		Direction[] directions = Utils.getDirectionsFromChromosome(dir.getChromosome());
		MazeWalkingInfo info = maze.getInfoForDirections(directions);
		sum += maze.width + maze.height - info.getStepsToReachTarget();
		sum += info.getStepsToReachTarget() == 0 ? 1 : 0;
		sum += maze.width + maze.height - info.getSteps();
		return sum;

	}

}
