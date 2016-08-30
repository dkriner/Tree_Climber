package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;



public class SoundManager {
	public static Sound staminaPickup;
	public static Sound healthPickup;
	public static Sound boostActivation;
	public static Sound debrisImpact;
	public static Sound freezeActivation;
	public static Music lava;
	public static Sound pointPickup;
	public static Sound victory;
	public static Sound goalReached;
	
	public static Music mainMenu;
	public static Music delayFun;
	public static Music spooky;
	
	public static void create() {
		//MUSIC
		mainMenu = Gdx.audio.newMusic(Gdx.files.internal("audio/mainMenu.mp3"));
		spooky = Gdx.audio.newMusic(Gdx.files.internal("audio/spooky.mp3"));
		delayFun = Gdx.audio.newMusic(Gdx.files.internal("audio/DelayFun.mp3"));
		
		//SOUND EFFECTS
		staminaPickup = Gdx.audio.newSound(Gdx.files.internal("audio/sound effect mp3's/stamina pickup.mp3"));
		healthPickup = Gdx.audio.newSound(Gdx.files.internal("audio/sound effect mp3's/health regen.mp3"));
		boostActivation = Gdx.audio.newSound(Gdx.files.internal("audio/sound effect mp3's/boost.mp3"));
		debrisImpact = Gdx.audio.newSound(Gdx.files.internal("audio/sound effect mp3's/debris impact.mp3"));
		freezeActivation = Gdx.audio.newSound(Gdx.files.internal("audio/sound effect mp3's/freeze activation.mp3"));
		lava = Gdx.audio.newMusic(Gdx.files.internal("audio/sound effect mp3's/lava.mp3"));
		pointPickup = Gdx.audio.newSound(Gdx.files.internal("audio/sound effect mp3's/point pickup.mp3"));
		victory  = Gdx.audio.newSound(Gdx.files.internal("audio/sound effect mp3's/victory.mp3"));
		goalReached = Gdx.audio.newSound(Gdx.files.internal("audio/sound effect mp3's/pointGoal reached.mp3"));
		
		
	}
	
	public static void dispose() {
		mainMenu.dispose();
		delayFun.dispose();
		spooky.dispose();
		staminaPickup.dispose();
		healthPickup.dispose();
		boostActivation.dispose();
		debrisImpact.dispose();
		freezeActivation.dispose();
		lava.dispose();
		pointPickup.dispose();
		victory.dispose();
		goalReached.dispose();
	}

}
