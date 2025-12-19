package com.github.anopensaucedev.fasterrandom.mixin;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(WorldgenRandom.class)
public class ChunkRandomMixin {

	/**
	 * @author AnOpenSauceDev
	 * @reason Don't optimize slime chunks, as it shuffles the placement.
	 */
	@Overwrite
	public static RandomSource seedSlimeChunk(int chunkX, int chunkZ, long worldSeed, long scrambler) {
		return new LegacyRandomSource(
				worldSeed + (long)(chunkX * chunkX * 4987142) + (long)(chunkX * 5947611) + (long)(chunkZ * chunkZ) * 4392871L + (long)(chunkZ * 389711) ^ scrambler
		);
	}

}
