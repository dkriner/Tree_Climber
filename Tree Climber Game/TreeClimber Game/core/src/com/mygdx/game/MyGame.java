package com.mygdx.game;

import com.badlogic.gdx.Game;

public class MyGame extends Game{

	public Level playLevel;
	public TreeClimber mainMenu;
	public ResultScreen resultsScreen;
	public HelpScreen helpScreen;
	public Death death;
	public FailureScreen failureScreen;
	public int completions;
	public int lives;
	public Player player;
	
	@Override
	public void create() {
		player = new Player("Images/TestSqaure.jpg");
		playLevel = new Level(this);
		mainMenu = new TreeClimber(this);
		resultsScreen = new ResultScreen(this);
		helpScreen = new HelpScreen(this);
		death = new Death(this);
		failureScreen = new FailureScreen(this);
		completions = 3;
		
		SoundManager.create();
		//death.create();
		mainMenu.create();
		setScreen(mainMenu);
		//setScreen(death);
		// TODO Auto-generated method stub
		
	}

}
