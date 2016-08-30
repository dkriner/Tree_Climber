package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class StaminaPellet {
	private Sprite model;
	private boolean isVisible;
	private Rectangle rectangle;
	
	public StaminaPellet(int x, int y) {
		model = new Sprite(new Texture("testPellet.jpg"));
		model.setPosition(x, y);
		isVisible = true;
		rectangle = new Rectangle(x,y,16,16);
	}
	
	public boolean isVisible() {
		return isVisible;
		
	}
	
	public void setVisible(boolean bool) {
		isVisible = bool;
	}
	
	public void remove() {
		setRectanglePosition(0,0);
	
	}
	
	public Sprite getSprite() {
		return model;
		
	}
	
	private void setRectanglePosition(float x, float y) {
		rectangle.setPosition(x,y);
		
	}
	
	public Rectangle getRectangle() {
		return rectangle;
		
	}
}
