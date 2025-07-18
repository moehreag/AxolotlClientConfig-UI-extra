package io.github.axolotlclient.AxolotlClientConfig.impl.ui.vanilla.widgets;


import net.minecraft.client.render.TextRenderer;

public class PlainTextButtonWidget extends VanillaButtonWidget {
	private final TextRenderer font;
	private final String content;

	public PlainTextButtonWidget(int x, int y, int width, int height, String content, PressAction empty, TextRenderer font) {
		super(x, y, width, height, content, empty);
		this.font = font;
		this.content = content;
	}

	@Override
	public void drawWidget(int mouseX, int mouseY, float delta) {
		font.drawWithShadow(content, getX(), getY(), 16777215 | 255 << 24);
		if (isHovered()) {
			fillRect(getX(), getY() + getHeight() - 1, getX() + getWidth(), 1, -1);
		}
	}
}
