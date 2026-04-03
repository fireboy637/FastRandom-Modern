package com.github.anopensaucedev.fasterrandom.mixin;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.SingleThreadedRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(WorldgenRandom.class)
public class WorldgenRandomMixin {

	/**
	 * @author AnOpenSauceDev
	 * @reason Don't optimize slime chunks, as it shuffles the placement.
	 */
	@SuppressWarnings("IntegerMultiplicationImplicitCastToLong")
	@Overwrite
	public static RandomSource seedSlimeChunk(int x, int z, long seed, long salt) {
		return new SingleThreadedRandomSource(
				seed + (long)(x * x * 4987142) + (long)(x * 5947611) + (long)(z * z) * 4392871L + (long)(z * 389711) ^ salt
		);
	}

}
