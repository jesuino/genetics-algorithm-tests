package org.fxapps.genetics;

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
public class SentenceWriter {

	final static String SENTENCE = "Be or not to be?";
	private static String POSSIBLE_CHARS;

	static Random r = new Random();

	public static Character generateRandomString() {
		POSSIBLE_CHARS = " abcdefghijklmnopqrstuvxzwyABCDEFGHIJKLMNOPQRSTUVXZWY!?.,'";
		int i = r.nextInt(POSSIBLE_CHARS.length());
		return POSSIBLE_CHARS.charAt(i);
	}

	private static Integer fitness(Genotype<AnyGene<Character>> ch) {
		int sum = 0;
		for (int i = 0; i < SENTENCE.length(); i++) {
			char target = SENTENCE.charAt(i);
			char allele = ch.getChromosome().getGene(i).getAllele();
			if (allele == target) {
				sum += 100;
			}
			sum += POSSIBLE_CHARS.length() - Math.abs(target - allele);
		}
		return sum;

	}

	public static void main(String[] args) {

		Factory<Genotype<AnyGene<Character>>> gtf = Genotype
				.of(AnyChromosome.of(SentenceWriter::generateRandomString, SENTENCE.length()));
		final Engine<AnyGene<Character>, Integer> engine = Engine.builder(SentenceWriter::fitness, gtf)
				.offspringSelector(new RouletteWheelSelector<>()).build();

		Genotype<AnyGene<Character>> result = engine.stream().limit(50000).collect(EvolutionResult.toBestGenotype());

		System.out.println("Selected: " + result);
	}
}
