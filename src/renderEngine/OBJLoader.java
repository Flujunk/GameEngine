package renderEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import models.RawModel;

public class OBJLoader {

	public static RawModel loadObjModel(String fileName, Loader loader) {	//Loads OBJ's
		
		FileReader fr = null;
		try {
			fr = new FileReader(new File("res/" + fileName + ".obj")); //finds file in the res folder
		} catch (FileNotFoundException e) {
			System.err.println("Couldn't load file!");
			e.printStackTrace();
		}
		BufferedReader reader = new BufferedReader(fr); //reads from file
		String line;
		List<Vector3f> vertices = new ArrayList<Vector3f>(); //this and the next couple lines are read from the file
		List<Vector2f> textures = new ArrayList<Vector2f>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		List<Integer> indices = new ArrayList<Integer>();
		float[] verticesArray = null; //these are float arrays because the loader needs them to load into a VAO
		float[] normalsArray = null;
		float[] textureArray = null;
		int[] indicesArray = null;
		try {
			while(true) { //reads through file until broken out.
				line = reader.readLine();
				String[] currentLine = line.split(" "); //breaks the lines at a space
				if(line.startsWith("v ")) { //vertex postion
					Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3])); //creates a vertex from data here
					vertices.add(vertex); //adds to list of vertices
				}else if(line.startsWith("vt ")) {
					Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]));//^
					textures.add(texture); //adds to list of textures
				}else if(line.startsWith("vn ")) {
					Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));//^
					normals.add(normal); // adds to list of normals
				}else if(line.startsWith("f ")) { //if we get here, we've read all the other data
					textureArray = new float[vertices.size() * 2]; //number of vertices multiplies by two because each texture has 2 floats.
					normalsArray = new float[vertices.size() * 3];
					break;
				}
			}
			while(line != null) {
				if(!line.startsWith("f ")) {
					line = reader.readLine();
					continue;
				}
				String[] currentLine = line.split(" "); //splits at spaces. Gives a string array.
				String[] vertex1 = currentLine[1].split("/");
				String[] vertex2 = currentLine[2].split("/");
				String[] vertex3 = currentLine[3].split("/");// ^ gives a string array of numbers.
				
				processVertex(vertex1, indices, textures, normals, textureArray, normalsArray);
				processVertex(vertex2, indices, textures, normals, textureArray, normalsArray);
				processVertex(vertex3, indices, textures, normals, textureArray, normalsArray); //processes current triangle and puts normal and vertex positions into the right place
				line = reader.readLine();
			}
			reader.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		verticesArray = new float[vertices.size() * 3]; //creates a new float array. Smae size as vertex list multiplied by 3 since they're floats instead of vectors
		indicesArray = new int[indices.size()]; //converts indices list into an int array
		
		int vertexPointer = 0;
		for(Vector3f vertex:vertices){ //loops through list of vertices 
			verticesArray[vertexPointer++] = vertex.x;
			verticesArray[vertexPointer++] = vertex.y;
			verticesArray[vertexPointer++] = vertex.z; //puts into the float array
		}
		
		for(int i = 0; i < indices.size(); i++) { //copies indices data from indices list, and puts it in the indices array
			indicesArray[i] = indices.get(i);
		}
		return loader.loadToVAO(verticesArray, textureArray, normalsArray, indicesArray);
	}
	
	private static void processVertex(String[] vertexData, List<Integer> indices, List<Vector2f> textures, List<Vector3f> normals, float[] textureArray, float[] normalsArray) {
		
		int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1; //tells index of the vertex position in the vertex positions list
		indices.add(currentVertexPointer);
		Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1]) - 1); //gets texture that corresponds to this vertex
		textureArray[currentVertexPointer * 2] = currentTex.x;
		textureArray[currentVertexPointer * 2 + 1] = 1 - currentTex.y; //sets x & y position of texture in the texture array
		Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2]) - 1); //gets normal vector associated this vertex
		normalsArray[currentVertexPointer * 3] = currentNorm.x;
		normalsArray[currentVertexPointer * 3 + 1] = currentNorm.y;
		normalsArray[currentVertexPointer * 3 + 2] = currentNorm.z; //puts normal into correct position into the array of normals
	}
	
}
