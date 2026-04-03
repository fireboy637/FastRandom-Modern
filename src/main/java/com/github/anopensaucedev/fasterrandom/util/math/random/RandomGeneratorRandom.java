package com.github.anopensaucedev.fasterrandom.util.math.random;

import com.google.common.annotations.VisibleForTesting;
import org.jetbrains.annotations.NotNull;

import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.BitRandomSource;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;
import org.jspecify.annotations.NonNull;

public class RandomGeneratorRandom implements BitRandomSource {
	private static final @NotNull RandomGeneratorFactory<RandomGenerator.SplittableGenerator> RANDOM_GENERATOR_FACTORY = RandomGeneratorFactoryUtil.getRandomGeneratorFactory();
	private static final int MODULUS_BITS = 48;
	private static final long MODULUS_MASK = 281474976710655L;
	private static final long MULTIPLIER = 25214903917L;
	private static final long INCREMENT = 11L;

	private long seed;
	private RandomGenerator.SplittableGenerator randomGenerator;

	public RandomGeneratorRandom(long seed) {
		setSeed(seed);
		this.randomGenerator = RANDOM_GENERATOR_FACTORY.create(seed);
	}

	@Override
	public @NonNull RandomSource fork() {
		return new RandomGeneratorRandom(this.nextLong());
	}

	@Override
	public @NonNull PositionalRandomFactory forkPositional() {
		return new Splitter(this.nextLong());
	}

	@Override
	public void setSeed(long seed) {
		this.seed = (seed ^ MULTIPLIER) & MODULUS_MASK;
		this.randomGenerator = RANDOM_GENERATOR_FACTORY.create(this.seed);
	}

	@Override
	public int next(int bits) {
		long newSeed = seed * MULTIPLIER + INCREMENT & MODULUS_MASK;
		this.seed = newSeed;
		// >>> instead of Mojang's >> fixes MC-239059
		return (int) (newSeed >>> MODULUS_BITS - bits);
	}

	@Override
	public int nextInt() {
		return randomGenerator.nextInt();
	}

	@Override
	public int nextInt(int bound) {
		return randomGenerator.nextInt(bound);
	}

	@Override
	public long nextLong() {
		return randomGenerator.nextLong();
	}

	@Override
	public boolean nextBoolean() {
		return randomGenerator.nextBoolean();
	}

	@Override
	public float nextFloat() {
		return randomGenerator.nextFloat();
	}

	@Override
	public double nextDouble() {
		return randomGenerator.nextDouble();
	}

	@Override
	public double nextGaussian() {
		return randomGenerator.nextGaussian();
	}

	private record Splitter(long seed) implements PositionalRandomFactory {
		@SuppressWarnings("deprecation")
		@Override
		public @NotNull RandomSource at(int x, int y, int z) {
			return new RandomGeneratorRandom(Mth.getSeed(x, y, z) ^ this.seed);
		}

		@Override
		public @NotNull RandomSource fromHashOf(@NotNull String seed) {
			return new RandomGeneratorRandom((long) seed.hashCode() ^ this.seed);
		}

		@Override
		public @NonNull RandomSource fromSeed(long seed) {
			return new RandomGeneratorRandom(seed);
		}

		@Override
		@VisibleForTesting
		public void parityConfigString(@NotNull StringBuilder info) {
			info.append("RandomGeneratorRandom$Splitter{").append(this.seed).append("}");
		}
	}
}
