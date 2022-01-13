package tamaized.frostfell.client;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import tamaized.frostfell.Frostfell;

import java.io.IOException;
import java.util.function.Consumer;

public class ClientListener {

	public static void init() {
		IEventBus busMod = FMLJavaModLoadingContext.get().getModEventBus();
		IEventBus busForge = MinecraftForge.EVENT_BUS;
		TintHandler.setup(busMod);
		ModelBakeListener.init(busMod);
		MinecraftForgeClient.registerTextureAtlasSpriteLoader(new ResourceLocation(Frostfell.MODID, "portal"),
				(atlas, resourceManager, textureInfo, resource, atlasWidth, atlasHeight, spriteX, spriteY, mipmapLevel, image) -> {
					Resource r = null;
					try {
						r = resourceManager.getResource(new ResourceLocation("textures/block/nether_portal.png"));
						NativeImage nativeimage = NativeImage.read(r.getInputStream());
						for (int x = 0; x < nativeimage.getWidth(); x++) {
							for (int y = 0; y < nativeimage.getHeight(); y++) {
								int pixel = nativeimage.getPixelRGBA(x, y);
								int L = (int) (0.299D * ((pixel) & 0xFF) + 0.587D * (0xDD) + 0.114D * ((pixel >> 16) & 0xFF));
								nativeimage.setPixelRGBA(x, y, NativeImage.combine((pixel >> 24) & 0xFF, L, L, L));
							}
						}
						image = nativeimage;
					} catch (Throwable throwable1) {
						if (r != null) {
							try {
								r.close();
							} catch (Throwable throwable) {
								throwable1.addSuppressed(throwable);
							}
						}

						throwable1.printStackTrace();
					}
					if (r != null) {
						try {
							r.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					return new TextureAtlasSprite(atlas, textureInfo, mipmapLevel, atlasWidth, atlasHeight, spriteX, spriteY, image) {
					};
				});
		busMod.addListener((Consumer<TextureStitchEvent.Pre>) event -> {
			event.addSprite(new ResourceLocation(Frostfell.MODID, "blocks/portal"));
		});
	}

}
