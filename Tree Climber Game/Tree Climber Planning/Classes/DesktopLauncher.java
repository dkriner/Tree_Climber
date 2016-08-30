package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.Level;
//import com.mygdx.game.TreeClimber;


public class DesktopLauncher {
	public static void main (String[] arg) {
		
		
		/*LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Tree Climber";
		config.useGL30 = true;
		config.width = 800;
		config.height = 600;
		
		new LwjglApplication(new TreeClimber(), config);*/
		
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Tree Climber";
		config.useGL30 = true;
		config.width = 800;
		config.height = 600;
		
		
		new LwjglApplication(new Level(), config);
	}
}
