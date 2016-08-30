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

public class FailureScreen extends Game implements Screen {
	
	private SpriteBatch batch;
	private Texture backdrop;
	private TextButton buttonRetry, buttonHelp, buttonQuit;
	private Skin skin;
	private MyGame game;
	private Stage stage;
	private Level play;
	
	
	public FailureScreen(MyGame game) {
		this.game = game;
	}

	@Override
	public void create() {
	
		batch = new SpriteBatch();
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		skin = new Skin();
		Pixmap pixmap = new Pixmap(300,100, Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		
		play = new Level(game);
		
		skin.add("white", new Texture(pixmap));
		BitmapFont bfont = new BitmapFont(Gdx.files.internal("font/black.fnt"));
		skin.add("default", bfont);
		
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.newDrawable("white", Color.WHITE);
		textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
		textButtonStyle.checked = skin.newDrawable("white", Color.LIGHT_GRAY);
		textButtonStyle.over = skin.newDrawable("white", Color.DARK_GRAY);
		textButtonStyle.font = skin.getFont("default");
		
		skin.add("default", textButtonStyle);
		
		backdrop = new Texture("Images/failureScreen.png");
		TextureRegion region = new TextureRegion(backdrop, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Image background = new Image(region);
		
		//making the pause screen buttons
		buttonRetry = new TextButton("RETRY", textButtonStyle);
		buttonRetry.setSize(300, 50);
		buttonRetry.setPosition((Gdx.graphics.getWidth()/3), Gdx.graphics.getHeight()-175);
		buttonRetry.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				hide();
				play.create();
				game.setScreen(play);
			}
		});
		
		buttonHelp = new TextButton("HELP", textButtonStyle);
		buttonHelp.setSize(300, 50);
		buttonHelp.setPosition((Gdx.graphics.getWidth()/3), Gdx.graphics.getHeight()-235);
		buttonHelp.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				hide();
				game.helpScreen.create();
				game.helpScreen.setFailure(true);
				game.helpScreen.setPlaying(true);
				game.setScreen(game.helpScreen);
			}
		});
		
		buttonQuit = new TextButton("QUIT", textButtonStyle);
		buttonQuit.setSize(300,50);
		buttonQuit.setPosition((Gdx.graphics.getWidth()/3), Gdx.graphics.getHeight()-295);
		buttonQuit.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				hide();
				SoundManager.delayFun.stop();
				SoundManager.spooky.stop();
				game.mainMenu.create();
				game.setScreen(game.mainMenu);
			}
		});
		
		
		stage.addActor(background);
		stage.addActor(buttonHelp);
		stage.addActor(buttonRetry);
		stage.addActor(buttonQuit);
		
		
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
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		skin.dispose();
		batch.dispose();
		stage.dispose();
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

}
