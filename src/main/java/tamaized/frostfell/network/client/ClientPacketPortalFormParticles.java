package tamaized.frostfell.network.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import tamaized.frostfell.Frostfell;
import tamaized.frostfell.network.NetworkMessages;

import javax.annotation.Nullable;
import java.util.Random;

public class ClientPacketPortalFormParticles implements NetworkMessages.IMessage<ClientPacketPortalFormParticles> {

	private BlockPos pos;

	public ClientPacketPortalFormParticles(BlockPos pos) {
		this.pos = pos;
	}

	@Override
	public void handle(@Nullable Player player) {
		if (player == null || player.level == null || !player.level.isClientSide()) {
			Frostfell.LOGGER.fatal("Warning, client attempted to send malicious packet! ({})", player == null ? "NULL PLAYER" : player.getDisplayName());
			return;
		}
		Random rand = player.level.getRandom();
		for (int i = 0; i < 100; i++) {
			double xPos = pos.getX() + rand.nextFloat();
			double yPos = pos.getY() + 1D;
			double zPos = pos.getZ() + rand.nextFloat();
			double xSpeed = (rand.nextFloat() * 0.25D) - 0.125D;
			double ySpeed = rand.nextFloat() * 0.5D;
			double zSpeed = (rand.nextFloat() * 0.25D) - 0.125D;
			player.level.addParticle(ParticleTypes.SNOWFLAKE, xPos, yPos, zPos, xSpeed, ySpeed, zSpeed);
		}
	}

	@Override
	public void toBytes(FriendlyByteBuf packet) {
		packet.writeBlockPos(pos);
	}

	@Override
	public ClientPacketPortalFormParticles fromBytes(FriendlyByteBuf packet) {
		pos = packet.readBlockPos();
		return this;
	}

}
