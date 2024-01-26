package io.github.axolotlclient.AxolotlClientConfig.impl.util;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.security.Key;

import org.lwjgl.input.Keyboard;

public class KeyboardUtil {

	public static boolean isShiftDown(){
		return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
	}

	public static boolean isSelectAll(int keyCode){
		return keyCode == Keyboard.KEY_A && isControlDown();
	}

	public static boolean isCopy(int code){
		return code == Keyboard.KEY_C && isControlDown();
	}

	public static boolean isControlDown(){
		return Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
	}

	public static void setClipboard(String value){

		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(value), null);

	}

	public static boolean isPaste(int keyCode) {
		return keyCode == Keyboard.KEY_V && isControlDown();
	}

	public static boolean isCut(int keyCode) {
		return keyCode == Keyboard.KEY_X && isControlDown();
	}
}
