package tamaized.frostfell.common.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class CommandFrostfell extends CommandBase {

	@Override
	public String getName() {
		return "frostfell";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.frostfell.usage";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length > 0) {
			try {
				SubCommand.valueOf(args[0].toUpperCase(Locale.ROOT)).execute(this, server, sender, args);
			} catch (IllegalArgumentException e) {
				throw new WrongUsageException(getUsage(sender));
			}
		} else {
			throw new CommandException(getUsage(sender));
		}
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
		if (args.length == 1)
			return getListOfStringsMatchingLastWord(args, SubCommand.COMMAND_LIST);

		if (args.length > 1) {
			try {
				String[] argsMoved = Arrays.copyOfRange(args, 1, args.length);

				return SubCommand.valueOf(args[0].toUpperCase(Locale.ROOT)).tabCompletion(server, sender, argsMoved, targetPos);
			} catch (Throwable e) {
				return Collections.emptyList();
			}
		}

		return Collections.emptyList();
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		try {
			return SubCommand.valueOf(args[0].toUpperCase(Locale.ROOT)).isUsernameIndex(args, index);
		} catch (Throwable t) {
			return false;
		}
	}

	private enum SubCommand {
		WEATHER {

			private final String[] TAB_LIST = {"clear", "rain", "thunder"};

			@Override
			protected void execute(ICommand command, MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
				if (args.length >= 2) {
					World world = sender.getEntityWorld();
					WorldInfo info = world.getWorldInfo();
					int i = (300 + (new Random()).nextInt(600)) * 20;
					switch (args[1].toLowerCase()) {
						case "clear":
							info.setCleanWeatherTime(i);
							info.setRainTime(0);
							info.setThunderTime(0);
							info.setRaining(false);
							info.setThundering(false);
							notifyCommandListener(sender, command, "commands.weather.clear");
							break;
						case "rain":
							info.setCleanWeatherTime(0);
							info.setRainTime(i);
							info.setThunderTime(i);
							info.setRaining(true);
							info.setThundering(false);
							notifyCommandListener(sender, command, "commands.weather.rain");
							break;
						case "thunder":
							info.setCleanWeatherTime(0);
							info.setRainTime(i);
							info.setThunderTime(i);
							info.setRaining(true);
							info.setThundering(true);
							notifyCommandListener(sender, command, "commands.weather.thunder");
							break;
						default:
							throw new WrongUsageException("commands.frostfell.weather.usage");
					}
				} else if (args.length == 1) {
					throw new CommandException("commands.frostfell.weather.usage");
				} else {
					throw new CommandException("commands.frostfell.usage");
				}
			}

			@Override
			protected List<String> tabCompletion(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
				return args.length == 1 ? getListOfStringsMatchingLastWord(args, TAB_LIST) : Collections.emptyList();
			}
		};

		private final static String[] COMMAND_LIST;

		static {
			int length = SubCommand.values().length;

			String[] list = new String[length];

			for (SubCommand action : SubCommand.values())
				list[action.ordinal()] = action.toString().toLowerCase(Locale.ROOT);

			COMMAND_LIST = list;
		}

		protected abstract void execute(ICommand command, MinecraftServer server, ICommandSender sender, String[] args) throws CommandException;

		protected List<String> tabCompletion(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
			return Collections.emptyList();
		}

		protected boolean isUsernameIndex(String[] args, int index) {
			return false;
		}
	}
}
