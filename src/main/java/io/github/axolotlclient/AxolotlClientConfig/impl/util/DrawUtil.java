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

package io.github.axolotlclient.AxolotlClientConfig.impl.util;

import java.util.*;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.BufferBuilder;
import io.github.axolotlclient.AxolotlClientConfig.api.options.Option;
import io.github.axolotlclient.AxolotlClientConfig.api.util.Color;
import io.github.axolotlclient.AxolotlClientConfig.api.util.Rectangle;
import io.github.axolotlclient.AxolotlClientConfig.impl.ui.DrawingUtil;
import io.github.axolotlclient.AxolotlClientConfig.impl.ui.NVGFont;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiElement;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.TextRenderer;
import net.minecraft.client.render.Window;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.resource.Identifier;
import org.lwjgl.opengl.GL11;

public class DrawUtil extends GuiElement implements DrawingUtil {

	private static final DrawUtil INSTANCE = new DrawUtil();
	private static final Map<Identifier, Integer> textureCache = new HashMap<>();

	private static final Stack<Rectangle> scissorStack = new Stack<>();

	public static void fillRect(Rectangle rectangle, Color color) {
		fillRect(rectangle.x(), rectangle.y(), rectangle.width(),
			rectangle.height(),
			color.get().toInt());
	}

	public static void fillRect(int x, int y, int width, int height, int color) {
		INSTANCE.fill(x, y, x + width, y + height, color);
	}

	public static void outlineRect(Rectangle rectangle, Color color) {
		outlineRect(rectangle.x(), rectangle.y(), rectangle.width(), rectangle.height(), color.get().toInt());
	}

	public static void outlineRect(int x, int y, int width, int height, int color) {
		fillRect(x, y, 1, height - 1, color);
		fillRect(x + width - 1, y + 1, 1, height - 1, color);
		fillRect(x + 1, y, width - 1, 1, color);
		fillRect(x, y + height - 1, width - 1, 1, color);
	}

	public static void drawCenteredString(TextRenderer renderer,
										  String text, int centerX, int y,
										  int color, boolean shadow) {
		drawString(renderer, text, centerX - renderer.getWidth(text) / 2,
			y,
			color, shadow);
	}

	public static void drawString(TextRenderer renderer, String text, int x, int y,
								  int color, boolean shadow) {
		if (shadow) {
			renderer.drawWithShadow(text, x, y, color);
		} else {
			renderer.draw(text, x, y, color);
		}
	}

	public static void bindTexture(Identifier texture) {
		Minecraft.INSTANCE.getTextureManager().bind(texture);
	}

	/*public static int nvgCreateImage(long ctx, Identifier texture) {
		return nvgCreateImage(ctx, texture, 0);
	}*/

	/*public static int nvgCreateImage(long ctx, Identifier texture, int imageFlags) {
		try {
			ByteBuffer buffer = mallocAndRead(Minecraft.INSTANCE.texturePacks.selected.getResource(texture.toString()));
			int handle = NanoVG.nvgCreateImageMem(ctx, imageFlags, buffer);
			MemoryUtil.memFree(buffer);
			return handle;
		} catch (IOException ignored) {
		}
		return 0;
	}

	private static ByteBuffer mallocAndRead(InputStream in) throws IOException {
		try (ReadableByteChannel channel = Channels.newChannel(in)) {
			ByteBuffer buffer = MemoryUtil.memAlloc(8192);

			while (channel.read(buffer) != -1)
				if (buffer.remaining() == 0)
					buffer = MemoryUtil.memRealloc(buffer, buffer.capacity() + buffer.capacity() * 3 / 2);

			((Buffer) buffer).flip();

			return buffer;
		}
	}*/

	public static void pushScissor(int x, int y, int width, int height) {
		pushScissor(new Rectangle(x, y, width, height));
	}

	public static void pushScissor(Rectangle rect) {
		setScissor(scissorStack.push(rect));
	}

	public static void popScissor() {
		scissorStack.pop();
		setScissor(scissorStack.empty() ? null : scissorStack.peek());
	}

	private static void setScissor(Rectangle rect) {
		if (rect != null) {
			GL11.glEnable(GL11.GL_SCISSOR_TEST);
			Window window = new Window(Minecraft.INSTANCE.options, Minecraft.INSTANCE.width, Minecraft.INSTANCE.height);
			int scale = window.getScale();
			GL11.glScissor(rect.x() * scale, (int) ((window.getScaledHeight() - rect.height() - rect.y()) * scale),
				rect.width() * scale, rect.height() * scale);
		} else {
			GL11.glDisable(GL11.GL_SCISSOR_TEST);
		}
	}

	public static void drawScrollingText(String text, int x, int y, int width, int height, Color color) {
		drawScrollingText(x, y, x + width, y + height, text, color);
	}

	public static void drawScrollingText(int left, int top, int right, int bottom, String text, Color color) {
		drawScrollingText(Minecraft.INSTANCE.textRenderer, text, (left + right) / 2, left, top, right, bottom, color);
	}

	public static void drawScrollingText(TextRenderer renderer, String text, int center, int left, int top, int right, int bottom, Color color) {
		int textWidth = renderer.getWidth(text);
		int y = (top + bottom - 9) / 2 + 1;
		int width = right - left;
		if (textWidth > width) {
			float r = textWidth - width;
			double d = (double) (System.nanoTime() / 1000000L) / 1000.0;
			double e = Math.max((double) r * 0.5, 3.0);
			double f = Math.sin((Math.PI / 2) * Math.cos((Math.PI * 2) * d / e)) / 2.0 + 0.5;
			double g = f * r;
			pushScissor(left, top, right - left, bottom - top);
			drawString(renderer, text, left - (int) g, y, color.toInt(), true);
			popScissor();
		} else {
			int min = left + textWidth / 2;
			int max = right - textWidth / 2;
			int centerX = center < min ? min : Math.min(center, max);
			drawCenteredString(renderer, text, centerX, y, color.toInt(), true);
		}
	}

	public static void drawTooltip(Option<?> option, int x, int y) {
		String tooltip = I18n.translate(option.getTooltip());
		if (tooltip.equals(option.getTooltip())) {
			return;
		}
		String[] text = tooltip.split("<br>");
		if (!text[0].isEmpty() || text.length > 1) {
			INSTANCE.renderTooltip(Arrays.asList(text), x-2, y+12+3+10);
		}

	}

	protected void renderTooltip(List<String> list, int mouseX, int mouseY) {
		TextRenderer textRenderer = Minecraft.INSTANCE.textRenderer;
		if (!list.isEmpty()) {
			GL11.glDisable(32826);
			Lighting.turnOff();
			GL11.glDisable(2896);
			GL11.glDisable(2929);
			int width = 0;

			for (String var7 : list) {
				int var8 = textRenderer.getWidth(var7);
				if (var8 > width) {
					width = var8;
				}
			}

			int x = mouseX + 12;
			int y = mouseY - 12;
			int height = 8;
			if (list.size() > 1) {
				height += 2 + (list.size() - 1) * 10;
			}

			this.drawOffset = 300.0F;
			int var10 = -267386864;
			this.fillGradient(x - 3, y - 4, x + width + 3, y - 3, var10, var10);
			this.fillGradient(x - 3, y + height + 3, x + width + 3, y + height + 4, var10, var10);
			this.fillGradient(x - 3, y - 3, x + width + 3, y + height + 3, var10, var10);
			this.fillGradient(x - 4, y - 3, x - 3, y + height + 3, var10, var10);
			this.fillGradient(x + width + 3, y - 3, x + width + 4, y + height + 3, var10, var10);
			int var11 = 1347420415;
			int var12 = (var11 & 16711422) >> 1 | var11 & 0xFF000000;
			this.fillGradient(x - 3, y - 3 + 1, x - 3 + 1, y + height + 3 - 1, var11, var12);
			this.fillGradient(x + width + 2, y - 3 + 1, x + width + 3, y + height + 3 - 1, var11, var12);
			this.fillGradient(x - 3, y - 3, x + width + 3, y - 3 + 1, var11, var11);
			this.fillGradient(x - 3, y + height + 2, x + width + 3, y + height + 3, var12, var12);

			for (int i = 0; i < list.size(); ++i) {
				String line = list.get(i);
				textRenderer.drawWithShadow(line, x, y, -1);
				if (i == 0) {
					y += 2;
				}

				y += 10;
			}

			drawOffset = 0.0F;
		}
	}

	public static void drawTooltip(long ctx, NVGFont font, Option<?> option, int x, int y) {
		String tooltip = I18n.translate(option.getTooltip());
		if (tooltip.equals(option.getTooltip())) {
			return;
		}
		String[] text = tooltip.split("<br>");
		if (!text[0].isEmpty() || text.length > 1) {
			Screen screen = Minecraft.INSTANCE.screen;
			INSTANCE.drawTooltip(ctx, font, text, x, y, screen.width, screen.height);
		}

	}

	public static void drawTexture(int x, int y, float u, float v, int width, int height, float scaleU, float scaleV) {
		float invertedScaleU = 1.0f / scaleU;
		float invertedScaleV = 1.0f / scaleV;
		BufferBuilder bufferBuilder = BufferBuilder.INSTANCE;
		bufferBuilder.start();
		bufferBuilder.vertex(x, y + height, 0.0, u * invertedScaleU, (v + (float) height) * invertedScaleV);
		bufferBuilder.vertex(x + width, y + height, 0.0, (u + (float) width) * invertedScaleU, (v + (float) height) * invertedScaleV);
		bufferBuilder.vertex(x + width, y, 0.0, (u + (float) width) * invertedScaleU, v * invertedScaleV);
		bufferBuilder.vertex(x, y, 0.0, u * invertedScaleU, v * invertedScaleV);
		bufferBuilder.end();
	}
}
