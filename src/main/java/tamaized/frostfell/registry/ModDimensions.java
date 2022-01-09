package tamaized.frostfell.registry;

import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
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
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;
import tamaized.frostfell.Frostfell;
import tamaized.frostfell.world.ChunkGeneratorFrostfell;
import tamaized.frostfell.world.DimensionFrostfell;
import tamaized.frostfell.world.FrostfellBiomeProvider;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Frostfell.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModDimensions {

	@ObjectHolder(Frostfell.MODID + ":frostfell")
	public static final ModDimension DIMENSION_FROSTFELL = getNull();
	@ObjectHolder(Frostfell.MODID + ":chunkgen")
	public static final ChunkGeneratorType<OverworldGenSettings, ChunkGeneratorFrostfell> CHUNKGENTYPE_FROSTFELL = getNull();
	@ObjectHolder(Frostfell.MODID + ":biomeprovider")
	public static final BiomeProviderType<OverworldBiomeProviderSettings, FrostfellBiomeProvider> BIOMEPROVIDER_FROSTFELL = getNull();
	private static DimensionType TYPE_FROSTFELL_FUTURE;
	public static final Supplier<DimensionType> TYPE_FROSTFELL = () -> TYPE_FROSTFELL_FUTURE;

	public static void register() {
		TYPE_FROSTFELL_FUTURE = DimensionManager.registerDimension(new ResourceLocation(Frostfell.MODID, "dimension"), Objects.requireNonNull(ModDimensions.DIMENSION_FROSTFELL), new PacketBuffer(Unpooled.buffer()), true);
	}

	private static <T> T getNull() {
		return null;
	}


	private static <T extends IForgeRegistryEntry<T>> T make(String name, T entry) {
		return entry.setRegistryName(new ResourceLocation(Frostfell.MODID, name));
	}

	@SubscribeEvent
	public static void registerChunkGeneratorTypes(RegistryEvent.Register<ChunkGeneratorType<?, ?>> event) {
		event.getRegistry().registerAll(

				make("chunkgen", new ChunkGeneratorType<>(ChunkGeneratorFrostfell::new, false, OverworldGenSettings::new))

		);
	}

	@SubscribeEvent
	public static void registerBiomeProviderTypes(RegistryEvent.Register<BiomeProviderType<?, ?>> event) {
		event.getRegistry().registerAll(

				make("biomeprovider", new BiomeProviderType<>(FrostfellBiomeProvider::new, OverworldBiomeProviderSettings::new))

		);
	}

	@SubscribeEvent
	public static void registerDimensions(RegistryEvent.Register<ModDimension> event) {
		event.getRegistry().registerAll(

				make("frostfell", new ModDimension() {
					@Override
					public BiFunction<World, DimensionType, ? extends Dimension> getFactory() {
						return DimensionFrostfell::new;
					}
				})

		);
	}

}
