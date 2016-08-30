package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Player extends Sprite implements InputProcessor{
	//player traits
	private int health, maxHealth, score, lives, freezes;
	private double stamina, maxStamina, movementSpeed, boostedSpeed, fullSpeed, boost, maxBoost;
	private float modelHeight, modelWidth, playerLocationx, playerLocationy;
	private Texture model;
	private Vector2 playerLocation;
	private float screenHeight = Gdx.graphics.getHeight();
	private float screenWidth =  Gdx.graphics.getWidth();

	
	
	
	public Player(String body) {	//when making new player, include String for Texture jpg location
		model = new Texture(body);
		movementSpeed = 3.0;
		fullSpeed = movementSpeed;
		boostedSpeed = movementSpeed*2;
		maxHealth = 190;
		health = maxHealth;
		maxStamina = 190;
		stamina = maxStamina;
		maxBoost = 190;
		boost = maxBoost;
		freezes = 0;
		score = 0;
		lives = 5;
		modelHeight = model.getHeight();
		modelWidth = model.getWidth();
		
		Vector2 playerLocation = new Vector2(screenWidth/2, screenHeight/4);	//spawn point
		playerLocationx = playerLocation.x;
		playerLocationy = playerLocation.y;
		
		
	}

	
	
	
	
	public Texture getModel() {
		return model;
	}
	
	public Vector2 getPlayerLocation() {
		return playerLocation;
		
	}
	
	public float getLocationX() {
		return playerLocationx;
	}
	
	public float getLocationY() {
		return playerLocationy;
	}
	
	public void setLocationX(double x) {
		playerLocationx += x;
	}
	
	public void setLocationY(double y) {
		playerLocationy += y;
	}
	public void setSpawnX(float x) {
		playerLocationx = x;
	}
	
	public void setSpawnY(float y) {
		playerLocationy = y;
	}
	
	public float getWidth() {
		return modelWidth/2;
	}
	
	public float getHeight() {
		return modelHeight/2;
	}
	
	public int getScore() {
		return score;
	}
	
	public void setScore(int x) {
		score += x;
	}
	
	
	public int getHealth() {
		return health;
	}
	
	public int getMaxHealth() {
		return maxHealth;
	}
	
	public int getFreezes() {
		return freezes;
	}
	
	public void incrFreeze() {
		freezes++;
	}
	
	public void deductFreeze() {
		freezes--;
	}
	
	public double getStamina() {
		return stamina;
	}
	
	public double getMaxStamina() {
		return maxStamina;
	}
	
	public double getSpeed() {
		return movementSpeed;
	}
	
	public void setHealth() {
		health = 100;
	}
	
	
	

	
	public void setHealth(int health) { //when calling, enter a negative int to take away health, and positive to add
		//these if's prevent health from ever being <0 or >100
		if(this.health + health <= 0) {
			this.health = 0;
			//player dies
		}
		else if(this.health + health >= maxHealth)
			this.health = maxHealth;
		
		else
		
		this.health += health;
	}
	
	public void setStaminaMax() {
		stamina = 100;
	}
	
	public void depleteStamina() {
		stamina = 0.0;
	}
	
	public void setStamina(double stamina) {
		//these if's prevent stamina from ever being <0 or >100
		if(this.stamina + stamina <= 0) 
			this.stamina = 0;
		
		if(this.stamina + stamina >= maxStamina)
			this.stamina = maxStamina;
		
		else
		
		this.stamina += stamina;
	
	}
	
	public void setBoost(double x) {
		if(this.boost + x <= 0)
			this.boost = 0;
		
		else if(boost + x >= maxBoost)
			boost = maxBoost;
		
		else
			boost += x;
	}
	
	public void setSpeed(double speed) {
		movementSpeed = speed;
		boostedSpeed = movementSpeed*2;
		
	}
	public void boostSpeed() {
		movementSpeed = boostedSpeed;
	}
	
	public double getBoost() {
		return boost;
	}
	
	public double getMaxBoost() {
		return maxBoost;
	}
	
	public void death() {
		lives--;
	}
	
	public int getLives() {
		return lives;
	}
	
	public double getBoostedSpeed() {
		return boostedSpeed;
	}
	
	
	
	public void resetSpeed() {
		movementSpeed = fullSpeed;
		boostedSpeed = movementSpeed*2;
	}
	
	public void setFullSpeed(double x) {
		fullSpeed = x;
		movementSpeed = x;
		boostedSpeed = x*2;
	}
	


	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
}
