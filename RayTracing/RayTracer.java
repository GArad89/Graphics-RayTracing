
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader ;

public class RayTracer{
    
    public static void main(String[] args) throws IOException{
        String scene_fn=args[1], img_fn=args[2];
        int h=Integer.parseInt(args[3]), w=Integer.parseInt(args[4]);
        BufferedReader reader = new BufferedReader(new FileReader(new File(scene_fn)));
        String line;
        Scene scene=null;
        Camera cam=null;
        while((line = reader.readLine()) != null){
            String cmd = line.substring(0, 3), params[]=line.substring(5).split(" ");
            if(cmd.equals("cam")){
                cam = new Camera(Float.parseFloat(params[0]), Float.parseFloat(params[1]), Float.parseFloat(params[2]),
                             Float.parseFloat(params[3]), Float.parseFloat(params[4]), Float.parseFloat(params[5]),
                             Float.parseFloat(params[6]), Float.parseFloat(params[7]), Float.parseFloat(params[8]),
                             Float.parseFloat(params[9]), Float.parseFloat(params[10]));
                if(scene != null)
                    scene.setCam(cam);
            }
            else if(cmd.equals("set")){
                scene = new Scene(Integer.parseInt(params[0]), Integer.parseInt(params[1]), Integer.parseInt(params[2]),
                                  Integer.parseInt(params[3]), Integer.parseInt(params[4]), Integer.parseInt(params[5]));
                if(cam != null)
                    scene.setCam(cam);
            }
            else if(cmd.equals("mtl")){
                //mtl = new Material()
            }
            else if(cmd.equals("sph")){
                //sph = new Sphere()
            }
            else if(cmd.equals("pln")){
                //pln = new Plane()
            }
            else if(cmd.equals("trg")){
                //trg = new Triangle()
            }
            
        }
    }
}