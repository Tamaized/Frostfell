package tamaized.frostfell;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tamaized.frostfell.client.ClientInitiator;
import tamaized.frostfell.network.NetworkMessages;
import tamaized.frostfell.registry.ModBlocks;
import tamaized.frostfell.registry.ModFeatures;
import tamaized.frostfell.registry.ModItems;
import tamaized.frostfell.world.FrostfellChunkGenerator;
import tamaized.frostfell.world.FrostfellSeededBiomeProvider;
import tamaized.regutil.RegUtil;

import java.util.function.Consumer;

@Mod(Frostfell.MODID)
public class Frostfell {

	public static final String MODID = "frostfell";

	public static final Logger LOGGER = LogManager.getLogger(MODID);

	public static final SimpleChannel NETWORK = NetworkRegistry.ChannelBuilder.
			named(new ResourceLocation(MODID, MODID)).
			clientAcceptedVersions(s -> true).
			serverAcceptedVersions(s -> true).
			networkProtocolVersion(() -> "1").
			simpleChannel();

	public static final ResourceKey<Level> DIMENSION = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(MODID, MODID));

	public Frostfell() {
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		IEventBus forgeBus = MinecraftForge.EVENT_BUS;

		DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientInitiator::call);
		RegUtil.setup(MODID, () -> ModItems.PERMAFROST, modBus,
				ModBlocks::new,
				ModItems::new,
				ModFeatures::new
		);

		Registry.register(Registry.BIOME_SOURCE, MODID + ":biomeprovider", FrostfellSeededBiomeProvider.CODEC);

		modBus.addListener((Consumer<FMLCommonSetupEvent>) event -> {
			NetworkMessages.register(NETWORK);
			Registry.register(Registry.CHUNK_GENERATOR, new ResourceLocation(MODID, MODID), FrostfellChunkGenerator.codec);
		});

		forgeBus.addListener((Consumer<TickEvent.PlayerTickEvent>) event -> {
			Level level = event.player.level;
			if (!level.isClientSide() && !event.player.isSpectator() && event.phase == TickEvent.Phase.END && event.player.tickCount % 20 == 0)
				if (level.dimension().equals(Level.OVERWORLD) || level.dimension().equals(DIMENSION))
					for (ItemEntity item : level.getEntitiesOfClass(ItemEntity.class, event.player.getBoundingBox().inflate(32)))
						if (item.getItem().is(ModItems.PERMAFROST.get()) && ModBlocks.PORTAL.get().canFormPortal(level.getBlockState(item.blockPosition())))
							ModBlocks.PORTAL.get().tryToCreatePortal(level, item.blockPosition(), item, event.player);
		});

	}

	public static boolean checkForDimension(Level world) {
		return world.dimension().location().equals(DIMENSION.location());
	}

}
