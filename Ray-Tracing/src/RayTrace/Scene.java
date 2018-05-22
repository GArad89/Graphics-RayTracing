package RayTrace;

import java.util.ArrayList;

class Scene{
    
    private float bg_r, bg_g, bg_b;
    private int rays_num, max_recurs, sample_lev;
    private Camera cam;
    private ArrayList<Surface> surfs;
    private ArrayList<Light> lights;

    
    public Scene(float bg_r, float bg_g, float bg_b, int rays_num, int max_recurs, int sample_lev){
        this.bg_r = bg_r;
        this.bg_g = bg_g;
        this.bg_b = bg_b;
        this.rays_num = rays_num;
        this.max_recurs = max_recurs;
        this.sample_lev = sample_lev;
        this.surfs = new ArrayList<Surface>();
        this.lights = new ArrayList<Light>();
    }
    
    public void setCam(Camera cam){
        this.cam = cam;
    }
    
    public void addSurface(Surface surf){
        this.surfs.add(surf);
    }
    
    public void addLight(Light light){
        this.lights.add(light);
    }
    
    public void Render(byte[] rgbData, int imageWidth,int imageHeight) {
        int i=0;
        int j=0;
        float[] lightInt = new float[rgbData.length];
       // float mod=0; //need a better name
        Vector camleft = this.cam.look_v.cross(this.cam.up); //the "left direction" of the camera.
        camleft = camleft.prod(1/camleft.size()); //normalize camleft
       // Vector pixelCord= null; //the coordinate of the pixel (bottom left corner for now, no supersampling yet)
        Vector p_0 = this.cam.look_v.prod(this.cam.screen_dist);  //the vector to pixel[0,0]
        //mod = this.cam.look.minus(this.cam.pos).size();
        p_0 = p_0.plus(camleft.prod((float)(0.5*this.cam.screen_width)));
        float screen_height = ((float)imageHeight/(float)imageWidth)*this.cam.screen_width;
        p_0 = p_0.plus(this.cam.up.prod((float)(-0.5*screen_height))); //this is a vector from pos to p_0. NOT the coordinates of p_0
        Vector j_step = camleft.prod((float)((-this.cam.screen_width)/((float)imageWidth))); 
        Vector i_step = this.cam.up.prod((float)((screen_height)/((float)imageHeight))); 
        Ray camray = null;
        Ray lightray = null;
        int min_ind=-1;
        float min_val=Float.POSITIVE_INFINITY;
        float temp;
        float maxlight =0;
        
        for(i=0;i<imageHeight;i++) {
        	for(j=0;j<imageWidth;j++) {
        		camray = new Ray(this.cam.pos,p_0.plus(j_step.prod((float)j).plus(i_step.prod((float)i))));
        		for(int k=0;k< this.surfs.size();k++) {
        			temp = this.surfs.get(k).intersect(camray);
        			if(temp < min_val) {
        				min_ind = k;
        				min_val = temp;
        			}
        		}
//        		pixelCord = camleft.prod(((float)j)/((float)imageWidth));
//        		pixelCord = pixelCord.plus(this.cam.up.prod(((float)i)/((float)imageHeight)));
//        		pixelCord = pixelCord.plus(this.cam.look.prod(this.cam.screen_dist));
//        		camray = new Ray()
        		if(min_ind > -1) {
        			for(int k=0; k< this.lights.size();k++) {
        				lightray = new Ray(this.lights.get(k).pos, new Vector(this.lights.get(k).pos,camray.getPos(min_val)),this.lights.get(k));
        				temp = Float.POSITIVE_INFINITY;
        				for(int m=0; m<this.surfs.size();m++) {
        					temp = Math.min(this.surfs.get(m).intersect(lightray),temp);
        				}
        				if(temp == this.surfs.get(min_ind).intersect(lightray)) {
        					lightInt[(j+(imageHeight-1-i)*imageWidth)*3] += lightray.light.r;
        					
        					 
        					maxlight = Math.max(maxlight, lightInt[(j+(imageHeight-1-i)*imageWidth)*3]);
        					lightInt[(j+(imageHeight-1-i)*imageWidth)*3+1] += lightray.light.g;
        					maxlight = Math.max(maxlight, lightInt[(j+(imageHeight-1-i)*imageWidth)*3+1]);
        					lightInt[(j+(imageHeight-1-i)*imageWidth)*3+2] += lightray.light.b;
        					maxlight = Math.max(maxlight, lightInt[(j+(imageHeight-1-i)*imageWidth)*3+2]);
        				}
        			}
	        		rgbData[(j+(imageHeight-1-i)*imageWidth)*3] = (byte) (this.surfs.get(min_ind).mat.diff_r*255);
	        		
	        		rgbData[(j+(imageHeight-1-i)*imageWidth)*3+1] = (byte) (this.surfs.get(min_ind).mat.diff_g*255);
	        		
	        		rgbData[(j+(imageHeight-1-i)*imageWidth)*3+2] = (byte) (this.surfs.get(min_ind).mat.diff_b*255);
        		}
        		else {
             		rgbData[(j+(imageHeight-1-i)*imageWidth)*3] = (byte) (this.bg_r*255);
	        		rgbData[(j+(imageHeight-1-i)*imageWidth)*3+1] = (byte) (this.bg_g*255);
	        		rgbData[(j+(imageHeight-1-i)*imageWidth)*3+2] = (byte) (this.bg_b*255);
        		}
        		min_ind =-1;
        		min_val = Float.POSITIVE_INFINITY;
        	}
        }
      System.out.println(maxlight);
    //  maxlight =5;
       for(int k=0; k< rgbData.length;k++) {
    	// System.out.println(rgbData[k]);
    	   rgbData[k] = (byte) ((float)(rgbData[k])*lightInt[k]/maxlight);
    	// System.out.println(rgbData[k]);
       }
      
    }
    
}