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

package io.github.axolotlclient.AxolotlClientConfig.impl.ui.vanilla.widgets;

import io.github.axolotlclient.AxolotlClientConfig.impl.options.StringOption;
import io.github.axolotlclient.AxolotlClientConfig.impl.ui.TextFieldWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.language.I18n;

public class StringWidget extends TextFieldWidget {

	private final StringOption option;

	public StringWidget(int x, int y, int width, int height, StringOption option) {
		super(Minecraft.INSTANCE.textRenderer, x, y, width, height, I18n.translate(option.getName()));

		setMaxLength(option.getMaxLength());
		write(option.get());
		this.option = option;
		setChangedListener(option::set);
	}

	@Override
	public void drawWidget(int mouseX, int mouseY, float delta) {
		if (!option.get().equals(getText())) {
			setText(option.get());
		}
		super.drawWidget(mouseX, mouseY, delta);
	}
}
