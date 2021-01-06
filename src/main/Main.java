package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import entities.Player2;
import entities.Terrain;
import loaders.Loader;
import loaders.OBJLoader;
import models.TexturedModel;
import renderers.MasterRenderer;
import socket.GameClient;
import renderers.DisplayManager;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

public class Main {
	
	public static Integer currSpeed;
	public static Integer currTurnSpeed;
	public static Integer isJump;
	
	public static GameClient client;
	
	public static Integer clientId = 1;
	public static String ipString = "127.0.0.1";
	public static Integer portNumber = 8888;
	
	public static HashMap<Integer, Player2> playerList = new HashMap<Integer, Player2>(); 

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		
		DisplayManager display = new DisplayManager();
		display.createDisplay();
		
		Loader loader = new Loader();
		
		// terrain
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("grassy"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassy"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("grassy"));
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		Terrain terrain = new Terrain(0,-1, loader, texturePack, blendMap, "heightMap");
		
		// lighting
		List <Light> lights = new ArrayList<Light>();
		Light sun = new Light(new Vector3f(400,1000,-400), new Vector3f(0.1f,0.1f,0.1f));
		lights.add(sun);
		float sunIntensity = 1/2;
		sun.setColour(new Vector3f(sunIntensity, sunIntensity, sunIntensity));
		
		// player
		TexturedModel playerTexture = new TexturedModel(OBJLoader.loadObjModel("player",  loader), new ModelTexture(loader.loadTexture("playerTexture")));
		Player player = new Player(playerTexture, new Vector3f(400,0,-400), 0, 180, 0, 1); // TODO pose
		Camera camera = new Camera(player);
		
		// test player 2
//		List<Entity> entities = new ArrayList<Entity>();
//		TexturedModel player2Texture = new TexturedModel(OBJLoader.loadObjModel("player",  loader), new ModelTexture(loader.loadTexture("playerTexture")));
//		Player2 player2 = new Player2(player2Texture, new Vector3f(300,0,-400), 0, 180, 0, 1);
		
		MasterRenderer renderer = new MasterRenderer(loader);
		
		float time = 0;
		float timeDelay = 100;
		while(!Display.isCloseRequested()) {
			camera.move();
			
			// added
			HashMap<Integer, Integer[]> player_pos_hmap = checkMove();
			Integer[] player_pos = player_pos_hmap.get(clientId);
//			System.out.println("receiving:");
//			System.out.println(player_pos[0]);
//			System.out.println(player_pos[1]);
//			System.out.println(player_pos[2]);
			player.setCurrentSpeed(player_pos[0]);
			player.setCurrentTurnSpeed(player_pos[1]);
			player.setJump(player_pos[2]);
			
			for (Integer key : player_pos_hmap.keySet()) {
				if (!key.equals(clientId)) {
					if (!playerList.containsKey(key)) {
						TexturedModel player2Texture = new TexturedModel(OBJLoader.loadObjModel("player",  loader), new ModelTexture(loader.loadTexture("playerTexture")));
						Player2 player2 = new Player2(player2Texture, new Vector3f(400,0,-400), 0, 180, 0, 1);
						playerList.put(key, player2);
					} else {
						Player2 player2 = playerList.get(key);
						Integer[] player2_pos = player_pos_hmap.get(key);
						player2.setCurrentSpeed(player2_pos[0]);
						player2.setCurrentTurnSpeed(player2_pos[1]);
						player2.setJump(player2_pos[2]);
					}
				}
            }
			
			player.move(terrain, display);
			for (Integer key : playerList.keySet()) {
				Player2 player2 = playerList.get(key);
				player2.move(terrain, display);
			}
			
			// TODO what
			time += display.getFrameTimeSeconds() * 1000;
			sunIntensity = 0.99f; //(float) ((float) (Math.sin(Math.toRadians(time/timeDelay))+1.0)/2.0);
			sun.setColour(new Vector3f(sunIntensity, sunIntensity, sunIntensity));
			
			renderer.processEntity(player);
			for (Integer key : playerList.keySet()) {
				Player2 player2 = playerList.get(key);
				renderer.processEntity(player2);
			}
			renderer.processTerrain(terrain);
			renderer.render(lights, camera);
			
			display.updateDisplay();
		}
		
		renderer.cleanUp();
		loader.cleanUp();
		
		display.closeDisplay();

	}
	
	public static HashMap<Integer, Integer[]> checkMove() throws ClassNotFoundException, IOException {
		if (Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			currSpeed = 1;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_S) || Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			currSpeed = -1;
		} else {
			currSpeed = 0;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_D) || Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			currTurnSpeed = -1;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_A) || Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			currTurnSpeed = 1;
		} else {
			currTurnSpeed = 0;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			isJump = 1;
		} else {
			isJump = 0;
		}	
		
		// server client stuff
		client = new GameClient();
		client.startConnection(ipString, portNumber);
		Integer[] controlsUpdate = {currSpeed, currTurnSpeed, isJump};
//		System.out.println("sending:");
//		System.out.println(controlsUpdate[0]);
//		System.out.println(controlsUpdate[1]);
//		System.out.println(controlsUpdate[2]);
		HashMap<Integer, Integer[]> ret = client.sendMessage(clientId, controlsUpdate); 
		client.stopConnection();
		return ret;
	}

}
