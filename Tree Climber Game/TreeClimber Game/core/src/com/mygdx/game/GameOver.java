package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class GameOver extends Game implements Screen {
	private SpriteBatch batch;
	private TextButton buttonMainMenu, buttonQuit;
	private BitmapFont gameOver;
	private Skin skin;
	private MyGame game;
	private Stage stage;
	private Level play;
	
	
	public GameOver(MyGame game) {
		this.game = game;
	}

	@Override
	public void create() {
		play = new Level(game);
		batch = new SpriteBatch();
		
		Pixmap pixmap = new Pixmap(300,100, Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		
		skin = new Skin();
		skin.add("white", new Texture(pixmap));
		BitmapFont bfont = new BitmapFont(Gdx.files.internal("font/black.fnt"));
		skin.add("default", bfont);
		
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.newDrawable("white", Color.CLEAR);
		textButtonStyle.down = skin.newDrawable("white", Color.YELLOW);
		textButtonStyle.checked = skin.newDrawable("white", Color.LIGHT_GRAY);
		textButtonStyle.over = skin.newDrawable("white", Color.YELLOW);
		textButtonStyle.font = skin.getFont("default");
		
		skin.add("default", textButtonStyle);
		
		gameOver = new BitmapFont();
		gameOver.setColor(Color.RED);
		
		//making the pause screen buttons
		buttonMainMenu = new TextButton("MAIN MENU", textButtonStyle);
		buttonMainMenu.setSize(300, 50);
		buttonMainMenu.setPosition((Gdx.graphics.getWidth()/3), Gdx.graphics.getHeight()-75);
		buttonMainMenu.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				hide();
				play.create();
				game.setScreen(game.mainMenu);
			}
		});
		
		
		buttonQuit = new TextButton("QUIT", textButtonStyle);
		buttonQuit.setSize(300,50);
		buttonQuit.setPosition((Gdx.graphics.getWidth()/3), Gdx.graphics.getHeight()-195);
		buttonQuit.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.completions = 0;
				SoundManager.delayFun.stop();
				SoundManager.spooky.stop();
				game.mainMenu.create();
				game.setScreen(game.mainMenu);
			}
		});
		
		stage = new Stage();
		stage.addActor(buttonMainMenu);
		stage.addActor(buttonQuit);
		
		
	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		System.out.println("Game Over");
		
		batch.begin();
		stage.act();
		stage.draw();
		gameOver.draw(batch, "GAME OVER", Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/8);
		
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}
}
