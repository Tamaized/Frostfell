package tamaized.frostfell.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;
import tamaized.frostfell.client.ClientUtil;
import tamaized.frostfell.network.client.ClientPacketPortalFormParticles;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class NetworkMessages {

	private static int index = 0;

	@SuppressWarnings("ConstantConditions")
	public static void register(SimpleChannel network) {
		registerMessage(network, ClientPacketPortalFormParticles.class, () -> new ClientPacketPortalFormParticles(BlockPos.ZERO));
	}

	private static <M extends IMessage<M>> void registerMessage(SimpleChannel network, Class<M> type, Supplier<M> factory) {
		network.registerMessage(index++, type, IMessage::encode, p -> IMessage.decode(p, factory), IMessage::onMessage);
	}

	public interface IMessage<SELF extends IMessage<SELF>> {

		static <M extends IMessage<M>> void encode(M message, FriendlyByteBuf packet) {
			message.toBytes(packet);
		}

		static <M extends IMessage<M>> M decode(FriendlyByteBuf packet, Supplier<M> factory) {
			return factory.get().fromBytes(packet);
		}

		static void onMessage(IMessage message, Supplier<NetworkEvent.Context> context) {
			context.get().enqueueWork(() -> message.handle(getSidedPlayer(context.get().getSender())));
			context.get().setPacketHandled(true);
		}

		@Nullable
		static Player getSidedPlayer(@Nullable Player test) {
			return test == null ? ClientUtil.getClientPlayerSafely() : test;
		}

		void handle(@Nullable Player sup);

		void toBytes(FriendlyByteBuf packet);

		SELF fromBytes(FriendlyByteBuf packet);

	}
}
