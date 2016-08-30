package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;

/** 
 * @Author Darren Kriner, Mitchell Nye
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
 * 1) moving camera reveals beyond the edges of the map (mostly horizontal issue, but also vertical once player reaches end of level, camera keeps going)
 * 2) player spawn location needs to accommodate maps wider than screen width, and if we ever want it to be custom (like not always in the middle of the screen)
 * 3) HUD jolts when camera begins scrolling horizontally
 * 4) for big maps, when player veers to the far left or right, horizontal camera follow is too strong, shouldn't begin translating back to center until player is returning to center.
 * 
 * =======THINGS TO IMPLEMENT (TO-DO)========
 * 
 * 
 * 1) new horizontal scroll system?	*in progress*
 * 2) implement death screen (dispose method, new Death());
 * 3) Health Bar? *temp added*
 * 4) end of level/goal
 * 5) more items, de-spawning of used items, produce items in arraylists *in progress*
 * 6) updated visuals
 * 7) add start screen & main menu
 * 8) insert more hazards
 * 9) points system
 * 
 * */

public class Level extends ApplicationAdapter implements Screen{
	
	private TreeClimber mainMenu;
	
	//private ArrayList<Rectangle> blockers;
	private ArrayList<StaminaPellet> staminaPellets;
	private ArrayList<FallingItem> debris;
	private ArrayList<HealthPellet> healthPellets;
	private ArrayList<PointPellet> pointPellets;
	private TiledMap map;	//map
	private TiledMapTileLayer blocked;	
	private MapProperties prop;									
	
	private Sprite staminaBar;
	private Sprite staminaMeter;
	private Sprite healthBar;
	private Sprite healthMeter;

	
	private OrthogonalTiledMapRenderer renderer;	//renders map
	private OrthographicCamera camera;	//used to view map, used for scroll
	
	private Player player;
	

	private SpriteBatch batch;

	
	private Rectangle recPlayer;
	
	
	private float screenHeight;
	private float screenWidth;
	private float centerX;
	private float screenCeiling;
	private float screenFloor;
	private float oldX;
	private int mapWidth;
	private int mapHeight;
	//private float gameTime;
	private double scrollSpeed;
	private boolean bigMap;
	
	private enum STATE {		//started playing with this weird notation. basically whatever state is set to will determine what happens
								//ie if the game is in game state, load level, if menu state, load main menu.
								//this hasn't been fully implemented yet. When you figure out how to make the menu screens, use state = menu
		MENU,
		GAME
	};
	
	private STATE state = STATE.MENU;
	
	
	
	@Override
	public void create() {
		state = STATE.GAME;
		mainMenu = new TreeClimber();
		
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
		centerX = (float) ((screenWidth/2));	//centerX used for translating camera horizontally
		screenCeiling = screenHeight;
		screenFloor = 0;

		//gameTime = 0.0f;
		
		//necessary junk to render map
		
		map = new TmxMapLoader().load("maps/map3.tmx");
		blocked = (TiledMapTileLayer) map.getLayers().get(0);
		prop = map.getProperties();
		
		mapWidth = prop.get("width", Integer.class)*prop.get("tilewidth", Integer.class);			//gets dimensions of map in pixels
		mapHeight = prop.get("height", Integer.class)*prop.get("tileheight", Integer.class);
		
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false,(screenWidth),(screenHeight));
		camera.update();	
		renderer = new OrthogonalTiledMapRenderer(map);
		
		player = new Player("TestSqaure.jpg");
		
		oldX = player.getLocationX();
		
		//player collision rectangle
		recPlayer = new Rectangle(player.getLocationX(), player.getLocationY(), player.getWidth(), player.getHeight());
		batch = new SpriteBatch();
		
		
		//creating HUD
		healthBar = new Sprite(new Texture("healthbar.png"));
		healthBar.setSize(screenWidth/5,screenHeight/18);
		healthBar.setPosition((float) (screenWidth*.75), (float) (screenHeight*.905));	
		healthMeter = new Sprite(new Texture("healthmeter.png"));
		healthMeter.setPosition(healthBar.getX() +3 ,healthBar.getY() + 4);
		healthMeter.setSize((float) player.getHealth(), 8);
		
		staminaBar = new Sprite(new Texture("staminabar.png"));
		staminaBar.setSize(healthBar.getWidth(), healthBar.getHeight());
		staminaBar.setPosition((float) (screenWidth*.75), (float) (.85*screenHeight));
		staminaMeter = new Sprite(new Texture("staminameter.png"));
		//stamina meter setSize located in render
		staminaMeter.setPosition(staminaBar.getX() + 3, staminaBar.getY() + 3);
		
		
		bigMap  = (mapWidth > screenWidth);	
		if(bigMap) {								//enable horizontal camera movement
			centerX += (mapWidth - screenWidth)/2;
			camera.translate((mapWidth-screenWidth)/2, 0);
			staminaBar.translate((mapWidth-screenWidth)/2, 0);
			staminaMeter.translate((mapWidth-screenWidth)/2, 0);
			healthBar.translate((mapWidth-screenWidth)/2, 0);
			healthMeter.translate((mapWidth-screenWidth)/2, 0);
			player.setLocationX((mapWidth - screenWidth)/2);
			oldX = player.getLocationX();
			camera.update();
			System.out.println("centerx = " + centerX);
		}
		
		
		debris = new ArrayList<FallingItem>();
		debris.add(new FallingItem(50,(int) (screenCeiling + 100)));
		debris.add(new FallingItem(150,(int) (screenCeiling + 100)));
		debris.add(new FallingItem(250,(int) (screenCeiling + 100)));
		//debris.add(new FallingItem(300,(int) (screenCeiling + 100)));
		debris.add(new FallingItem(500,(int) (screenCeiling + 100)));
		debris.add(new FallingItem(750,(int) (screenCeiling + 100)));
		debris.add(new FallingItem(600,(int) (screenCeiling + 100)));
		//debris.add(new FallingItem(256,(int) (screenCeiling + 100)));
		debris.add(new FallingItem(350,(int) (screenCeiling + 100)));
		debris.add(new FallingItem(450,(int) (screenCeiling + 100)));
		debris.add(new FallingItem(400,(int) (screenCeiling + 100)));
		
		
		
		staminaPellets = new ArrayList<StaminaPellet>();
		healthPellets = new ArrayList<HealthPellet>();
		pointPellets = new ArrayList<PointPellet>();
		
		for(int i = 0; i < (mapWidth/prop.get("tilewidth", Integer.class)); i++ ) {
			for(int j = 0; j < (mapHeight/prop.get("tileheight", Integer.class)); j++ ) {
				
				if(blocked.getCell(i, j).getTile().getProperties().containsKey("stamina")) 								//reads all tiles in uploaded map, and checks for custom properties
					staminaPellets.add(new StaminaPellet(i*16, j*16));													//if a custom property is found, a corresponding pellet is created and drawn
																														//in that tile's location
				else if(blocked.getCell(i, j).getTile().getProperties().containsKey("health"))
					healthPellets.add(new HealthPellet(i*16, j*16));
				
				else if(blocked.getCell(i, j).getTile().getProperties().containsKey("points"))
					pointPellets.add(new PointPellet(i*16, j*16));
			}
			
			
		}
		
		scrollSpeed = 1.4;
	}
	
	
	@Override
	public void render() {
		//more render junk
		Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        renderer.setView(camera);
        renderer.render();
        batch.setProjectionMatrix(camera.combined);			//fixes issue of collisions being affected by scrolling
        //gameTime += Gdx.graphics.getDeltaTime();
        
        
       
        
        batch.begin();
        				
        if(state == state.GAME) {
        			//drawing things
        drawItems();
        staminaBar.draw(batch);	
        healthBar.draw(batch);
        healthMeter.draw(batch);
        staminaMeter.draw(batch);
        batch.draw(player.getModel(), player.getLocationX(), player.getLocationY());	
        updateStaminaAndHealth();
        
        update();	
        scroll();
        
        }
        
        else if(state == state.MENU) {
        	
        	
        	//mainMenu.render();
        	
        }
        
        batch.end();
        
		
        
	}

	
	
	public void update() {
		
		
		boolean collisionX = false, collisionY = false; 
		float tileWidth = blocked.getTileWidth();
		float tileHeight = blocked.getTileHeight();
		oldX = player.getLocationX();
		
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
		}
		
		if((Gdx.input.isKeyPressed(Keys.A) || (Gdx.input.isKeyPressed(Keys.LEFT))) && (player.getLocationX() > 0 + player.getWidth())) {
			//if left tile is not a collision block
			 collisionX = blocked.getCell((int) (player.getLocationX() / tileWidth), (int) 
					 ((player.getLocationY() + player.getHeight()/2) / tileHeight)).getTile().getProperties().containsKey("blocked");
			 
			 if(!collisionX) {
				player.setLocationX(-player.getSpeed());
				
				recPlayer.setPosition(player.getLocationX(), player.getLocationY());
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
			 
				
		}
		
		if((Gdx.input.isKeyPressed(Keys.D) || (Gdx.input.isKeyPressed(Keys.RIGHT))) && (player.getLocationX() < mapWidth-player.getWidth())) {
			//if right tile is not blocked
			collisionX = blocked.getCell((int) ((player.getLocationX() + player.getWidth()) / tileWidth), (int) 
					((player.getLocationY() + player.getWidth() / 2) / tileHeight)).getTile().getProperties().containsKey("blocked");
			
			if(!collisionX) {
				player.setLocationX(player.getSpeed());
			
				recPlayer.setPosition(player.getLocationX(), player.getLocationY());
			}
			
		}
	
	 
	 checkItemCollision();
	 checkDeath();
	}

	
	public void scroll() {	//scrolls screen and updates ceiling and floor values
		
		
			
		camera.translate(0, (float) scrollSpeed);
		staminaBar.setPosition(staminaBar.getX(), (float) (staminaBar.getY()+scrollSpeed));
		staminaMeter.setPosition(staminaMeter.getX(), (float) (staminaMeter.getY()+scrollSpeed));
		healthBar.setPosition(healthBar.getX(), (float) (healthBar.getY()+scrollSpeed));
		healthMeter.setPosition(healthBar.getX(), (float) (healthMeter.getY()+scrollSpeed));
		screenCeiling += (float) scrollSpeed;
		screenFloor += (float) scrollSpeed;
		debrisFall();
		
		if(bigMap)
			shiftCameraHorizontal();	//permits horizontal camera movement if the map is wider than the screen
		
		
	}
	
	
	public void shiftCameraHorizontal() {
		
			if((player.getLocationX() < centerX - (screenWidth*.13)) || (player.getLocationX() > centerX + (screenWidth*.12))) { // && include another boolean to prevent showing beyond edges of map
				
				camera.translate((float) ((player.getLocationX() - oldX)*.8),0);	//shift camera horizontally
				
																								//shift HUD horizontally
				staminaBar.translate((float) ((player.getLocationX() - oldX)*.8),0);
				staminaMeter.translate((float) ((player.getLocationX() - oldX)*.8),0);
				healthBar.translate((float) ((player.getLocationX() - oldX)*.8),0);
				healthMeter.translate((float) ((player.getLocationX() - oldX)*.8),0);
			}	
		
	}
	
	public void checkItemCollision() {
		
		
		//checks if player has overlapped a pellet, takes appropriate action
				for(int i = 0; i < staminaPellets.size(); i++) {
					if(recPlayer.overlaps(staminaPellets.get(i).getRectangle()) && player.getStamina() < player.getMaxStamina()) {
						staminaPellets.get(i).setVisible(false);
						player.setStamina(70);
						player.setSpeed(6.0);
						break;
						}
					}
				
				for(int i = 0; i < healthPellets.size(); i++) {
					if(recPlayer.overlaps(healthPellets.get(i).getRectangle()) && player.getHealth() < player.getMaxHealth()) {
						healthPellets.get(i).setVisible(false);
						player.setHealth(50);
						break;
					}
				}
				
				for(int i = 0; i < pointPellets.size(); i++) {
					if(recPlayer.overlaps(pointPellets.get(i).getRectangle())) {
						pointPellets.get(i).setVisible(false);
						player.setScore(25);
						break;
					}
				}
				
				//checks if player got hit by debris (bad pellets)
				for(int i = 0; i < debris.size(); i++) {
					if(recPlayer.overlaps(debris.get(i).getRectangle())) {
						debris.get(i).setVisible(false);
						player.setHealth(-30);
						break;
					}
				}
	}
	
	public void debrisFall() {
		//make debris fall 4x the scrollSpeed
				for(int i = 0; i < debris.size(); i++) {
					debris.get(i).setLocationY((float) -scrollSpeed*4);
				}
				
				if(debris.get(0).getY() < screenFloor) {
					for(int i = 0; i < debris.size(); i++) {
						debris.get(i).setVisible(true);
						debris.get(i).setLocationY(screenCeiling + 50);
					}
				}	
				
	}
	
	public void drawItems() {
		//draws pellets still in play, removes pellets that have been picked up and stops drawing them
		for(int i = 0; i < staminaPellets.size(); i++) {
        	if(staminaPellets.get(i).isVisible())
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
        
        for(int i = 0; i < debris.size(); i++) {
        	if(debris.get(i).isVisible())
        		debris.get(i).getSprite().draw(batch);
        	else 
        		debris.get(i).remove();
        }
	}
	public void updateStaminaAndHealth() {
		
		 if(player.getStamina() > 0.0) {	//if player still has stamina, deplete stamina meter.
				player.setStamina(-0.4);	//change this number to alter stamina depletion rate.
				staminaMeter.setSize((float)player.getStamina(), staminaBar.getHeight()/4);	//stamina meter width dependent on player stamina
			}
			else {	//when stamina runs out, reduce speed.
				player.setSpeed(0.5); //reduces speed by ~75%
			}
	        
		 //updates health meter
	        healthMeter.setSize((float) player.getHealth(), healthBar.getHeight()/4);
	}
	
	
	
	public void checkDeath() {
		
		 if((recPlayer.getY() + recPlayer.getHeight() <= screenFloor) || player.getHealth() == 0) {	//if player reaches screenFloor = death
			//game over
			//new Death();
			//dispose();
			Gdx.app.exit();
			System.out.println("YOU LOSE :(");
			}
		 
	}
	
	public void checkVictory() {
		/*if(recPlayer.getY() + recPlayer.getHeight() >= *map height*) || recPlayer.overlaps winning pellet {
		 * new victory screen()
		 * record score()
		 Gdx.app.exit();
		 System.out.println("YOU WIN!!!");
	 }*/
	 
	}
	
	
	
	@Override
	public void dispose() {	//how to delete everything and create a new death screen?
		map.dispose();
		batch.dispose();
		renderer.dispose();
		//others to dispose?
	
	}
	
	
									//interface methods from Screen
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	
	
	
	
	

}
