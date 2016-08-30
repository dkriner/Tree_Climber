package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
/** 
 * @Author Darren Kriner
 * @StartDate: 8/15/15
 * 
 * ****This class will be used for creating all Levels of the game. Levels will be built depending on map file.
 * When creating maps, include object layers for items, enemies, etc. and blocked tiles
 * ArrayLists will be created for each item type, and locations for spawning items will reference the object layers' markers
 * This should allow us to use one class for making all levels, instead of one class per level
 * 
 *
 * 
 * =======KNOWN BUGS=========
 * 
 * moving camera reveals beyond the edges of the map
 * Intro-delay doesn't always activate, usually stops working after the first 2 levels have been completed
 * resize broken
 * dispose() broken
 * rotate player
 * 
 * =======THINGS TO IMPLEMENT (TO-DO)========
 * 
 * 
 * animation*
 * 321 go before levels
 * update help screen, new pause buttons p&r
 * sound effects and music
 * updated visuals
 * create more maps
 * insert more hazards
 * AI enemies
 * make meters easier to see? (bigger/brighter)
 * lives system* 
 * credits screen
 * */

public class Level extends Game implements Screen{
	

	
	//private ArrayList<Rectangle> blockers;
	private ArrayList<StaminaPellet> staminaPellets;
	private ArrayList<FallingItem> debris;
	private ArrayList<HealthPellet> healthPellets;
	private ArrayList<PointPellet> pointPellets;
	private ArrayList<GoalPellet> goalPellets;
	private ArrayList<FreezePellet> freezePellets;
	private ArrayList<Rectangle> hazards;
	private Sprite[] animationFrames;
	private TiledMap map;	//map
	private TiledMapTileLayer blocked;	
	private MapProperties prop;		
	private Animation animation;
	
	private Sprite healthBar, healthMeter, staminaBar, staminaMeter, boostBar, boostMeter, freezeScreen;
	private BitmapFont score, paused, ready, freezes, pointGoalText, getReadyText, healthWarning, staminaWarning, enoughPoints;

	
	private OrthogonalTiledMapRenderer renderer;	//renders map
	private OrthographicCamera camera;	//used to view map, used for scroll

	
	private Player player;
	private SpriteBatch batch;

	
	private Rectangle recPlayer, goal;
	
	private float screenHeight, screenWidth, centerX, screenCeiling, screenFloor, oldX, oldY, scoreX, scoreY, freezesX, freezesY, pointGoalX, pointGoalY, elapsedTime;
	private int mapWidth, mapHeight, tileWidth, tileHeight, pointGoal;
	//private float gameTime;
	private double scrollSpeed;
	protected boolean startUp, frozen, bigMap, intro, isBoosting, noDebris, soundOn, facingUp, facingDown, facingLeft, facingRight;
	private String levelPath, scoreName, freezesName;
	
	private MyGame game;
	
	private enum STATE {
		playing, paused, frozen, intro, completed
	};
	
	public STATE gameState = STATE.playing;
	
	
	
	public Level(MyGame game) {
		this.game = game;

	}
	
	@Override
	public void create() {
		
		//gameTime = 0.0f;
		pointGoal = 1000;
		levelPath = "map";
		startUp = true;
		intro = true;
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
		centerX = (float) ((screenWidth/2));	//centerX used for translating camera horizontally
		screenCeiling = screenHeight;
		screenFloor = 0;
		game.helpScreen.setPlaying(true);
		frozen = false;
		isBoosting = false;
		noDebris = false;
		facingUp = true;
		facingDown = false;
		facingRight = false;
		facingLeft = false;
		
		score = new BitmapFont();
		score.setColor(Color.CYAN);
		scoreName = "Score: 0";
		scoreY = 550;
		scoreX = 50;
		freezes = new BitmapFont();
		freezes.setColor(Color.CYAN);
		freezesName = "Freezes: 0";
		freezesY = 50;
		freezesX = scoreX;
		freezeScreen = new Sprite(new Texture("Images/freezeScreen.png"));
		
		enoughPoints = new BitmapFont();
		enoughPoints.setColor(Color.YELLOW);
		
		healthWarning = new BitmapFont();
		healthWarning.setColor(Color.RED);
		staminaWarning = new BitmapFont();
		staminaWarning.setColor(Color.GREEN);
		
		pointGoalText = new BitmapFont();
		pointGoalText.setColor(Color.CYAN);		//(Gdx.graphics.getWidth()/1.4f), screenCeiling-(Gdx.graphics.getHeight()/8)
		pointGoalX = Gdx.graphics.getWidth()/2.4f;
		pointGoalY = screenCeiling-(Gdx.graphics.getHeight()/12);
		//scaling text on screen
		pointGoalText.getData().setScale(2, 2);
		score.getData().setScale(1.5f, 1.5f);
		freezes.getData().setScale(1.5f, 1.5f);
		enoughPoints.getData().setScale(1.5f, 1.5f);
	
		
		paused = new BitmapFont();
		paused.getData().setScale(1.5f, 1.5f);
		ready = new BitmapFont();
		paused.setColor(Color.CYAN);
		ready.setColor(Color.RED);
		
		//necessary junk to render map
		
		map = new TmxMapLoader().load("Maps/" + levelPath + game.completions + ".tmx");
		blocked = (TiledMapTileLayer) map.getLayers().get(0);
		prop = map.getProperties();
		
		mapWidth = prop.get("width", Integer.class)*prop.get("tilewidth", Integer.class);			//gets dimensions of map in pixels
		mapHeight = prop.get("height", Integer.class)*prop.get("tileheight", Integer.class);
		tileWidth = prop.get("tilewidth", Integer.class);
		tileHeight = prop.get("tileheight", Integer.class);
		
		
		camera = new OrthographicCamera(screenWidth, screenHeight);
		camera.setToOrtho(false,(screenWidth),(screenHeight));
		camera.update();	
		
		renderer = new OrthogonalTiledMapRenderer(map);


		scrollSpeed = 1.2;
		
		//CREATING THE PLAYER
		player = new Player("Images/antCycle.png");
		animationFrames = new Sprite[4];
		for(int i = 0; i < 4; i++)
			animationFrames[i] = new Sprite(new Texture("Images/antCycle" + (i+1) + ".png"));
				
		
		animation = new Animation(1f/4f, new TextureRegion(animationFrames[0].getTexture()), new TextureRegion(animationFrames[1].getTexture()), new TextureRegion(animationFrames[2].getTexture()), new TextureRegion(animationFrames[3].getTexture()));
				
		oldX = player.getLocationX();
		oldY = player.getLocationY();
		
		//player collision rectangle
		recPlayer = new Rectangle(player.getLocationX(), player.getLocationY(), player.getWidth(), player.getHeight());
		batch = new SpriteBatch();
		
		
		//creating HUD
		healthBar = new Sprite(new Texture("Images/healthbar.png"));
		healthBar.setSize(screenWidth/4,screenHeight/10);
		healthBar.setPosition((float) (screenWidth*.7), (float) (screenHeight*.90));	
		healthMeter = new Sprite(new Texture("Images/healthmeter.png"));
		healthMeter.setPosition(healthBar.getX()+10 ,healthBar.getY() + 6);
		healthMeter.setSize((float) player.getHealth(), 8);
		
		staminaBar = new Sprite(new Texture("Images/staminabar.png"));
		staminaBar.setSize(healthBar.getWidth(), healthBar.getHeight());
		staminaBar.setPosition((float) (screenWidth*.7), (float) (screenHeight*.80));
		staminaMeter = new Sprite(new Texture("Images/staminameter.png"));
		staminaMeter.setSize(healthMeter.getWidth(), healthMeter.getHeight());
		staminaMeter.setPosition(staminaBar.getX() + 5, staminaBar.getY() + 6);
		
		boostBar = new Sprite(new Texture("Images/boostbar.png"));
		boostBar.setSize(screenWidth/4, screenHeight/10);
		boostBar.setPosition((float) (screenWidth*.7), (float) (screenHeight*0.70));
		boostMeter = new Sprite (new Texture("Images/boostmeter.png"));
		boostMeter.setSize(healthMeter.getWidth(),  healthMeter.getHeight());
		boostMeter.setPosition(boostBar.getX()+ 5, boostBar.getY()+6);
		
		getReadyText = new BitmapFont();
		getReadyText.setColor(Color.WHITE);

		bigMap  = (mapWidth > screenWidth);	
		bigMap = (mapHeight > screenHeight);
		if(bigMap) {								//enable horizontal camera movement
			centerX += (mapWidth - screenWidth)/2;
			camera.translate((mapWidth-screenWidth)/2, 0);
			staminaBar.translate((mapWidth-screenWidth)/2, 0);
			staminaMeter.translate((mapWidth-screenWidth)/2, 0);
			healthBar.translate((mapWidth-screenWidth)/2, 0);
			healthMeter.translate((mapWidth-screenWidth)/2, 0);
			boostBar.translate((mapWidth-screenWidth)/2, 0);
			boostMeter.translate((mapWidth-screenWidth)/2, 0);
			freezeScreen.translate((mapWidth-screenWidth)/2, 0);
			player.setLocationX((mapWidth - screenWidth)/2);
			scoreX += ((mapWidth - screenWidth)/2);
			pointGoalX += ((mapWidth - screenWidth)/2);
			freezesX += ((mapWidth - screenWidth)/2);
			oldX = player.getLocationX();
			oldY = player.getLocationY();
			camera.update();
			//System.out.println("centerx = " + centerX);
		}

		hazards = new ArrayList<Rectangle>();
		staminaPellets = new ArrayList<StaminaPellet>();
		healthPellets = new ArrayList<HealthPellet>();
		pointPellets = new ArrayList<PointPellet>();
		goalPellets = new ArrayList<GoalPellet>(1);
		freezePellets = new ArrayList<FreezePellet>(5);
		
		for(int i = 0; i < (mapWidth/prop.get("tilewidth", Integer.class)); i++ ) {
			for(int j = 0; j < (mapHeight/prop.get("tileheight", Integer.class)); j++ ) {
				
				if(blocked.getCell(i, j).getTile().getProperties().containsKey("stamina")) 								//reads all tiles in uploaded map, and checks for custom properties
					staminaPellets.add(new StaminaPellet(i*tileWidth, j*tileHeight));									//if a custom property is found, a corresponding pellet is created and drawn
																														//in that tile's location
				else if(blocked.getCell(i, j).getTile().getProperties().containsKey("health"))
					healthPellets.add(new HealthPellet(i*tileWidth, j*tileHeight));
				
				else if(blocked.getCell(i, j).getTile().getProperties().containsKey("points"))
					pointPellets.add(new PointPellet(i*tileWidth, j*tileHeight));
				
				else if(blocked.getCell(i, j).getTile().getProperties().containsKey("playerSpawn")) {
					player.setSpawnX(i*tileWidth);
					player.setSpawnY(j*tileHeight);		
					recPlayer.setPosition(player.getLocationX(), player.getLocationY());
				}
				
				else if(blocked.getCell(i, j).getTile().getProperties().containsKey("goal")) {
					goal = new Rectangle(i*tileWidth, j*tileHeight, tileWidth, tileHeight);
					goalPellets.add(new GoalPellet(i*tileWidth, j*tileWidth));
				}
				
				else if(blocked.getCell(i, j).getTile().getProperties().containsKey("freeze")) {
					freezePellets.add(new FreezePellet(i*tileWidth, j*tileHeight));
				}
				
				else if(blocked.getCell(i, j).getTile().getProperties().containsKey("hazard")) {
					hazards.add(new Rectangle(i*tileWidth, j*tileHeight, tileWidth, tileHeight));
				}
				
			}
			
		}
		
		//LEVEL SPECIFIC GAMEPLAY SETTINGS
		//NOTE: DO NOT SET PLAYER SPEED TO MORE THAN 4.0
		if(game.completions == 0)  {			
			setScrollSpeed(0.4);
			setMovementSpeed(2.5);
			pointGoal = 400;
			getReadyText.setColor(Color.RED);
		
			
		}
		
		else if(game.completions == 1) {
			setScrollSpeed(0.5);
			setMovementSpeed(3.0);
			pointGoal = 800;
		}
		else if(game.completions == 2) {
			setScrollSpeed(1.0);
			setMovementSpeed(4.0);
			pointGoal = 900;
		}
		else if(game.completions == 3) {
			setScrollSpeed(1.0);
			setMovementSpeed(4.0);
			pointGoal = 4000;
		}
		
		else if(game.completions == 4) {
			pointGoal = 3000;
			setScrollSpeed(0.8);
			setMovementSpeed(4.0);
			noDebris = true;
			
		}
		
		if(!noDebris) {
			debris = new ArrayList<FallingItem>();//creates falling debris
			
			for(int i = 0; i < mapWidth; i+= 100) 
				debris.add(new FallingItem(i, (int) screenCeiling + 100));
		}
			
		
		soundOn = true;
		//MUSIC MANAGEMENT
		SoundManager.spooky.setLooping(false);
		SoundManager.spooky.setVolume(0.2f);
		SoundManager.delayFun.setLooping(false);
		SoundManager.delayFun.setVolume(0.3f);
		if(soundOn) {
			if(game.completions < 3)
				SoundManager.spooky.play();
			else
				SoundManager.delayFun.play();
		}	
		
	}
	

	
	@Override
	public void render(float delta) {
		//more render junk
		
				Gdx.gl.glClearColor(0, 0, 0, 1);
		        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		        renderer.setView(camera);
		        renderer.render();
		        batch.setProjectionMatrix(camera.combined);		//fixes issue of collisions being affected by scrolling
		        elapsedTime += Gdx.graphics.getDeltaTime();
		        camera.update();
		        
		        batch.begin();
		        
		        score.draw(batch, scoreName, scoreX, scoreY); 
		    	freezes.draw(batch, freezesName, freezesX, freezesY);					//Draw bitmap font HUD pieces
		    	pointGoalText.draw(batch, "Point Goal: " + pointGoal, pointGoalX, pointGoalY);
		    	
		        //if(startUp) 				//at the start of the level, wait 3 seconds for the player to get ready
		        	//introDelay(); 
		        
		        			//drawing things
		        drawItems();
		        pauseCheck();
		        boostCheck();
		        
		        if(gameState == STATE.frozen)
		        	freezeScreen.draw(batch);
		        
		        

		        if (gameState == STATE.frozen){				//stop screen scrolling if a freeze powerup is used
			        scoreY -= scrollSpeed;
			        freezesY -= scrollSpeed;
			        pointGoalY -= scrollSpeed;
			        pointGoalText.draw(batch, "Point Goal: " + pointGoal, pointGoalX, pointGoalY);
			        score.draw(batch, scoreName, scoreX, scoreY);			//scrolls the score the same speed the screen is scrolling so it stays in the same part of the screen
			        freezes.draw(batch, freezesName, freezesX, freezesY);
			       
		        }
		        
		        staminaBar.draw(batch);	
		        healthBar.draw(batch);
		        healthMeter.draw(batch);
		        staminaMeter.draw(batch);
		        boostBar.draw(batch);
		        boostMeter.draw(batch);
		        
		        batch.draw(animation.getKeyFrame(elapsedTime, true), player.getLocationX(), player.getLocationY()); //draws player
		        
		        
		        if(player.getHealth() < player.getMaxHealth()*.35)
		        	getReadyText.draw(batch, "WARNING: HEALTH LOW", freezesX + screenWidth/6, (float) (screenCeiling-(Gdx.graphics.getHeight()/1.8)));
		        	//play warning sound
		        if(player.getStamina() < player.getMaxStamina()*.30)
		        	staminaWarning.draw(batch, "WARNING: LOW STAMINA", freezesX + screenWidth/6, (float) (screenCeiling-(Gdx.graphics.getHeight()/1.9)));
		        
		        if(player.getScore() >= pointGoal) {
		        	enoughPoints.draw(batch, "POINT GOAL REACHED!", pointGoalX - screenWidth / 15, pointGoalY + screenHeight / 17);
		        	
		        }
		        	
		        //normal mechanics when the game is not paused or just starting
		        if(gameState != STATE.paused && gameState != STATE.intro) {
			        //gameTime = Gdx.graphics.getDeltaTime();
			        updateStaminaAndHealthandBoost();	        
			        update();        		        
			        scroll();
			        scoreY += scrollSpeed;
			        freezesY += scrollSpeed;
			        pointGoalY += scrollSpeed;
		        }
		        
		        else if(gameState == STATE.paused)	//notify when game is paused
		        	paused.draw(batch, "PAUSED", (centerX), screenCeiling-(Gdx.graphics.getHeight()/3));
		        	  
		     
		        batch.end();
		
	}

	
	
	public void update() {
		
		
		boolean collisionX = false, collisionY = false; 
		float tileWidth = blocked.getTileWidth();
		float tileHeight = blocked.getTileHeight();
		oldX = player.getLocationX();
		oldY = player.getLocationY();
		
																	//******PLAYER MOVEMENT********//
							//player moves with WASD or arrow keys, and cannot move beyond any collision block, the screen's floor, or the screen's ceiling
		
		if((Gdx.input.isKeyPressed(Keys.W) || (Gdx.input.isKeyPressed(Keys.UP))) && (player.getLocationY() < screenCeiling-player.getHeight())) {
			//if upper block is not a collision block
			
			
			collisionY = blocked.getCell((int) ((player.getLocationX() + player.getWidth()/2) / tileWidth), (int) 			
				((player.getLocationY() + player.getWidth() ) / tileHeight)).getTile().getProperties().containsKey("blocked");
			
			
			if(!collisionY) {//if there is not collision...
				player.setLocationY(player.getSpeed());	
				recPlayer.setPosition(player.getLocationX(), player.getLocationY());
			}
			
			//rotate player animation frames
			if(!facingUp) {
				if(facingLeft) {
					for(int i = 0; i < 4; i++) 
						animationFrames[i].rotate90(true);

					facingLeft = false;
					
				}
				else if(facingRight) {
					for(int i = 0; i < 4; i++) 
						animationFrames[i].rotate90(false);		
	
					facingRight = false;
										
				}
				else {
					for(int i = 0; i < 4; i++) 
						animationFrames[i].rotate(180);					
					
					facingDown = false;	
				}
				facingUp = true;
			}
			
		}
		
		if((Gdx.input.isKeyPressed(Keys.A) || (Gdx.input.isKeyPressed(Keys.LEFT))) && (player.getLocationX() > 0 + player.getWidth())) {
			//if left tile is not a collision block
			 collisionX = blocked.getCell((int) (player.getLocationX() / tileWidth), (int) 
					 ((player.getLocationY() + player.getHeight()/2) / tileHeight)).getTile().getProperties().containsKey("blocked");
			 
			 if(!collisionX) {
				player.setLocationX(-player.getSpeed());
				
				recPlayer.setPosition(player.getLocationX(), player.getLocationY());
			 }
			 
			 if(!facingLeft) {
					if(facingRight) {
						for(int i = 0; i < 4; i++) 
							animationFrames[i].rotate(180);

						facingRight = false;
						
					}
					else if(facingUp) {
						for(int i = 0; i < 4; i++) 
							animationFrames[i].rotate90(false);		
						
						facingUp = false;
											
					}
					else {
						for(int i = 0; i < 4; i++) 
							animationFrames[i].rotate90(true);	

						facingDown = false;	
					}
					facingLeft = true;
				}
			 
		}
		
		if((Gdx.input.isKeyPressed(Keys.S) || (Gdx.input.isKeyPressed(Keys.DOWN))) && (player.getLocationY() > 0)) {
			//if lower tile is not blocked
			
			collisionY = blocked.getCell((int) ((player.getLocationX() + player.getWidth()/2) / tileWidth), (int) 
					(player.getLocationY() / tileHeight)).getTile().getProperties().containsKey("blocked");
			
			if(!collisionY) {
				player.setLocationY(-player.getSpeed());
				
				recPlayer.setPosition(player.getLocationX(), player.getLocationY());
			}
			 if(!facingDown) {
					if(facingRight) {
						for(int i = 0; i < 4; i++) 
							animationFrames[i].rotate90(true);
							
							facingRight = false;
						
					}
					else if(facingUp) {
						for(int i = 0; i < 4; i++) 
							animationFrames[i].rotate(180);	
							
							facingUp = false;
											
					}
					else {
						for(int i = 0; i < 4; i++) 
							animationFrames[i].rotate90(false);					
								
							facingLeft = false;	
					}
					facingDown = true;
				}
				
		}
		
		if((Gdx.input.isKeyPressed(Keys.D) || (Gdx.input.isKeyPressed(Keys.RIGHT))) && (player.getLocationX() < mapWidth-player.getWidth())) {
			//if right tile is not blocked
			collisionX = blocked.getCell((int) ((player.getLocationX() + player.getWidth()) / tileWidth), (int) 
					((player.getLocationY() + player.getWidth() / 2) / tileHeight)).getTile().getProperties().containsKey("blocked");
			
			if(!collisionX) {
				player.setLocationX(player.getSpeed());
			
				recPlayer.setPosition(player.getLocationX(), player.getLocationY());
			}
			
			 if(!facingRight) {
					if(facingLeft) {
						for(int i = 0; i < 4; i++) 
							animationFrames[i].rotate(180);
							
						facingLeft = false;
						
					}
					else if(facingUp) {
						for(int i = 0; i < 4; i++) 
							animationFrames[i].rotate90(true);		
							
							facingUp = false;
											
					}
					else {
						for(int i = 0; i < 4; i++) 
							animationFrames[i].rotate90(false);					
								
							facingDown = false;	
					}
					facingRight = true;
				}
		}
			
	
		if(bigMap)
			shiftCameraHorizontal();	//permits horizontal camera movement if the map is wider than the screen
		
		checkItemCollision();
		checkDeath();
		scoreName = "Score: " + player.getScore();		//update score count and freeze count
		freezesName = "Freezes: " + player.getFreezes();
	}

	
	public void scroll() {	//scrolls screen and updates ceiling and floor values
		
		
		if(gameState != STATE.frozen) {
			camera.translate(0, (float) scrollSpeed);
			staminaBar.setPosition(staminaBar.getX(), (float) (staminaBar.getY()+scrollSpeed));
			staminaMeter.setPosition(staminaMeter.getX(), (float) (staminaMeter.getY()+scrollSpeed));
			healthBar.setPosition(healthBar.getX(), (float) (healthBar.getY()+scrollSpeed));
			healthMeter.setPosition(healthBar.getX()+4, (float) (healthMeter.getY()+scrollSpeed));
			boostBar.setPosition(boostBar.getX(), (float) (boostBar.getY()+scrollSpeed));
			boostMeter.setPosition(boostMeter.getX(), (float) (boostMeter.getY()+scrollSpeed));
			freezeScreen.setPosition(freezeScreen.getX(),  (float) (freezeScreen.getY()+scrollSpeed));
			
			screenCeiling += (float) scrollSpeed;
			screenFloor += (float) scrollSpeed;
		}
		else {
			
			if(frozen) {
				frozen = false;

			Timer.schedule(new Task(){
				@Override
				public void run() {
					gameState = STATE.playing;
					
				}
				
			} ,5);
			}
			
				//frozen = false;
		}
		
		if(!noDebris)
			debrisFall();
		
		//TODO:	stop camera from moving up once it reaches the top of the map
		//		Or, alternatively, since stopping the camera would eliminate the possibility of death, we must take note to draw beyond the ends of our maps
		
		
	}
	
	
	public void shiftCameraHorizontal() {
		
		
			if((player.getLocationX() < centerX - (screenWidth*.15)) || (player.getLocationX() > centerX + (screenWidth*.15))) { 
		
				//TODO: 1)	add booleans or in some other way, prevent camera from translating so far as to reveal beyond the edges of the map
				//		2)	while player is off on the side of the map, prevent camera from translating towards the center every time the player moves even an inch toward center.
				
				camera.translate((float) ((player.getLocationX() - oldX)),0);	//shift camera horizontally with player																				//shift HUD horizontally
				staminaBar.translate((float) ((player.getLocationX() - oldX)),0);
				staminaMeter.translate((float) ((player.getLocationX() - oldX)),0);
				healthBar.translate((float) ((player.getLocationX() - oldX)),0);
				healthMeter.translate((float) ((player.getLocationX() - oldX)),0);
				boostBar.translate((float) ((player.getLocationX() - oldX)),0);
				boostMeter.translate((float) ((player.getLocationX() - oldX)),0);
				freezeScreen.translate((float) ((player.getLocationX() - oldX)), 0);
				
				
				
			}	
			
			if((player.getLocationX() < centerX - (screenWidth*.15)))	{		//shifts score board appropriately if the player moves laterally
					scoreX = player.getLocationX() - 230;	//TODO: change magic numbers to proportional numbers
					freezesX = player.getLocationX() - 230;
					pointGoalX = player.getLocationX() + 53;
			}
			if((player.getLocationX() > centerX + (screenWidth*.15))) {
					scoreX = player.getLocationX() - 470;
					freezesX = player.getLocationX() - 470;
					pointGoalX = player.getLocationX() - 188;
			}
	}
	
	public void checkItemCollision() {
		
		//VICTORY CHECK
		//checks if player has overlapped a pellet, takes appropriate action
		if(recPlayer.overlaps(goal)) {							//show the results screen when goal is reached
			//SoundManager.delayFun.stop();
			if(game.completions == 4) 
				Gdx.app.exit();
			hide();
			//dispose();
			
			gameState = STATE.completed;
			//results screen
			if(player.getScore() >= pointGoal) {
				SoundManager.victory.play(0.8f);
				game.completions++;
				game.resultsScreen.create();
				game.resultsScreen.setDied(false);
				game.setScreen(game.resultsScreen);
			}
			else {
				game.failureScreen.create();
				game.setScreen(game.failureScreen);
			}
		}
		
				for(int i = 0; i < staminaPellets.size(); i++) {
					if(recPlayer.overlaps(staminaPellets.get(i).getRectangle()) && player.getStamina() < player.getMaxStamina()) {
						staminaPellets.get(i).setVisible(false);
						player.setStamina(70);
						player.resetSpeed();
						if(soundOn)
							SoundManager.staminaPickup.play(0.3f);
						break;	//replenish sound
						}
					}
				
				for(int i = 0; i < healthPellets.size(); i++) {
					if(recPlayer.overlaps(healthPellets.get(i).getRectangle()) && player.getHealth() < player.getMaxHealth()) {
						healthPellets.get(i).setVisible(false);
						player.setHealth(50);
						if(soundOn)
							SoundManager.healthPickup.play(0.3f);
						break;	//health sound?
					}
				}
				
				for(int i = 0; i < pointPellets.size(); i++) {
					if(recPlayer.overlaps(pointPellets.get(i).getRectangle())) {
						pointPellets.get(i).setVisible(false);
						if(player.getScore() == pointGoal - 25 && soundOn)
							SoundManager.goalReached.play(0.6f);
						player.setScore(25);
						if(soundOn)
							SoundManager.pointPickup.play(.1f);
						
						break;	//ding sound
					}
				}
				
				//checks if player got hit by debris (bad pellets)
				if(!noDebris) {
					for(int i = 0; i < debris.size(); i++) {
						if(recPlayer.overlaps(debris.get(i).getRectangle())) {
							debris.get(i).setVisible(false);
							player.setHealth(-40);
							if(soundOn)
								SoundManager.debrisImpact.play(0.2f);
							break;	//ouch sound
						}
					}
				}
				
				for(int i = 0; i < freezePellets.size(); i++) {
					if(recPlayer.overlaps(freezePellets.get(i).getRectangle())) {
						freezePellets.get(i).setVisible(false);
						player.incrFreeze();
						break;	//powerup sound
					}
				}
				
				for(int i = 0; i < hazards.size(); i++) {
					if(recPlayer.overlaps(hazards.get(i))) {
						player.setHealth(-3);	
						if(soundOn)
							SoundManager.lava.play();
							
						break;	//sizzling sound
					}
				}
				
	}
	
	public void debrisFall() {
		//make debris fall 4x the scrollSpeed
				for(int i = 0; i < debris.size(); i++) {
					debris.get(i).setLocationY((float) -scrollSpeed*4);
				}
			
				if(debris.get(0).getY() < screenFloor) {				//respawn debris above the screen once it follows below
					for(int i = 0; i < debris.size(); i++) {
						debris.get(i).setVisible(true);
						debris.get(i).setLocationY(screenCeiling + 50);
					}
				}	
				
	}
	
	public void drawItems() {
		//draws pellets still in play, removes pellets that have been picked up and stops drawing them
		for(int i = 0; i < staminaPellets.size(); i++) {
        	if(staminaPellets.get(i).isVisible())				//if the item has not already been collided, continue drawing it
        		staminaPellets.get(i).getSprite().draw(batch);
        	else 
				staminaPellets.get(i).remove();
			
        }
        
        for(int i = 0; i < healthPellets.size(); i++) {
        	if(healthPellets.get(i).isVisible())
        		healthPellets.get(i).getSprite().draw(batch);
        	else 
				healthPellets.get(i).remove();
			
        }
        
        for(int i = 0; i < pointPellets.size(); i++) {
        	if(pointPellets.get(i).isVisible())
        		pointPellets.get(i).getSprite().draw(batch);
        	else 
				pointPellets.get(i).remove();
			
        }
        if(!noDebris) {
	        for(int i = 0; i < debris.size(); i++) {
	        	if(debris.get(i).isVisible())
	        		debris.get(i).getSprite().draw(batch);
	        	else 
	        		debris.get(i).remove();
	        }
        }
        
        for(int i = 0; i < freezePellets.size(); i++) {
        	if(freezePellets.get(i).isVisible())
        		freezePellets.get(i).getSprite().draw(batch);
        	else
        		freezePellets.get(i).remove();
        }
        
        if(goalPellets.get(0).isVisible())
        	goalPellets.get(0).getSprite().draw(batch);
        else 
        	goalPellets.get(0).remove();
        
        
	}
	
	public void pauseCheck() {
		//pause check
        if((Gdx.input.isKeyJustPressed(Keys.P)) && gameState == STATE.playing)
			pause();
			
		else if((Gdx.input.isKeyJustPressed(Keys.R)) && gameState == STATE.paused) 
				resume();
			
				
		
        
        if((Gdx.input.isKeyJustPressed(Keys.SPACE)) && gameState != STATE.frozen && player.getFreezes() > 0) {
        	frozen = true;
        	gameState = STATE.frozen;
        	if(soundOn)
        		SoundManager.freezeActivation.play(0.3f);
        	player.deductFreeze();
        }
	}
	
	public void boostCheck() {					//SPEED BOOST
		if((Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) && gameState != STATE.intro) {
			if(player.getLocationX() != oldX || player.getLocationY() != oldY) {
				player.setBoost(-1.5);					
				isBoosting = false;//while left shift is held, boost speed
			}
			if(player.getBoost() > 0.0) {
				isBoosting = true;
				player.boostSpeed();	
				if(Gdx.input.isKeyJustPressed(Keys.SHIFT_LEFT)&&soundOn)
					SoundManager.boostActivation.play(0.1f);
			}
			else {
				player.resetSpeed();
				isBoosting = false;
				
			}
				
			
		}
		if((!Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))) {
			player.resetSpeed();
			isBoosting = false;
		}
		
	}
	
	
	public void updateStaminaAndHealthandBoost() {
		
		 if(player.getStamina() > 0.0) {
			
			 if((player.getLocationX() != oldX || player.getLocationY() != oldY)) {//if player still has stamina, deplete stamina meter.
				player.setStamina(-0.4);	//change this number to alter stamina depletion rate.
				staminaMeter.setSize((float)player.getStamina(), staminaBar.getHeight()/4);	//stamina meter width dependent on player stamina
			 }
			}
			else {
				//when stamina runs out, reduce speed.
				if(!isBoosting) 
					player.setSpeed(0.5); //reduces speed by ~75%
				else
					player.setSpeed(1.5);
				
			}
	        
		 //updates health meter
	        healthMeter.setSize((float) player.getHealth(), healthBar.getHeight()/4);
	        boostMeter.setSize((float) player.getBoost(), boostBar.getHeight()/4);
	      
	        
	     if(player.getBoost() < player.getMaxBoost()) {
	    	 player.setBoost(0.3);
	    	 boostMeter.setSize((float)player.getBoost(), boostBar.getHeight()/4);
	     }     
	     
	     
	}
	
	
	/*public void introDelay() {
		//creates a delay from when the level loads, and you start playing 
		
		if(intro) {
			intro = false;
			startUp = false;
			Timer.schedule(new Task() {
				
				@Override
				public void run() {
					gameState = STATE.playing;
				}
			}, 3);		//3 seconds
		}
	
	}*/
	
	
	
	public void checkDeath() {
		
		 if((recPlayer.getY() + recPlayer.getHeight() <= screenFloor) || player.getHealth() == 0) {	//if player reaches screenFloor = death
			System.out.println("YOU LOST :(");
			player.death();
			game.helpScreen.setDied(true);
			game.resultsScreen.setDied(true);
			game.helpScreen.setPlaying(true);
			if(player.getLives() == 0) {
				//game over screen
				game.mainMenu.create();
				game.setScreen(game.mainMenu);
			}
			else {
				//death screen
				hide();
				game.death.create();
				game.setScreen(game.death);
			}
		}
		 
	}
	
	
	
	@Override
	public void dispose() {	//TODO: this method currently does not work, fails to dispose and crashes program.
		map.dispose();
		batch.dispose();
		renderer.dispose();
		staminaBar.getTexture().dispose();
		staminaMeter.getTexture().dispose();
		healthBar.getTexture().dispose();
		healthMeter.getTexture().dispose();
		boostMeter.getTexture().dispose();
		boostBar.getTexture().dispose();
		
		//others to dispose?
	
	}
	
	
	public void setLevelPath(String levelName) {
		levelPath = levelName;
	}
	
	public void setScrollSpeed(double x) {
		scrollSpeed = x;
	}
	
	public void setMovementSpeed(double x) {
		player.setFullSpeed(x);
	}
	
	
									//interface methods from Screen
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	

	@Override
	public void hide() {
		
	}


	@Override
	public void resize(int width, int height) {
		
		
	}


	@Override
	public void pause() {
		gameState = STATE.paused;
		
		
	}


	@Override
	public void resume() {
		gameState = STATE.playing;
		
	}
	
	

	
	
	
	
	

}
