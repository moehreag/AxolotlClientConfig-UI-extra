/*
 * Copyright © 2021-2023 moehreag <moehreag@gmail.com> & Contributors
 *
 * This file is part of AxolotlClient.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * For more information, see the LICENSE file.
 */

package io.github.axolotlclient.AxolotlClientConfig.impl;

import java.io.IOException;

import io.github.axolotlclient.AxolotlClientConfig.api.util.WindowPropertiesProvider;
import io.github.axolotlclient.AxolotlClientConfig.impl.ui.ConfigUIImpl;
import io.github.axolotlclient.AxolotlClientConfig.impl.ui.NVGMC;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.Window;
import net.minecraft.client.resource.Identifier;
import net.minecraft.client.resource.Resource;
import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;
import net.ornithemc.osl.lifecycle.api.client.MinecraftClientEvents;
import net.ornithemc.osl.resource.loader.api.ResourceLoaderEvents;

public class AxolotlClientConfigMod implements ClientModInitializer {

	@Override
	public void initClient() {
		MinecraftClientEvents.TICK_END.register(client -> AxolotlClientConfigImpl.getInstance().runTick());
		NVGMC.setWindowPropertiesProvider(new WindowPropertiesProvider() {
			@Override
			public int getHeight() {
				return Minecraft.INSTANCE.height;
			}

			@Override
			public int getWidth() {
				return Minecraft.INSTANCE.width;
			}

			@Override
			public float getScaleFactor() {
				return new Window(Minecraft.INSTANCE, Minecraft.INSTANCE.width, Minecraft.INSTANCE.height).getScale();
			}
		});

		ResourceLoaderEvents.END_RESOURCE_RELOAD.register(() -> {
			ConfigUIImpl.getInstance().preReload();
			Minecraft.getInstance().getResourceManager()
				.getResources(new Identifier(ConfigUIImpl.getInstance().getUiJsonPath())).forEach(resource -> {
					ConfigUIImpl.getInstance().read(((Resource)resource).asStream());
				});
			ConfigUIImpl.getInstance().postReload();
		});

	}
}
