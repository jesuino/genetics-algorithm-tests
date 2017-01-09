package org.fxapps.genetics;

import java.util.concurrent.atomic.AtomicInteger;

import org.jenetics.Genotype;
import org.jenetics.LongChromosome;
import org.jenetics.LongGene;
import org.jenetics.engine.Engine;
import org.jenetics.engine.EvolutionResult;
import org.jenetics.util.Factory;

/**
 * 
 * Select the genotype with greatest algorithms sum
 * 
 * @author wsiqueir
 *
 */
public class LongAlgoritimsSum {

	// 2.) Definition of the fitness function.
	private static Integer eval(Genotype<LongGene> lt) {
		LongChromosome lc = lt.getChromosome().as(LongChromosome.class);

		AtomicInteger sum = new AtomicInteger(0);
		lc.stream().forEach(l -> {
			String str = String.valueOf(l.longValue());
			for (char ch : str.toCharArray()) {
				sum.addAndGet(Integer.parseInt(String.valueOf(ch)));
			}
		});
		System.out.println(lt + ", sum " + sum.get());
		return sum.get();
	}

	public static void main(String[] args) {
		// 1.) Define the genotype (factory) suitable
		// for the problem.
		Factory<Genotype<LongGene>> gtf = Genotype.of(LongChromosome.of(1, 100, 3));
		// 3.) Create the execution environment.
		Engine<LongGene, Integer> engine = Engine.builder(LongAlgoritimsSum::eval, gtf).build();

		// 4.) Start the execution (evolution) and
		// collect the result.
		Genotype<LongGene> result = engine.stream().limit(10).collect(EvolutionResult.toBestGenotype());

		System.out.println("Selected: " + result +  ". Sum: " + eval(result));
	}
}
