package com.github.anopensaucedev.fasterrandom.mixin;

import com.github.anopensaucedev.fasterrandom.util.math.random.RandomGeneratorRandom;
import io.netty.util.internal.ThreadLocalRandom;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.RandomSupport;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RandomSource.class)
public interface RandomSourceMixin {
	@Inject(method = "create(J)Lnet/minecraft/util/RandomSource;", at = @At(value = "HEAD"), cancellable = true)
	private static void fasterrandom$createInject(long seed, @NotNull CallbackInfoReturnable<RandomSource> cir) {
		cir.setReturnValue(new RandomGeneratorRandom(seed));
	}

	@SuppressWarnings("deprecation")
	@Inject(method = "createThreadLocalInstance()Lnet/minecraft/util/RandomSource;", at = @At(value = "HEAD"), cancellable = true)
	private static void fasterrandom$createLocalInject(@NotNull CallbackInfoReturnable<RandomSource> cir) {
		cir.setReturnValue(new RandomGeneratorRandom(ThreadLocalRandom.current().nextLong()));
	}

	@Inject(method = "createThreadLocalInstance(J)Lnet/minecraft/util/RandomSource;", at = @At(value = "HEAD"), cancellable = true)
	private static void fasterrandom$createLocalInject(long seed, @NotNull CallbackInfoReturnable<RandomSource> cir) {
		cir.setReturnValue(new RandomGeneratorRandom(seed));
	}

	@Inject(method = "createThreadSafe", at = @At(value = "HEAD"), cancellable = true)
	private static void fasterrandom$createThreadSafeInject(@NotNull CallbackInfoReturnable<RandomSource> cir) {
		cir.setReturnValue(new RandomGeneratorRandom(RandomSupport.generateUniqueSeed()));
	}
}
