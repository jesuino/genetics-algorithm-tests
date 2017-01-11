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
public class LargestDigitSum {

	private static Integer fitness(Genotype<LongGene> lt) {
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
		Factory<Genotype<LongGene>> gtf = Genotype.of(LongChromosome.of(1, 100, 3));
		Engine<LongGene, Integer> engine = Engine.builder(LargestDigitSum::fitness, gtf).build();
		Genotype<LongGene> result = engine.stream().limit(10).collect(EvolutionResult.toBestGenotype());
		System.out.println("Selected: " + result +  ". Sum: " + fitness(result));
	}
}