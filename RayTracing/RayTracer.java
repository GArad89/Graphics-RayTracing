import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader ;
import java.util.ArrayList;
import java.util.Arrays;

public class RayTracer{
    
    public static void main(String[] args) throws IOException{
        String scene_fn=args[0], img_fn=args[1];
        int h=Integer.parseInt(args[2]), w=Integer.parseInt(args[3]);
        BufferedReader reader = new BufferedReader(new FileReader(new File(scene_fn)));
        ArrayList<Material> mats = new ArrayList<Material>();
        String line;
        Scene scene=null;
        Camera cam=null;
        Surface surf;
        Light light;
        Material mat;
        while((line = reader.readLine()) != null){
            if(line.length() == 0 || line.charAt(0) == '#')
                continue;
            String params[]=line.split("\\s+"), cmd=params[0];
            params = Arrays.copyOfRange(params, 1, params.length);
                for(int i=0;i<params.length;i++){
                    System.out.print(params[i]);
                    System.out.print(" ");
                }
                System.out.println();
            if(cmd.equals("cam")){
                cam = new Camera(Float.parseFloat(params[0]), Float.parseFloat(params[1]), Float.parseFloat(params[2]),
                             Float.parseFloat(params[3]), Float.parseFloat(params[4]), Float.parseFloat(params[5]),
                             Float.parseFloat(params[6]), Float.parseFloat(params[7]), Float.parseFloat(params[8]),
                             Float.parseFloat(params[9]), Float.parseFloat(params[10]));
                if(scene != null)
                    scene.setCam(cam);
            }
            else if(cmd.equals("set")){
                scene = new Scene(Float.parseFloat(params[0]), Float.parseFloat(params[1]), Float.parseFloat(params[2]),
                                  Integer.parseInt(params[3]), Integer.parseInt(params[4]), Integer.parseInt(params[5]));
                if(cam != null)
                    scene.setCam(cam);
            }
            else if(cmd.equals("mtl")){
                mat = new Material(Float.parseFloat(params[0]), Float.parseFloat(params[1]), Float.parseFloat(params[2]), 
                                   Float.parseFloat(params[3]), Float.parseFloat(params[4]), Float.parseFloat(params[5]),
                                   Float.parseFloat(params[6]), Float.parseFloat(params[7]), Float.parseFloat(params[8]),
                                   Float.parseFloat(params[9]), Float.parseFloat(params[10]));
                mats.add(mat);
            }
            else if(cmd.equals("sph")){
                mat = mats.get(Integer.parseInt(params[4])-1);
                surf = new Sphere(Float.parseFloat(params[0]), Float.parseFloat(params[1]), Float.parseFloat(params[2]),
                                 Float.parseFloat(params[3]), mat);
                scene.addSurface(surf);
            }
            else if(cmd.equals("pln")){
                mat = mats.get(Integer.parseInt(params[4])-1);
                surf = new Plane(Float.parseFloat(params[0]), Float.parseFloat(params[1]), Float.parseFloat(params[2]),
                                 Float.parseFloat(params[3]), mat);
                scene.addSurface(surf);
            }
            else if(cmd.equals("trg")){
                mat = mats.get(Integer.parseInt(params[9])-1);
                surf = new Triangle(Float.parseFloat(params[0]), Float.parseFloat(params[1]), Float.parseFloat(params[2]),
                                   Float.parseFloat(params[3]), Float.parseFloat(params[4]), Float.parseFloat(params[5]),
                                   Float.parseFloat(params[6]), Float.parseFloat(params[7]), Float.parseFloat(params[8]), mat);
                scene.addSurface(surf);
            }
            else if(cmd.equals("lgt")){
                light = new Light(Float.parseFloat(params[0]), Float.parseFloat(params[1]), Float.parseFloat(params[2]),
                                  Float.parseFloat(params[3]), Float.parseFloat(params[4]), Float.parseFloat(params[5]),
                                  Float.parseFloat(params[6]), Float.parseFloat(params[7]), Float.parseFloat(params[8]));
                scene.addLight(light);
            }
        }
    }
}