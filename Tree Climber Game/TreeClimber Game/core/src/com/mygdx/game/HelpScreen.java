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

public class HelpScreen extends Game implements Screen{

	private MyGame game;
	private Stage stage;
	private SpriteBatch batch;
	private Texture instructions;
	private TextButton buttonBack;
	private Skin skin;
	private boolean playing, died, failure;

	
	public HelpScreen(MyGame game) {
		this.game = game;
	}
	
	

	@Override
	public void create() {
		batch = new SpriteBatch();
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		playing = false;
		died = false;
	
		
		skin = new Skin();
		Pixmap pixmap = new Pixmap(300, 100, Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		
		skin.add("white", new Texture(pixmap));
		
		BitmapFont bfont = new BitmapFont(Gdx.files.internal("font/black.fnt"));
		
		skin.add("default",  bfont);
		
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.newDrawable("white", Color.WHITE);
		textButtonStyle.down = skin.newDrawable("white", Color.LIGHT_GRAY);
		textButtonStyle.checked = skin.newDrawable("white", Color.LIGHT_GRAY);
		textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
		

		

		textButtonStyle.font = skin.getFont("default");
		skin.add("default", textButtonStyle);
		
	
		
		buttonBack = new TextButton("BACK", textButtonStyle);
		buttonBack.setPosition(Gdx.graphics.getWidth()-200, 10);
		buttonBack.setSize(150, 50);
		buttonBack.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				hide();
				dispose();
				
				if(playing == false) {
					hide();
					SoundManager.delayFun.stop();
					SoundManager.spooky.stop();
					game.mainMenu.create();
					game.setScreen(game.mainMenu);
				}
				else if(died == true){
					hide();
					game.death.create();
					game.setScreen(game.death);
				}
				else if(failure == true)	{
					hide();
					game.failureScreen.create();
					game.setScreen(game.failureScreen);
				}
				else {
					hide();
					game.resultsScreen.create();
					game.setScreen(game.resultsScreen);
				}
				
				
			}
		});
		
		instructions  = new Texture("Maps/HowToPlay.jpg");
		
		TextureRegion region = new TextureRegion(instructions, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Image background = new Image(region);
		
	
		
		stage.addActor(background);
		stage.addActor(buttonBack);
		
		
			
		
		
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		

		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
       
	
        batch.begin();
        
        stage.act();
     
    
        stage.draw();
      
       
        
        batch.end();
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}
	
	public void setPlaying(boolean playing) {
		this.playing = playing;
	}
	
	public boolean isPlaying() {
		return playing;
	}
	
	public void setDied(boolean died) {
		this.died = died;
	}
	
	public boolean isDead() {
		return died;
	}
	
	public void setFailure(boolean failure) {
		this.failure = failure;
	}
	
	public boolean failed() {
		return failure;
	}

}
