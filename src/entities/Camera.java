package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;

public class Camera {
	
	private float distanceFromPlayer = 0;
	private float angleAroundPlayer = 0;
	
	private Vector3f position = new Vector3f(100, 35, 50);
	private float pitch = 110; //how high or low the camera is aimed
	private float yaw; //how far left or right the camera is aimed
	private float roll; //how much the camera is tilted to one side
	
	private Player player;
	
	public Camera(Player player) {
		
		this.player = player;
		
	}
	
	public void move() {
		
		//calculateZoom();
		calculatePitch();
		calculateAngleAroundPlayer();
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		calculateCameraPosition(horizontalDistance, verticalDistance);
		this.yaw = 180 - (player.getRotY() + angleAroundPlayer);

		calculateAngleAroundPlayer();
		calculatePitch();
		if(Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_S)){
			player.increaseRotation(0, angleAroundPlayer, 0);
			angleAroundPlayer = 0;
		if(Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_S)){
			angleAroundPlayer /= 1.2f;
			if(angleAroundPlayer >= -0.5f && angleAroundPlayer <= 0.5f)
				angleAroundPlayer = 0;
		}
	}
		if(Keyboard.isKeyDown(Keyboard.KEY_J)) {
			Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
			player.rotate(0, -Mouse.getDX() * DisplayManager.getFrameTimeSeconds(), 0);
			Mouse.setGrabbed(true);
		}
		
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
	
	private void calculateCameraPosition(float horizDistance, float verticDistance) {
		
		float theta = player.getRotY() + angleAroundPlayer;
		float offsetX = (float) (horizDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horizDistance * Math.cos(Math.toRadians(theta)));
		position.x = player.getPosition().x - offsetX;
		position.z = player.getPosition().z - offsetZ;
		position.y = player.getPosition().y + verticDistance + 10;
		
	}
	
	private float calculateHorizontalDistance(){
		float hD = (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
		if(hD < 0)
			hD = 1;
		return hD;
	}
	
	private float calculateVerticalDistance(){
		float vD = (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
		if(vD < 0)
			vD = 1;
		return vD;
	}
	
	/*
	private void calculateZoom() {
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		distanceFromPlayer -= zoomLevel;
	}
	*/
	private void calculatePitch() {
		
			float pitchChange = Mouse.getDY() * 0.1f;
			pitch -= pitchChange;
			if(pitch > 60)
				pitch = 60;
		}
		
	
	
	private void calculateAngleAroundPlayer() {
		
			float angleChange = Mouse.getDX() * 0.3f;
			angleAroundPlayer -= angleChange;
			
			
		
		
	}
}