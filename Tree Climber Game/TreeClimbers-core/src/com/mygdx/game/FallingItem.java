package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class FallingItem {
	private Sprite model;
	private boolean isVisible;
	private Rectangle rectangle;
	private float x, y;
	
	public FallingItem(float x, float y) {
		this.x = x;
		this.y = y;
		model = new Sprite(new Texture("Images/deathPellet.jpg"));
		model.setPosition(x, y);
		isVisible = true;
		rectangle = new Rectangle(x,y,16,16);
	}
	
	public boolean isVisible() {
		return isVisible;
		
	}
	
	
	public void setLocationY(float n) {
		y += n;
	}
	
	
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setVisible(boolean bool) {
		isVisible = bool;
	}
	
	public void remove() {
		setRectanglePosition(0,0);
	
	}
	
	public Sprite getSprite() {
		rectangle.setPosition(x,y);
		model.setPosition(x, y);
		return model;
		
	}
	
	private void setRectanglePosition(float x, float y) {
		rectangle.setPosition(x,y);
		
	}
	
	public Rectangle getRectangle() {
		return rectangle;
		
	}
}
