package RayTrace;
 
import java.awt.Transparency;
import java.awt.color.*;
import java.awt.image.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

 
import javax.imageio.ImageIO;
 
/**
 *  Main class for ray tracing exercise.
 */
public class RayTracer {
	
	public Scene scene;
    public int imageWidth;
    public int imageHeight;
 
    /**
     * Runs the ray tracer. Takes scene file, output image file and image size as input.
     */
    public static void main(String[] args) {
 
        try {
 
            RayTracer tracer = new RayTracer();
 
                        // Default values:
            tracer.imageWidth = 500;
            tracer.imageHeight = 500;
 
            if (args.length < 2)
                throw new RayTracerException("Not enough arguments provided. Please specify an input scene file and an output image file for rendering.");
 
            String sceneFileName = args[0];
            String outputFileName = args[1];
 
            if (args.length > 3)
            {
                tracer.imageWidth = Integer.parseInt(args[2]);
                tracer.imageHeight = Integer.parseInt(args[3]);
            }
 
 
            // Parse scene file:
            tracer.parseScene(sceneFileName);
 
            // Render scene:
            tracer.renderScene(outputFileName);
 
//      } catch (IOException e) {
//          System.out.println(e.getMessage());
     //   } catch (RayTracerException e) {
     //       System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
 
 
    }
 
    /**
     * Parses the scene file and creates the scene. Change this function so it generates the required objects.
     */
    public void parseScene(String sceneFileName) throws IOException, RayTracerException
    {
        String scene_fn=sceneFileName;
        //int h=Integer.parseInt(args[2]), w=Integer.parseInt(args[3]);
        BufferedReader reader = new BufferedReader(new FileReader(new File(scene_fn)));
        ArrayList<Material> mats = new ArrayList<Material>();
        boolean missing_mat = false;
        boolean missing_paramter = false;
        int mat_index =0;
        String line;
        Scene scene=null;
        Camera cam=null;
        Surface surf;
        Light light;
        Material mat;
        //FileReader fr = new FileReader(sceneFileName);
        //BufferedReader r = new BufferedReader(fr);
        //String line = null;
        int lineNum = 0;
        System.out.println("Started parsing scene file " + sceneFileName);
 
 
 
        while ((line = reader.readLine()) != null)
        {
            line = line.trim();
            ++lineNum;
 
            if (line.isEmpty() || (line.charAt(0) == '#'))
            {  // This line in the scene file is a comment
                continue;
            }
            else
            {
                String code = line.substring(0, 3).toLowerCase();
                // Split according to white space characters:
                String[] params = line.substring(3).trim().toLowerCase().split("\\s+");
                
                if (code.equals("cam"))
                {
                	if(params.length >= 11) {
	                    cam = new Camera(Double.parseDouble(params[0]), Double.parseDouble(params[1]), Double.parseDouble(params[2]),
	                            Double.parseDouble(params[3]), Double.parseDouble(params[4]), Double.parseDouble(params[5]),
	                            Double.parseDouble(params[6]), Double.parseDouble(params[7]), Double.parseDouble(params[8]),
	                            Double.parseDouble(params[9]), Double.parseDouble(params[10]));
	                    if(scene != null)
	                        scene.setCam(cam);
	 
	                    System.out.println(String.format("Parsed camera parameters (line %d)", lineNum));
                	}
                	else {
                		missing_paramter = true;
                		System.out.println(String.format("ERROR: Parsed camera is missing parameters (line %d)", lineNum));
                	}
                }
                else if (code.equals("set"))
                {
                	if(params.length >= 6) {
	                	 scene = new Scene(Double.parseDouble(params[0]), Double.parseDouble(params[1]), Double.parseDouble(params[2]),
	                             Integer.parseInt(params[3]), Integer.parseInt(params[4]), Integer.parseInt(params[5]));
	                	 if(cam != null)
	                		 scene.setCam(cam);
	 
	                    System.out.println(String.format("Parsed general settings (line %d)", lineNum));
                	}
                	else {
                		missing_paramter = true;
                		System.out.println(String.format("ERROR: Parsed general settings is missing parameters (line %d)", lineNum));
                	}
                }
                else if (code.equals("mtl"))
                {
                	if(params.length >= 11) {
	                	mat = new Material(Double.parseDouble(params[0]), Double.parseDouble(params[1]), Double.parseDouble(params[2]), 
	                            Double.parseDouble(params[3]), Double.parseDouble(params[4]), Double.parseDouble(params[5]),
	                            Double.parseDouble(params[6]), Double.parseDouble(params[7]), Double.parseDouble(params[8]),
	                            Double.parseDouble(params[9]), Double.parseDouble(params[10]));
	                	mats.add(mat);
	 
	                    System.out.println(String.format("Parsed material (line %d)", lineNum));
                	}
                	else {
                		missing_paramter = true;
                		System.out.println(String.format("ERROR: Parsed material is missing parameters (line %d)", lineNum));
                	}
                }
                else if (code.equals("sph"))
                {
                	if(params.length >= 5) {
	                	mat_index=Integer.parseInt(params[4])-1;
	                	if(mats.size() > mat_index) {
		                	mat = mats.get(mat_index);
		                    surf = new Sphere(Double.parseDouble(params[0]), Double.parseDouble(params[1]), Double.parseDouble(params[2]),
		                                     Double.parseDouble(params[3]), mat);
		                    scene.addSurface(surf);
		 
		                                        // Example (you can implement this in many different ways!):
		                    // Sphere sphere = new Sphere();
		                                        // sphere.setCenter(params[0], params[1], params[2]);
		                                        // sphere.setRadius(params[3]);
		                                        // sphere.setMaterial(params[4]);
		 
		                    System.out.println(String.format("Parsed sphere (line %d)", lineNum));
	                	}
	                	else {
	                		missing_mat = true;
	                		System.out.println(String.format("ERROR: material index for sphere doesn't exist (line %d)", lineNum));
	                	}
                	}
                  	else {
                		missing_paramter = true;
                		System.out.println(String.format("ERROR: Parsed sphere is missing parameters (line %d)", lineNum));
                	}
                }
                else if (code.equals("pln"))
                {
                	if(params.length >= 5) {
                		mat_index=Integer.parseInt(params[4])-1;
                		if(mats.size() > mat_index) {
		                	mat = mats.get(Integer.parseInt(params[4])-1);
		                    surf = new Plane(Double.parseDouble(params[0]), Double.parseDouble(params[1]), Double.parseDouble(params[2]),
		                                     Double.parseDouble(params[3]), mat);
		                    scene.addSurface(surf);
		 
		                    System.out.println(String.format("Parsed plane (line %d)", lineNum));
                		}
	                	else {
	                		missing_mat = true;
	                		System.out.println(String.format("ERROR: material index for plane doesn't exist (line %d)", lineNum));
	                	}
                	}
                	else {
                		missing_paramter = true;
                		System.out.println(String.format("ERROR: Parsed plane is missing parameters (line %d)", lineNum));
                	}
                }
                else if (code.equals("trg"))
                {
                	if(params.length >= 10) {
                		mat_index=Integer.parseInt(params[9])-1;
                		if(mats.size() > mat_index) {
		                	 mat = mats.get(Integer.parseInt(params[9])-1);
		                     surf = new Triangle(Double.parseDouble(params[0]), Double.parseDouble(params[1]), Double.parseDouble(params[2]),
		                                        Double.parseDouble(params[3]), Double.parseDouble(params[4]), Double.parseDouble(params[5]),
		                                        Double.parseDouble(params[6]), Double.parseDouble(params[7]), Double.parseDouble(params[8]), mat);
		                     scene.addSurface(surf);
		                     
		                    System.out.println(String.format("Parsed triangle (line %d)", lineNum));
                		}
                		else {
	                		missing_mat = true;
	                		System.out.println(String.format("ERROR: material index for triangle doesn't exist (line %d)", lineNum));
                		}
                	}
                	else {
                		missing_paramter = true;
                		System.out.println(String.format("ERROR: Parsed triangle is missing parameters (line %d)", lineNum));
                	}
                }
                else if (code.equals("lgt"))
                {
                	if(params.length >= 9) {
	                    light = new Light(Double.parseDouble(params[0]), Double.parseDouble(params[1]), Double.parseDouble(params[2]),
	                            Double.parseDouble(params[3]), Double.parseDouble(params[4]), Double.parseDouble(params[5]),
	                            Double.parseDouble(params[6]), Double.parseDouble(params[7]), Double.parseDouble(params[8]));
	                    scene.addLight(light);
	 
	                    System.out.println(String.format("Parsed light (line %d)", lineNum));
                	}
                  	else {
                		missing_paramter = true;
                		System.out.println(String.format("ERROR: Parsed light is missing parameters (line %d)", lineNum));
                	}
                }
                else
                {
                    System.out.println(String.format("ERROR: Did not recognize object: %s (line %d)", code, lineNum));
                }
            }
        }
        reader.close();
        if((scene == null)||(cam == null)) {
        	System.out.println("Critical Error occured while parsing scene file " + sceneFileName);
        	System.out.println("general setting or camera are missing.");
        	return;
        }
        if((missing_paramter == true)||(missing_mat == true)) {
        	System.out.println("Error occured while parsing scene file " + sceneFileName);
        	System.out.println("At least one line with missing paratmers or invalid material index.");
        }
        System.out.println("Finished parsing scene file " + sceneFileName);
        this.scene = scene;
 
 
    }
 
    /**
     * Renders the loaded scene and saves it to the specified file location.
     */
    public void renderScene(String outputFileName)
    {
        long startTime = System.currentTimeMillis();
 
        // Create a byte array to hold the pixel data:
        byte[] rgbData = new byte[this.imageWidth * this.imageHeight * 3];
        scene.Render(rgbData, this.imageWidth, this.imageHeight);
 

 
        long endTime = System.currentTimeMillis();
        Long renderTime = endTime - startTime;
 
                // The time is measured for your own conveniece, rendering speed will not affect your score
                // unless it is exceptionally slow (more than a couple of minutes)
        System.out.println("Finished rendering scene in " + renderTime.toString() + " milliseconds.");
 
                // This is already implemented, and should work without adding any code.
        saveImage(this.imageWidth, rgbData, outputFileName);
 
        System.out.println("Saved file " + outputFileName);
 
    }
 
 
 
 
    //////////////////////// FUNCTIONS TO SAVE IMAGES IN PNG FORMAT //////////////////////////////////////////
 
    /*
     * Saves RGB data as an image in png format to the specified location.
     */
    public static void saveImage(int width, byte[] rgbData, String fileName)
    {
        try {
 
            BufferedImage image = bytes2RGB(width, rgbData);
            ImageIO.write(image, "png", new File(fileName));
 
        } catch (IOException e) {
            System.out.println("ERROR SAVING FILE: " + e.getMessage());
        }
 
    }
 
    /*
     * Producing a BufferedImage that can be saved as png from a byte array of RGB values.
     */
    public static BufferedImage bytes2RGB(int width, byte[] buffer) {
        int height = buffer.length / width / 3;
        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
        ColorModel cm = new ComponentColorModel(cs, false, false,
                Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
        SampleModel sm = cm.createCompatibleSampleModel(width, height);
        DataBufferByte db = new DataBufferByte(buffer, width * height);
        WritableRaster raster = Raster.createWritableRaster(sm, db, null);
        BufferedImage result = new BufferedImage(cm, raster, false, null);
 
        return result;
    }
 
    @SuppressWarnings("serial")
	public static class RayTracerException extends Exception {
        public RayTracerException(String msg) {  super(msg); }
    }
    

 
}