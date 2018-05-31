package engineTester;
 
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
 
import models.RawModel;
import models.TexturedModel;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import gui.GuiRenderer;
import gui.GuiTexture;
 
public class MainGameLoop {
 
    public static void main(String[] args) {
 
        DisplayManager.createDisplay();
        Loader loader = new Loader();
        
        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
        
        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
         
        RawModel model = OBJLoader.loadObjModel("tree", loader);
        
        //TexturedModel staticModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("tree")));
        TexturedModel grass = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader), new ModelTexture(loader.loadTexture("grassTexture")));
        TexturedModel flower = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader), new ModelTexture(loader.loadTexture("flower")));
        //TexturedModel rock = new TexturedModel(OBJLoader.loadObjModel("rock", loader), new ModelTexture(loader.loadTexture("rock")));

        ModelTexture fernTextureAtlas = new ModelTexture(loader.loadTexture("fern"));
        fernTextureAtlas.setNumberOfRows(2);
        TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern",  loader), fernTextureAtlas);
        
        TexturedModel bobble = new TexturedModel(OBJLoader.loadObjModel("lowPolyTree", loader), new ModelTexture(loader.loadTexture("lowPolyTree")));
        //TexturedModel box = new TexturedModel(OBJLoader.loadObjModel("box", loader), new ModelTexture(loader.loadTexture("box")));
        
        TexturedModel lamp = new TexturedModel(OBJLoader.loadObjModel("lamp", loader), new ModelTexture(loader.loadTexture("lamp")));
        
        lamp.getTexture().setUseFakeLighting(true);
        grass.getTexture().setHasTransparency(true);
        grass.getTexture().setUseFakeLighting(true);
        flower.getTexture().setHasTransparency(true);
        flower.getTexture().setUseFakeLighting(true);
        fern.getTexture().setHasTransparency(true);
        

    	Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "heightMap");
    	//Terrain terrain2 = new Terrain(1, 0, loader, texturePack, blendMap, "myHeightMap");
  
    	
        List<Entity> entities = new ArrayList<Entity>();
        List<Light> lights = new ArrayList<Light>();
        Random random = new Random(676452);
        for(int i = 0; i < 400; i++){
            if(i % 20 == 0) {
            	float x = random.nextFloat() * 800 - 400;
            	float z = random.nextFloat() * -600;
            	float y = terrain.getHeightOfTerrain(x, z);
            }
            if(i % 5 == 0) {
            	float x = random.nextFloat() * 800 - 400;
            	float z = random.nextFloat() * -600;
            	float y = terrain.getHeightOfTerrain(x,  z);
            	x = random.nextFloat() * 800 - 400;
            	z = random.nextFloat() * -600;
            	y = terrain.getHeightOfTerrain(x,  z);


            }
			
        }
    	float x = 150;
    	float z = -50;
    	float y = terrain.getHeightOfTerrain(x,  z);
    	
    	entities.add(new Entity(bobble, new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0, random.nextFloat() * 0.1f + 1.0f));

    			
        lights.add(new Light(new Vector3f(0, 1000, -7000), new Vector3f(0.4f, 0.4f, 0.4f)));
       
        entities.add(new Entity(lamp, random.nextInt(4), new Vector3f(x, terrain.getHeightOfTerrain(x,  z), z), 0, random.nextFloat() * 360, 0, 0.9f));
        lights.add(new Light(new Vector3f(x, y + 20, z), new Vector3f(2, 0, 0), new Vector3f(1, 0.01f, 0.002f)));
        
        entities.add(new Entity(lamp, new Vector3f(x + 20, terrain.getHeightOfTerrain(x,  z), z), 0, 0, 0, 1));
        lights.add(new Light(new Vector3f(x + 20, y + 20, z), new Vector3f(0, 2, 0), new Vector3f(1, 0.01f, 0.002f)));
        
        entities.add(new Entity(lamp, new Vector3f(x + 40, terrain.getHeightOfTerrain(x,  z), z), 0, 0, 0, 1));
        lights.add(new Light(new Vector3f(x + 40, y + 20, z), new Vector3f(2, 2, 0), new Vector3f(1, 0.01f, 0.002f)));


        MasterRenderer renderer = new MasterRenderer(loader);
        
        RawModel bunnyModel = OBJLoader.loadObjModel("person", loader);
        TexturedModel stanfordBunny = new TexturedModel(bunnyModel, new ModelTexture(loader.loadTexture("playerTexture")));
        
        Player player = new Player(stanfordBunny, new Vector3f(100, 0, -50), 0, 0, 0, 1);
        Camera camera = new Camera(player);  
        


		
        List<GuiTexture> guis = new ArrayList<GuiTexture>();
        //GuiTexture gui = new GuiTexture(loader.loadTexture("socuwan"), new Vector2f(-0.5f, 0.5f), new Vector2f(0.25f, 0.25f));
        //GuiTexture gui2 = new GuiTexture(loader.loadTexture("thinmatrix"), new Vector2f(0.30f, 0.58f), new Vector2f(0.4f, 0.4f));

        //guis.add(gui);
        //guis.add(gui2);

        GuiRenderer guiRenderer = new GuiRenderer(loader);
        
        while(!Display.isCloseRequested()){
            player.move(terrain);
            camera.move();
            renderer.processEntity(player);
            renderer.processTerrain(terrain);
           // renderer.processTerrain(terrain2);
            for(Entity entity : entities){
                renderer.processEntity(entity);
            }
            renderer.render(lights, camera);
            guiRenderer.render(guis);
            DisplayManager.updateDisplay();
        }
 
        guiRenderer.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
 
    }
 
}