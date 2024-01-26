package io.github.axolotlclient.AxolotlClientConfig.impl.util;

import net.minecraft.SharedConstants;
import org.apache.commons.lang3.ArrayUtils;

public class TextUtil {

	public static final int FONT_HEIGHT = 8;

	public static boolean isValidChatChar(char c){
		return ArrayUtils.contains(SharedConstants.VALID_CHAT_CHARACTERS.toCharArray(), c);
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
