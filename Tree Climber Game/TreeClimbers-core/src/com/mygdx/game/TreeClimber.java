package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class TreeClimber extends Game implements Screen{

	private Stage stage;
	private SpriteBatch batch;
	private Texture backdrop;
	private TextButton buttonPlay, buttonHelp, buttonExit;
	private Skin skin;
	private MyGame game;
	private Level play;
	private boolean soundOn;
	//private Sprite leaf1, leaf2;
	
	
	
	//add buttons

	public TreeClimber(MyGame game) {
		this.game = game;
	}
	
	@Override
	public void create() {
		
		soundOn = true;
		
		batch = new SpriteBatch();
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		game.helpScreen.setPlaying(false);
		
		play = new Level(game);

		// A skin can be loaded via JSON or defined programmatically, either is fine. Using a skin is optional but strongly
		// recommended solely for the convenience of getting a texture, region, etc as a drawable, tinted drawable, etc.
		skin = new Skin();
		// Generate a 1x1 white texture and store it in the skin named "white".
		Pixmap pixmap = new Pixmap(300, 100, Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();

		skin.add("white", new Texture(pixmap));

		// Store the default libgdx font under the name "default".
		BitmapFont bfont=new BitmapFont(Gdx.files.internal("font/black.fnt"));
	
	
		skin.add("default",bfont);

		// Configure a TextButtonStyle and name it "default". Skin resources are stored by type, so this doesn't overwrite the font.
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.newDrawable("white", Color.CLEAR);
		textButtonStyle.down = skin.newDrawable("white", Color.YELLOW);
		textButtonStyle.checked = skin.newDrawable("white", Color.LIGHT_GRAY);
		textButtonStyle.over = skin.newDrawable("white", Color.YELLOW);
	
		textButtonStyle.font = skin.getFont("default");
		
		skin.add("default", textButtonStyle);
		
		//background wallpaper
		backdrop = new Texture("Images/titleScreen.png");
		TextureRegion region = new TextureRegion(backdrop, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Image background = new Image(region);
	
		
		
		

		// Create a button with the "default" TextButtonStyle. A 3rd parameter can be used to specify a name other than "default".
		buttonPlay = new TextButton("PLAY",textButtonStyle);
		buttonPlay.setPosition(Gdx.graphics.getWidth()/1.78f, Gdx.graphics.getHeight()/2);
		buttonPlay.setSize(300, 50);
		
		buttonPlay.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(soundOn)
					SoundManager.mainMenu.stop();
				hide();
				dispose();
				
				//state = state.GAME;
				
				// ((Game) Gdx.app.getApplicationListener()).setScreen(playLevel);
				play.create();
				 game.setScreen(play);
				 
				
			}
		});
	
		
		
		buttonHelp = new TextButton("HOW TO PLAY", textButtonStyle);
		buttonHelp.setPosition(Gdx.graphics.getWidth()/1.78f, Gdx.graphics.getHeight()/3);
		buttonHelp.setSize(300,50);
		buttonHelp.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				
				hide();
				dispose();
				//helpScreen();
				game.helpScreen.create();
				game.setScreen(game.helpScreen);
			}
		});
		
		 
		buttonExit = new TextButton("EXIT", textButtonStyle);
		buttonExit.setPosition(Gdx.graphics.getWidth()/1.78f, Gdx.graphics.getHeight()/6);
		buttonExit.setSize(300,50);
		buttonExit.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				dispose();
				Gdx.app.exit();
			}
		});
		
		
		stage.addActor(background);
		stage.addActor(buttonPlay);
		stage.addActor(buttonHelp);
		stage.addActor(buttonExit);
		
		
		//sound = new SoundManager();
		if(soundOn) {
		SoundManager.mainMenu.setLooping(true);
		SoundManager.mainMenu.setVolume(0.5f);
		SoundManager.mainMenu.play();
		}

	}

	

	@Override
	public void show() {
		
		
	}

	@Override
	public void render(float delta) {
	
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
       
	
        batch.begin();
      
        //menuBackground.draw(batch);
        stage.act();
		stage.draw();
		
        //add buttons
        	//if 'start' pressed
        			//level = new level();
        			//level.setGameState(GAME);
        			//level.setLevelPath(String nextLevel);
        			//batch.end()?, dispose()?
        			//level.render();
        
        batch.end();
        
	}

	@Override
	public void resize(int width, int height) {
	
		
	}

	@Override
	public void pause() {
		//this.pause();
		
	}

	@Override
	public void resume() {

		
	}

	@Override
	public void hide() {
			
	}

	@Override
	public void dispose() {
		
		stage.dispose();
		skin.dispose();
		batch.dispose();
		
	   
		
	}
	
	
	
}