package io.github.axolotlclient.AxolotlClientConfig.impl.mixin;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

import io.github.axolotlclient.AxolotlClientConfig.impl.ui.ConfigUIImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.pack.BuiltInTexturePack;
import net.minecraft.client.resource.pack.TexturePack;
import net.minecraft.client.resource.pack.TexturePacks;
import net.minecraft.resource.Identifier;
import net.ornithemc.osl.resource.loader.api.ModTexturePack;
import net.ornithemc.osl.resource.loader.impl.BuiltInModTexturePack;
import net.ornithemc.osl.resource.loader.impl.ResourceLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TexturePacks.class)
public abstract class ResourceLoaderMixin {

	@Shadow
	public abstract List<?> getAvailable();

	@Inject(method = "reload", at = @At("TAIL"))
	private void onTextureReloadTail(CallbackInfo ci){
		ConfigUIImpl.getInstance().preReload();
		String path = new Identifier(ConfigUIImpl.getInstance().getUiJsonPath()).toString();

		for (ModTexturePack pack : ResourceLoader.getDefaultModResourcePacks()){
			try (InputStream in = pack.getResource(path)){
				if (in != null){
					ConfigUIImpl.getInstance().read(in);
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		ConfigUIImpl.getInstance().postReload();
	}
}
