package org.fxapps.genetics.maze;

import org.jenetics.AnyGene;
import org.jenetics.Chromosome;
import org.jenetics.Genotype;

public class Utils {

	private Utils() {
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
