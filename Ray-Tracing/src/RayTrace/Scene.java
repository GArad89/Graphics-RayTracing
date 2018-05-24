package RayTrace;

import java.util.ArrayList;

class Scene{
    
    private double bg_r, bg_g, bg_b;
    private int rays_num, max_recurs, sample_lev;
    private Camera cam;
    private ArrayList<Surface> surfs;
    private ArrayList<Light> lights;

    
    public Scene(double bg_r, double bg_g, double bg_b, int rays_num, int max_recurs, int sample_lev){
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
        double[] lightInt = new double[rgbData.length];
       // double mod=0; //need a better name
        Vector camleft = this.cam.look_v.cross(this.cam.up); //the "left direction" of the camera.
        camleft = camleft.prod(1/camleft.size()); //normalize camleft
       // Vector pixelCord= null; //the coordinate of the pixel (bottom left corner for now, no supersampling yet)
        Vector p_0 = this.cam.look_v.prod(1/this.cam.screen_dist);  //the vector to pixel[0,0]
        //mod = this.cam.look.minus(this.cam.pos).size();
        p_0 = p_0.plus(camleft.prod((double)(0.5*this.cam.screen_width)));
        double screen_height = ((double)imageHeight/(double)imageWidth)*this.cam.screen_width;
        p_0 = p_0.plus(this.cam.up.prod((double)(-0.5*screen_height))); //this is a vector from pos to p_0. NOT the coordinates of p_0
        Vector j_step = camleft.prod((double)((-this.cam.screen_width)/((double)imageWidth))); 
        Vector i_step = this.cam.up.prod((double)((screen_height)/((double)imageHeight))); 
        Ray camray = null;
        Ray lightray = null;
        int min_ind=-1;
        double min_val=Double.POSITIVE_INFINITY;
        double temp;
        double maxlight =0;
       // Vector dir1,dir2;
        //Rect rect;
        
        for(i=0;i<imageHeight;i++) {
        	for(j=0;j<imageWidth;j++) {
        		camray = new Ray(this.cam.pos,p_0.plus(j_step.prod((double)j).plus(i_step.prod((double)i))));
        		for(int k=0;k< this.surfs.size();k++) {
        			temp = this.surfs.get(k).intersect(camray);
        			if(temp < min_val) {
        				min_ind = k;
        				min_val = temp;
        			}
        		}
//        		pixelCord = camleft.prod(((double)j)/((double)imageWidth));
//        		pixelCord = pixelCord.plus(this.cam.up.prod(((double)i)/((double)imageHeight)));
//        		pixelCord = pixelCord.plus(this.cam.look.prod(this.cam.screen_dist));
//        		camray = new Ray()
        		if(min_ind > -1) {
        			for(int k=0; k< this.lights.size();k++) {
        				//for(int n=0;n<=this.lights.get(k).radius)
        				lightray = new Ray(this.lights.get(k).pos, new Vector(this.lights.get(k).pos,camray.getPos(min_val)),this.lights.get(k));
        			//	if(this.rays_num == 1) {  //hard shadows only
	        				
	        				temp = Double.POSITIVE_INFINITY;
	        				for(int m=0; m<this.surfs.size();m++) {
	        					temp = Math.min(this.surfs.get(m).intersect(lightray),temp);
	        				}
	        				if(new Vector(camray.getPos(min_val),lightray.getPos(temp)).size() < 0.001) {  //surface isn't obscured from light source
	        					//System.out.println("wow");
	        					lightInt[(j+(imageHeight-1-i)*imageWidth)*3] += lightray.light.r;
	        					
	        					 
	        					maxlight = Math.max(maxlight, lightInt[(j+(imageHeight-1-i)*imageWidth)*3]);
	        					lightInt[(j+(imageHeight-1-i)*imageWidth)*3+1] += lightray.light.g;
	        					maxlight = Math.max(maxlight, lightInt[(j+(imageHeight-1-i)*imageWidth)*3+1]);
	        					lightInt[(j+(imageHeight-1-i)*imageWidth)*3+2] += lightray.light.b;
	        					maxlight = Math.max(maxlight, lightInt[(j+(imageHeight-1-i)*imageWidth)*3+2]);
	        				}
	        				else { //surface is obscured from light source
	        					lightInt[(j+(imageHeight-1-i)*imageWidth)*3] += lightray.light.r*(1-lightray.light.shadow_intens);
	        					maxlight = Math.max(maxlight, lightInt[(j+(imageHeight-1-i)*imageWidth)*3]);
	        					lightInt[(j+(imageHeight-1-i)*imageWidth)*3+1] += lightray.light.g*(1-lightray.light.shadow_intens);
	        					maxlight = Math.max(maxlight, lightInt[(j+(imageHeight-1-i)*imageWidth)*3+1]);
	        					lightInt[(j+(imageHeight-1-i)*imageWidth)*3+2] += lightray.light.b*(1-lightray.light.shadow_intens);
	        					maxlight = Math.max(maxlight, lightInt[(j+(imageHeight-1-i)*imageWidth)*3+2]);
	        				}
        		//		}
//        				else { //using soft shadows
//        					dir1 = lightray.direct.getPerp();
//        					dir2 = lightray.direct.cross(dir1);
//        					rect = new Rect(camray.getPos(min_val),dir1,dir2,lightray.light.radius,this.rays_num,lightray.light);
//        					for(int x=0;x<this.rays_num;x++) {
//        						for(int y=0;y<this.rays_num;y++) {
//        							temp = Double.POSITIVE_INFINITY;
//        							for(int m=0;m<this.surfs.size();m++) {
//        								temp = Math.min(this.surfs.get(m).intersect(rect.raygrid[x][y]),temp);
//        							}
//        							if(temp == this.surfs.get(min_ind).intersect(rect.raygrid[x][y])) {  //surface isn't obscured from light source
//        	        					lightInt[(j+(imageHeight-1-i)*imageWidth)*3] += rect.raygrid[x][y].light.r;
//        	        					
//        	        					 
//        	        					maxlight = Math.max(maxlight, lightInt[(j+(imageHeight-1-i)*imageWidth)*3]);
//        	        					lightInt[(j+(imageHeight-1-i)*imageWidth)*3+1] += rect.raygrid[x][y].light.g;
//        	        					maxlight = Math.max(maxlight, lightInt[(j+(imageHeight-1-i)*imageWidth)*3+1]);
//        	        					lightInt[(j+(imageHeight-1-i)*imageWidth)*3+2] += rect.raygrid[x][y].light.b;
//        	        					maxlight = Math.max(maxlight, lightInt[(j+(imageHeight-1-i)*imageWidth)*3+2]);
//        	        				}
//        	        				else { //surface is obscured from light source
//        	        					lightInt[(j+(imageHeight-1-i)*imageWidth)*3] += rect.raygrid[x][y].light.r*(1-rect.raygrid[x][y].light.shadow_intens);
//        	        					maxlight = Math.max(maxlight, lightInt[(j+(imageHeight-1-i)*imageWidth)*3]);
//        	        					lightInt[(j+(imageHeight-1-i)*imageWidth)*3+1] += rect.raygrid[x][y].light.g*(1-rect.raygrid[x][y].light.shadow_intens);
//        	        					maxlight = Math.max(maxlight, lightInt[(j+(imageHeight-1-i)*imageWidth)*3+1]);
//        	        					lightInt[(j+(imageHeight-1-i)*imageWidth)*3+2] += rect.raygrid[x][y].light.b*(1-rect.raygrid[x][y].light.shadow_intens);
//        	        					maxlight = Math.max(maxlight, lightInt[(j+(imageHeight-1-i)*imageWidth)*3+2]);
//        	        				}
//        						}
//        					}
//        				}
        			}
	        		rgbData[(j+(imageHeight-1-i)*imageWidth)*3] = (byte) (this.surfs.get(min_ind).mat.diff_r*255*lightInt[(j+(imageHeight-1-i)*imageWidth)*3]);
	        		
	        		rgbData[(j+(imageHeight-1-i)*imageWidth)*3+1] = (byte) (this.surfs.get(min_ind).mat.diff_g*255*lightInt[(j+(imageHeight-1-i)*imageWidth)*3+1]);
	        		
	        		rgbData[(j+(imageHeight-1-i)*imageWidth)*3+2] = (byte) (this.surfs.get(min_ind).mat.diff_b*255*lightInt[(j+(imageHeight-1-i)*imageWidth)*3+2]);
        		}
        		else {
             		rgbData[(j+(imageHeight-1-i)*imageWidth)*3] = (byte) (this.bg_r*255);
	        		rgbData[(j+(imageHeight-1-i)*imageWidth)*3+1] = (byte) (this.bg_g*255);
	        		rgbData[(j+(imageHeight-1-i)*imageWidth)*3+2] = (byte) (this.bg_b*255);
        		}
        		min_ind =-1;
        		min_val = Double.POSITIVE_INFINITY;
        	}
        }

      System.out.println(maxlight);
     maxlight =1;
//      for(int k=0; k< rgbData.length;k++) {
//       maxlight=Math.max(maxlight, rgbData[k]);
//      }
       for(int k=0; k< rgbData.length;k++) {
    	 //System.out.println(rgbData[k]);
    	 //  if(lightInt[k] > 0){
    		   rgbData[k] =(byte) ((rgbData[k])*(1/(maxlight)));
    	  // }
    	 //System.out.println(rgbData[k]);
       }
      
    }
    
}