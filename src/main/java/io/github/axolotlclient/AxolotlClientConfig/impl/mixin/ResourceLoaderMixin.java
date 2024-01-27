package io.github.axolotlclient.AxolotlClientConfig.impl.mixin;

import java.util.HashMap;
import java.util.Map;

import io.github.axolotlclient.AxolotlClientConfig.api.ui.ConfigUI;
import io.github.axolotlclient.AxolotlClientConfig.impl.ui.ConfigUIImpl;
import io.github.axolotlclient.AxolotlClientConfig.impl.ui.StyleImpl;
import net.minecraft.client.resource.pack.TexturePacks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TexturePacks.class)
public abstract class ResourceLoaderMixin {

	@Inject(method = "reload", at = @At("TAIL"))
	private void onTextureReloadTail(CallbackInfo ci) {
		ConfigUIImpl.getInstance().preReload();
		Map<String, String> vanillaWidgets = new HashMap<>();
		vanillaWidgets.put("boolean", "io.github.axolotlclient.AxolotlClientConfig.impl.ui.vanilla.widgets.BooleanWidget");
		vanillaWidgets.put("string", "io.github.axolotlclient.AxolotlClientConfig.impl.ui.vanilla.widgets.StringWidget");
		vanillaWidgets.put("enum", "io.github.axolotlclient.AxolotlClientConfig.impl.ui.vanilla.widgets.EnumWidget");
		vanillaWidgets.put("string[]", "io.github.axolotlclient.AxolotlClientConfig.impl.ui.vanilla.widgets.StringArrayWidget");
		vanillaWidgets.put("color", "io.github.axolotlclient.AxolotlClientConfig.impl.ui.vanilla.widgets.ColorWidget");
		vanillaWidgets.put("double", "io.github.axolotlclient.AxolotlClientConfig.impl.ui.vanilla.widgets.DoubleWidget");
		vanillaWidgets.put("float", "io.github.axolotlclient.AxolotlClientConfig.impl.ui.vanilla.widgets.FloatWidget");
		vanillaWidgets.put("integer", "io.github.axolotlclient.AxolotlClientConfig.impl.ui.vanilla.widgets.IntegerWidget");
		vanillaWidgets.put("category", "io.github.axolotlclient.AxolotlClientConfig.impl.ui.vanilla.widgets.CategoryWidget");
		vanillaWidgets.put("graphics", "io.github.axolotlclient.AxolotlClientConfig.impl.ui.vanilla.widgets.GraphicsWidget");
		ConfigUI.getInstance().addStyle(new StyleImpl("vanilla", vanillaWidgets, "io.github.axolotlclient.AxolotlClientConfig.impl.ui.vanilla.screen.VanillaConfigScreen", null));


		ConfigUIImpl.getInstance().postReload();
	}
}
