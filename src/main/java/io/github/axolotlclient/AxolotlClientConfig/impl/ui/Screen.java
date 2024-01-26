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

package io.github.axolotlclient.AxolotlClientConfig.impl.ui;

import java.util.List;

import com.google.common.collect.Lists;
import io.github.axolotlclient.AxolotlClientConfig.impl.util.MathUtil;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Mouse;

public abstract class Screen extends net.minecraft.client.gui.screen.Screen implements ParentElement {

	@Getter
	protected final String title;
	private final List<Drawable> drawables = Lists.newArrayList();
	private final List<Element> children = Lists.newArrayList();
	private final List<Selectable> selectables = Lists.newArrayList();
	private Element focused;
	private boolean dragging;
	private int lastButton;
	private long lastUpdateTime;
	private int lastMouseDragPosX, lastMouseDragPosY = -1;

	public Screen(String title) {
		this.title = title;
	}

	@Override
	public void init(Minecraft minecraft, int i, int j) {
		clearChildren();
		this.minecraft = Minecraft.INSTANCE;
		super.init(minecraft, i, j);
	}

	@Override
	public void render(int mouseX, int mouseY, float delta) {
		renderBackground();


		drawables.forEach(drawable -> drawable.render(mouseX, mouseY, delta));
	}

	@Override
	public List<? extends Element> children() {
		return children;
	}

	@Override
	public void handleMouse() {
		int x = Mouse.getEventX() * this.width / this.minecraft.width;
		int y = this.height - Mouse.getEventY() * this.height / this.minecraft.height - 1;
		int button = Mouse.getEventButton();
		if (Mouse.getEventButtonState()) {
						this.lastButton = button;
			this.lastUpdateTime = System.currentTimeMillis();
			this.mouseClicked(x, y, this.lastButton);
		} else if (button != -1) {

			this.lastButton = -1;
			this.mouseReleased(x, y, button);
		} else if (this.lastButton != -1 && this.lastUpdateTime > 0L) {
			long l = System.currentTimeMillis() - this.lastUpdateTime;
			this.mouseDragged(x, y, this.lastButton, l);
		}
		int scroll = Mouse.getDWheel();
		if (scroll != 0) {
			children.forEach(e -> e.mouseScrolled(x, y, 0, Math.signum(scroll)*2));
		}
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		return ParentElement.super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int button) {
		dragging = true;
		mouseClicked((double) mouseX, mouseY, button);
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int button) {
		mouseReleased((double) mouseX, mouseY, button);
		dragging = false;
	}


	protected void mouseDragged(int mouseX, int mouseY, int button, long lastClick) {
		if (lastMouseDragPosX == -1 || lastMouseDragPosY == -1) {
			lastMouseDragPosX = mouseX;
			lastMouseDragPosY = mouseY;
		}
		mouseDragged(mouseX, mouseY, button, mouseX - lastMouseDragPosX, mouseY - lastMouseDragPosY);
		lastMouseDragPosX = mouseX;
		lastMouseDragPosY = mouseY;
	}

	protected <T extends Element & Drawable & Selectable> T addDrawableChild(T drawableElement) {
		this.drawables.add(drawableElement);
		return this.addSelectableChild(drawableElement);
	}

	protected <T extends Drawable> T addDrawable(T drawable) {
		this.drawables.add(drawable);
		return drawable;
	}

	protected <T extends Element & Selectable> T addSelectableChild(T child) {
		this.children.add(child);
		this.selectables.add(child);
		return child;
	}

	protected void remove(Element child) {
		if (child instanceof Drawable) {
			this.drawables.remove((Drawable) child);
		}

		if (child instanceof Selectable) {
			this.selectables.remove((Selectable) child);
		}

		this.children.remove(child);
	}

	protected void clearChildren() {
		this.drawables.clear();
		this.children.clear();
		this.selectables.clear();
	}

	@Override
	public @Nullable Element getFocused() {
		return focused;
	}

	@Override
	public void setFocusedChild(@Nullable Element child) {
		if (this.focused != null) {
			focused.setFocused(false);
		}
		this.focused = child;
		if (this.focused != null) {
			focused.setFocused(true);
		}
	}

	@Override
	public boolean isDragging() {
		return dragging;
	}

	@Override
	public void setDragging(boolean dragging) {
		this.dragging = dragging;
	}

	@Override
	protected void keyPressed(char c, int i) {
		if (!keyPressed(i, 0, 0)) {
			super.keyPressed(c, i);
		}
		charTyped(c, 0);
	}

	@Override
	public void tick() {
	}
}
