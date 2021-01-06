package entities;

import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;
import renderers.DisplayManager;

public class Player2 extends Entity {
	
	private static final float RUN_SPEED = 250;   // units per second
	private static final float TURN_SPEED = 200; // degrees per second
	private static final float GRAVITY = -150;
	private static final float JUMP_POWER = 130;
	
	
	private float currentSpeed = RUN_SPEED; //0
	private float currentTurnSpeed = 0;
	private float upwardsSpeed = 0;
	private boolean isAirborn = false;
	
	public Player2(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}
	
	public void move(Terrain terrain){
		super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		super.increasePosition(dx, 0, dz);
		upwardsSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
		super.increasePosition(0, upwardsSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		float terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
		if (super.getPosition().y  < terrainHeight){
			upwardsSpeed = 0;
			super.getPosition().y = terrainHeight;
			isAirborn = false;
		}
		
		if (getPosition().x  < 0){
			upwardsSpeed = 0;
			getPosition().x = 0;
		}
		if (getPosition().x  > 800){
			upwardsSpeed = 0;
			getPosition().x = 800;
		}

		if (getPosition().z  > 0){
			upwardsSpeed = 0;
			getPosition().z = 0;
		}
		if (getPosition().z  < -800){
			upwardsSpeed = 0;
			getPosition().z = -800;
		}
	}

}
