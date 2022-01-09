package tamaized.frostfell.common.command;


import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.ServerWorld;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import tamaized.frostfell.registry.ModDimensions;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Objects;
import java.util.Random;

public class FrostfellCommands {

	private static Field field_worldTeleporter;

	private FrostfellCommands() {

	}

	public static class Weather {
		public static ArgumentBuilder<CommandSource, ?> register() {
			return Commands.literal("weather").
					requires(cs -> cs.hasPermissionLevel(2)).
					then(Commands.literal("clear").
							executes(context -> {
								World world = context.getSource().getWorld();
								WorldInfo info = world.getWorldInfo();
								int i = (300 + (new Random()).nextInt(600)) * 20;
								info.setClearWeatherTime(i);
								info.setRainTime(0);
								info.setThunderTime(0);
								info.setRaining(false);
								info.setThundering(false);
								context.getSource().sendFeedback(new TranslationTextComponent("commands.weather.clear"), true);
								return 0;
							})).
					then(Commands.literal("rain").
							executes(context -> {
								World world = context.getSource().getWorld();
								WorldInfo info = world.getWorldInfo();
								int i = (300 + (new Random()).nextInt(600)) * 20;
								info.setClearWeatherTime(0);
								info.setRainTime(i);
								info.setThunderTime(i);
								info.setRaining(true);
								info.setThundering(false);
								context.getSource().sendFeedback(new TranslationTextComponent("commands.weather.rain"), true);
								return 0;
							})).
					then(Commands.literal("thunder").
							executes(context -> {
								World world = context.getSource().getWorld();
								WorldInfo info = world.getWorldInfo();
								int i = (300 + (new Random()).nextInt(600)) * 20;
								info.setClearWeatherTime(0);
								info.setRainTime(i);
								info.setThunderTime(i);
								info.setRaining(true);
								info.setThundering(true);
								context.getSource().sendFeedback(new TranslationTextComponent("commands.weather.thunder"), true);
								return 0;
							}));
		}
	}

	public static class Teleport {
		public static ArgumentBuilder<CommandSource, ?> register() {
			return Commands.literal("teleport").
					requires(cs -> cs.hasPermissionLevel(2)).
					executes(context -> {
						try {
							Entity e = Objects.requireNonNull(context.getSource().getEntity());
							if (field_worldTeleporter == null) {
								field_worldTeleporter = ObfuscationReflectionHelper.findField(ServerWorld.class, "field_85177_Q");
								Field field_MODIFIERS = Field.class.getDeclaredField("modifiers");
								field_MODIFIERS.setAccessible(true);
								field_MODIFIERS.setInt(field_worldTeleporter, field_worldTeleporter.getModifiers() & ~Modifier.FINAL);
							}
							DimensionType dst = e.dimension == DimensionType.OVERWORLD ? ModDimensions.TYPE_FROSTFELL.get() : DimensionType.OVERWORLD;
							ServerWorld world = Objects.requireNonNull(e.getServer()).getWorld(dst);
							Teleporter cache = world.getDefaultTeleporter();
							field_worldTeleporter.set(world, new Teleporter(world) {
								@Override
								public boolean func_222268_a(Entity p_222268_1_, float p_222268_2_) {
									return true;
								}
							});
							e.changeDimension(dst);
							field_worldTeleporter.set(world, cache);
						} catch (Throwable e1) {
							e1.printStackTrace();
						}
						return 0;
					});
		}
	}
}
