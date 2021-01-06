package main;

import java.util.ArrayList;
import java.util.List;

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
import renderers.DisplayManager;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

public class Main {

	public static void main(String[] args) {
		
DisplayManager.createDisplay();
		
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
		List<Entity> entities = new ArrayList<Entity>();
		TexturedModel player2Texture = new TexturedModel(OBJLoader.loadObjModel("player",  loader), new ModelTexture(loader.loadTexture("playerTexture")));
		Player2 player2 = new Player2(player2Texture, new Vector3f(300,0,-400), 0, 180, 0, 1);
		
		MasterRenderer renderer = new MasterRenderer(loader);
		
		float time = 0;
		float timeDelay = 100;
		while(!Display.isCloseRequested()) {
			camera.move();
			player.move(terrain);
//			player2.move(terrain);
			
			// TODO what
			time += DisplayManager.getFrameTimeSeconds() * 1000;
			sunIntensity = (float) ((float) (Math.sin(Math.toRadians(time/timeDelay))+1.0)/2.0);
			sun.setColour(new Vector3f(sunIntensity, sunIntensity, sunIntensity));
			
			renderer.processEntity(player);
			renderer.processEntity(player2);
			renderer.processTerrain(terrain);
			renderer.render(lights, camera);
			
			DisplayManager.updateDisplay();
		}
		
		renderer.cleanUp();
		loader.cleanUp();
		
		DisplayManager.closeDisplay();

	}

}
