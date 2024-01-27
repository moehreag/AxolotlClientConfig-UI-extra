package io.github.axolotlclient.AxolotlClientConfig.impl.util;

import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;

public class TextUtil {

	public static final int FONT_HEIGHT = Minecraft.INSTANCE.textRenderer.fontHeight;

	public static boolean isValidChatChar(char c){
		return SharedConstants.isValidChatChar(c);
	}

	public static String stripInvalidChars(String input){
		StringBuilder builder = new StringBuilder();
		for (char c : input.toCharArray()){
			if (isValidChatChar(c)){
				builder.append(c);
			}
		}
		return builder.toString();
	}
}
