package io.github.axolotlclient.AxolotlClientConfig.impl.mixin;

import net.minecraft.client.render.texture.TextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(TextureManager.class)
public class TextureManagerMixin {

	@ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/MemoryTracker;createByteBuffer(I)Ljava/nio/ByteBuffer;"))
	private int increaseBufferSize(int size){
		return size*5;
	}
}
