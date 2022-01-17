package tamaized.frostfell.registry;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import tamaized.frostfell.blocks.Portal;
import tamaized.regutil.RegUtil;
import tamaized.regutil.RegistryClass;

public class ModBlocks implements RegistryClass {

	private static final DeferredRegister<Block> REGISTRY = RegUtil.create(ForgeRegistries.BLOCKS);

	public static final RegistryObject<Portal> PORTAL = REGISTRY.register("portal", () -> new Portal(Block.Properties.of(Material.AMETHYST, MaterialColor.COLOR_CYAN).
			strength(-1.0F, 3600000.0F).
			noDrops().
			isValidSpawn((state, level, pos, entity) -> false).
			sound(SoundType.AMETHYST)));
	public static final RegistryObject<Block> ICYSTONE = REGISTRY.register("icystone", () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.STONE).
			strength(1.5F, 6.0F).
			requiresCorrectToolForDrops().
			sound(SoundType.STONE)));

	@Override
	public void init(IEventBus bus) {

	}

}
