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
	
	private boolean isInAir = false;
	
	public static int x = 5;
	public static int y = 5;
	public static int width = 50;
	public static int height = 50;
		
	
	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}
	public void Item(TexturedModel model) {
		
	}
	
	public void move(Terrain terrain) {
		
		checkInputs();
		super.increaseRotation(0,  currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		super.increasePosition(dx,  0,  dz);
		upwardsSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
		super.increasePosition(0, upwardsSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		float terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
		if(super.getPosition().y < terrainHeight) {
			upwardsSpeed = 0;
			isInAir = false;
			super.getPosition().y = terrainHeight;
		}
		//invisible walls
		if(super.getPosition().x < 1) {
			super.getPosition().x = super.getPosition().x + 1;
		}
		if(super.getPosition().x > 799) {
			super.getPosition().x = super.getPosition().x - 1;
		}
		if(super.getPosition().z > -1){
			super.getPosition().z = super.getPosition().z - 1;
		}
		if(super.getPosition().z < -799){
			super.getPosition().z = super.getPosition().z + 1;
		}
		if(Player.x < super.getPosition().x + super.getPosition().z && 
				Player.x + Player.x > super.getPosition().x &&
				Player.y < super.getPosition().y + super.getPosition().y && 
				Player.y + super.getPosition().y > super.getPosition().y)
			{
			    System.out.println("Collision Detected");
			}

		
	}
	
	public class Vector2Float
	{
	   public float x, y;
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
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			this.currentSpeed = -RUN_SPEED;
		}
		else {
			this.currentSpeed = 0;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			jump();
		}

	}

	public void rotate(int i, float f, int j) {
		
	}
	
	

}