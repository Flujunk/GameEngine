package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;
import renderEngine.DisplayManager;
import terrains.Terrain;

public class Player extends Entity{
	
	private static float RUN_SPEED = 20;
	private static final float GRAVITY = -50;
	private static final float JUMP_POWER = 30;
	public static final String Entity = null;
	
	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	private float upwardsSpeed = 0;
	private float currentSidewaysSpeed = 0;
	
	private boolean isInAir = false;
	
	public int x = 5;
	public int y = 5;
	public int width = 50;
	public int height = 50;
	
	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}
	public void Item(TexturedModel model) {
		
	}
	
	public void move(Terrain terrain) {
		
		checkInputs();
		super.increaseRotation(0,  currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
		float distanceSideways = currentSidewaysSpeed * DisplayManager.getFrameTimeSeconds();
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dxSideways = (float) (distanceSideways * Math.sin(Math.toRadians(super.getRotY() + 90)));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		float dzSideways = (float) (distanceSideways * Math.cos(Math.toRadians(super.getRotY() + 90)));
		super.increasePosition(dx,  0,  dz, dxSideways, dzSideways);
		upwardsSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
		super.increasePosition(0, upwardsSpeed * DisplayManager.getFrameTimeSeconds(), 0, dxSideways, dzSideways);
		float terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
		if(super.getPosition().y < terrainHeight) {
			upwardsSpeed = 0;
			isInAir = false;
			super.getPosition().y = terrainHeight;
		}
		//invisible walls
		if(super.getPosition().x < 1) {
			super.getPosition().x = super.getPosition().x + 1;
			System.out.println("Collision Detected");
		}
		if(super.getPosition().x > 799) {
			super.getPosition().x = super.getPosition().x - 1;
			System.out.println("Collision Detected");
		}
		if(super.getPosition().z > -1){
			super.getPosition().z = super.getPosition().z - 1;
			System.out.println("Collision Detected");
		}
		if(super.getPosition().z < -799){
			super.getPosition().z = super.getPosition().z + 1;
			System.out.println("Collision Detected");
		}
		
		
		
	}
	
	private void jump() {
		if(!isInAir) {
			this.upwardsSpeed = JUMP_POWER;
			isInAir = true;
		}
		
	}
	
	private void checkInputs() {
		
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			this.currentSpeed = RUN_SPEED;
			if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
				this.currentSpeed = RUN_SPEED * 4;
				if(isInAir) {
					this.currentSpeed = RUN_SPEED * 4 - 25;
				}
			else if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
				this.currentSidewaysSpeed = RUN_SPEED;
			}
			else if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
				this.currentSidewaysSpeed = -RUN_SPEED;
			}
			else {
				this.currentSidewaysSpeed = 0;
			}
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			this.currentSpeed = -RUN_SPEED;
			if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
				this.currentSidewaysSpeed = RUN_SPEED;
			}
			else if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
				this.currentSidewaysSpeed = -RUN_SPEED;
			}
			else {
				this.currentSidewaysSpeed = 0;
			}
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			this.currentSidewaysSpeed = RUN_SPEED;
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			this.currentSidewaysSpeed = -RUN_SPEED;
		}


		else {
			this.currentSpeed = 0;
			this.currentSidewaysSpeed = 0;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			jump();
		}

	}

	public void rotate(int i, float f, int j) {
		
	}
	
	

}