package io.github.axolotlclient.AxolotlClientConfig.impl.mixin;

import java.io.IOException;
import java.util.Properties;

import net.minecraft.locale.LanguageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LanguageManager.class)
public class LanguageManagerMixin {

	@Shadow
	private Properties translations;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void print(CallbackInfo ci){
		try {
			translations.load(this.getClass().getResourceAsStream("/assets/axolotlclientconfig/lang/en_US.lang"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
