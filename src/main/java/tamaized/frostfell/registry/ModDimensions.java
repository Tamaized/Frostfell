package tamaized.frostfell.registry;

import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.provider.BiomeProviderType;
import net.minecraft.world.biome.provider.OverworldBiomeProviderSettings;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGeneratorType;
import net.minecraft.world.gen.OverworldGenSettings;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import tamaized.frostfell.Frostfell;
import tamaized.frostfell.world.ChunkGeneratorFrostfell;
import tamaized.frostfell.world.DimensionFrostfell;
import tamaized.frostfell.world.FrostfellBiomeProvider;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Frostfell.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModDimensions {

	@ObjectHolder(Frostfell.MODID + ":frostfell")
	public static final ModDimension DIMENSION_FROSTFELL = getNull();
	public static final ChunkGeneratorType<OverworldGenSettings, ChunkGeneratorFrostfell> CHUNKGENTYPE_FROSTFELL = ChunkGeneratorType.register(

			makeName("chunkgen"),

			ChunkGeneratorFrostfell::new,

			OverworldGenSettings::new,

			false

	);
	public static final BiomeProviderType<OverworldBiomeProviderSettings, FrostfellBiomeProvider> BIOMEPROVIDER_FROSTFELL = BiomeProviderType.register(

			makeName("biomeprovider"),

			FrostfellBiomeProvider::new,

			OverworldBiomeProviderSettings::new

	);
	private static DimensionType TYPE_FROSTFELL_FUTURE;
	public static final Supplier<DimensionType> TYPE_FROSTFELL = () -> TYPE_FROSTFELL_FUTURE;

	public static void register() {
		TYPE_FROSTFELL_FUTURE = DimensionManager.registerDimension(new ResourceLocation(Frostfell.MODID, "dimension"), Objects.requireNonNull(ModDimensions.DIMENSION_FROSTFELL), new PacketBuffer(Unpooled.buffer()));
	}

	private static <T> T getNull() {
		return null;
	}

	private static String makeName(String name) {
		return Frostfell.MODID + "_" + name;
	}

	@SubscribeEvent
	public static void registerDimensions(RegistryEvent.Register<ModDimension> event) {
		event.getRegistry().registerAll(

				new ModDimension() {
					@Override
					public Function<DimensionType, ? extends Dimension> getFactory() {
						return dimensionType -> new DimensionFrostfell();
					}
				}.setRegistryName(Frostfell.MODID, "frostfell")

		);
	}

}
