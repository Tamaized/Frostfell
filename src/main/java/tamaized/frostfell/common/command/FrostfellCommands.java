package tamaized.frostfell.common.command;


import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;

import java.util.Random;

public class FrostfellCommands {

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
								context.getSource().sendFeedback(new TextComponentTranslation("commands.weather.clear"), true);
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
								context.getSource().sendFeedback(new TextComponentTranslation("commands.weather.rain"), true);
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
								context.getSource().sendFeedback(new TextComponentTranslation("commands.weather.thunder"), true);
								return 0;
							}));
		}
	}
}
