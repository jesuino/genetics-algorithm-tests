package org.fxapps.genetics;

import org.jenetics.BitChromosome;
import org.jenetics.BitGene;
import org.jenetics.Genotype;
import org.jenetics.engine.Engine;
import org.jenetics.engine.EvolutionResult;
import org.jenetics.util.Factory;

public final class HelloWorldJenetics {
	// 2.) Definition of the fitness function.
	private static Integer eval(Genotype<BitGene> gt) {
		return gt.getChromosome().as(BitChromosome.class).bitCount();
	}

	public static void main(String[] args) {
		// 1.) Define the genotype (factory) suitable
		// for the problem.
		Factory<Genotype<BitGene>> gtf = Genotype.of(BitChromosome.of(5, 0.1));
		System.out.println(gtf.instances().count());
		// 3.) Create the execution environment.
		Engine<BitGene, Integer> engine = Engine.builder(HelloWorldJenetics::eval, gtf).build();

		// 4.) Start the execution (evolution) and
		// collect the result.
		Genotype<BitGene> result = engine.stream().limit(10000).collect(EvolutionResult.toBestGenotype());

		System.out.println("Hello World:\n" + result);
	}
}