package tamaized.frostfell.asm;

import com.mojang.datafixers.DataFixer;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.MappedRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import tamaized.frostfell.client.ModelBakeListener;

import java.util.Optional;

@SuppressWarnings({"JavadocReference", "unused", "RedundantSuppression"})
public class ASMHooks {

	public static long SEED;

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.world.level.levelgen.WorldGenSettings#WorldGenSettings(long, boolean, boolean, MappedRegistry, Optional)} <br>
	 * [BEFORE FIRST PUTFIELD]
	 */
	public static long seed(long seed) {
		SEED = seed;
		return seed;
	}

	/**
	 * Injection Point:<br>
	 * {@link LevelStorageSource#readWorldGenSettings(Dynamic, DataFixer, int)}<br>
	 * [BEFORE FIRST ASTORE]
	 */
	public static Dynamic<Tag> seed(Dynamic<Tag> seed) {
		SEED = ((CompoundTag) seed.getValue()).getLong("seed");
		return seed;
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.resources.model.ModelBakery#processLoading(ProfilerFiller, int)}<br>
	 * [BEFORE GETSTATIC {@link net.minecraft.core.Registry#ITEM)]
	 */
	@OnlyIn(Dist.CLIENT)
	public static void redirectModels() {
		try {
			ModelBakeListener.redirectModels();
		} catch (NullPointerException e) {
			// Another mod crashed earlier on, this will throw a NPE when the registry isnt populated, just fail silently and let the game error properly later
		}
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.resources.model.ModelBakery#processLoading(ProfilerFiller, int)}<br>
	 * [BEFORE INVOKESTATIC {@link com.google.common.collect.Sets#newLinkedHashSet()}]
	 */
	@OnlyIn(Dist.CLIENT)
	public static void cleanModels() {
		ModelBakeListener.clearOldModels();
	}

}
