package io.github.axolotlclient.AxolotlClientConfig.example.mixin;

import java.util.function.Function;

import io.github.axolotlclient.AxolotlClientConfig.api.AxolotlClientConfig;
import io.github.axolotlclient.AxolotlClientConfig.api.manager.ConfigManager;
import io.github.axolotlclient.AxolotlClientConfig.api.ui.ConfigUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {net.minecraft.client.gui.screen.TitleScreen.class, net.minecraft.client.gui.screen.GameMenuScreen.class})
public abstract class TitleScreenMixin extends Screen {

	@Inject(method = "init", at = @At("TAIL"))
	private void addButton(CallbackInfo ci){
		buttons.add(new ButtonWidget(734, 50, 50, "Click Here!"));
	}

	@Inject(method = "buttonClicked", at = @At("TAIL"))
	private void onButtonClicked(ButtonWidget par1, CallbackInfo ci){
		if (par1.id == 734){
			Minecraft.getInstance().openScreen(getConfigScreenFactory("axolotlclientconfig-test").apply(Minecraft.getInstance().screen));
		}
	}

	@Unique
	private Function<Screen, ? extends Screen> getConfigScreenFactory(String name) {
		ConfigManager manager = AxolotlClientConfig.getInstance().getConfigManager(name);
		return parent -> ConfigUI.getInstance().getScreen(this.getClass().getClassLoader(),
			manager.getRoot(), parent);
	}
}
