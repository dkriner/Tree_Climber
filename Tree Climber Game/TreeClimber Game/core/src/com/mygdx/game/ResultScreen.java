package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
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

public class ResultScreen extends Game implements Screen {
	
	private MyGame game;
	private Skin skin;
	private Texture backdrop;
	private TextButton buttonContinue, buttonHelp, buttonQuit;
	private SpriteBatch batch;
	private Stage stage;
	private boolean died;

	public ResultScreen(MyGame game) {
		this.game = game;
	}
	
	@Override
	public void create() {
		batch = new SpriteBatch();
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		died = false;
		
		Pixmap pixmap = new Pixmap(300,100, Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		
		skin = new Skin();
		skin.add("white", new Texture(pixmap));
		BitmapFont bfont = new BitmapFont(Gdx.files.internal("font/black.fnt"));
		skin.add("default", bfont);
		
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.newDrawable("white", Color.WHITE);
		textButtonStyle.down = skin.newDrawable("white", Color.YELLOW);
		textButtonStyle.checked = skin.newDrawable("white", Color.LIGHT_GRAY);
		textButtonStyle.over = skin.newDrawable("white", Color.YELLOW);
		textButtonStyle.font = skin.getFont("default");
		
		skin.add("default", textButtonStyle);
		
		
		
		backdrop = new Texture("Images/resultScreen.png");
		TextureRegion region = new TextureRegion(backdrop, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Image background = new Image(region);
		
		//making the pause screen buttons
		buttonContinue = new TextButton("CONTINUE", textButtonStyle);
		buttonContinue.setSize(300, 50);
		buttonContinue.setPosition((Gdx.graphics.getWidth()/3), Gdx.graphics.getHeight()-75);
		buttonContinue.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.playLevel.create();
				game.playLevel.startUp = true;
				game.setScreen(game.playLevel);
			}
		});
		
		buttonHelp = new TextButton("HELP", textButtonStyle);
		buttonHelp.setSize(300, 50);
		buttonHelp.setPosition((Gdx.graphics.getWidth()/3), Gdx.graphics.getHeight()-135);
		buttonHelp.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				hide();
				game.helpScreen.create();
				game.helpScreen.setPlaying(true);
				game.helpScreen.setDied(false);
				game.setScreen(game.helpScreen);
			}
		});
		
		buttonQuit = new TextButton("QUIT", textButtonStyle);
		buttonQuit.setSize(300,50);
		buttonQuit.setPosition((Gdx.graphics.getWidth()/3), Gdx.graphics.getHeight()-195);
		buttonQuit.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				hide();
				dispose();
				SoundManager.delayFun.stop();
				game.completions = 0;
				game.mainMenu.create();
				game.setScreen(game.mainMenu);
			}
		});
		
		stage.addActor(background);
		stage.addActor(buttonHelp);
		stage.addActor(buttonContinue);
		stage.addActor(buttonQuit);
		
		//boolean soundOn = true;
		//if(soundOn)
		
		//SoundManager.victory.dispose();
				
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		batch.begin();
		stage.draw();
		stage.act();
		
		batch.end();
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}
	
	public void setDied(boolean died) {
		this.died = died;
	}
	
	public boolean isDead() {
		return died;
	}

}
