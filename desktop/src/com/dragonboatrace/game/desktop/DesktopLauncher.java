package com.dragonboatrace.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.dragonboatrace.game.DragonBoatRace;
import java.awt.Dimension;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		Dimension dim = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		config.width=(int)dim.getWidth();
		config.height=(int)dim.getHeight();
		config.fullscreen=true;
		new LwjglApplication(new DragonBoatRace(), config);
	}
}
