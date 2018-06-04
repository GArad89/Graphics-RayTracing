package RayTrace;

import java.util.ArrayList;
import java.util.Random;


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
        int i=0, j=0, si=0, sj=0;
        double[] lightInt = new double[rgbData.length];
        double[] lightInt_spec = new double[rgbData.length];
       // double mod=0; //need a better name
        Vector camleft = this.cam.look_v.cross(this.cam.up); //the "left direction" of the camera.
        camleft = camleft.prod(1/camleft.size()); //normalize camleft
       // Vector pixelCord= null; //the coordinate of the pixel (bottom left corner for now, no supersampling yet)
        Vector p_0 = this.cam.look_v.prod(this.cam.screen_dist);  //the vector to pixel[0,0]
        //mod = this.cam.look.minus(this.cam.pos).size();
        p_0 = p_0.plus(camleft.prod((double)(0.5*this.cam.screen_width)));
        double screen_height = ((double)imageHeight/(double)imageWidth)*this.cam.screen_width;
        p_0 = p_0.plus(this.cam.up.prod((double)(-0.5*screen_height))); //this is a vector from pos to p_0. NOT the coordinates of p_0
        Vector j_step = camleft.prod((double)((-this.cam.screen_width)/((double)imageWidth*sample_lev))); 
        Vector i_step = this.cam.up.prod((double)((screen_height)/((double)imageHeight*sample_lev))); 
        Ray camray = null;
        Ray lightray = null;
        int min_ind=-1;
        double min_val=Double.POSITIVE_INFINITY;
        double temp = 0;
        double[] maxlight =new double[3];
        Vector dir1,dir2;
        Rect rect;
        Vector normal=null;
        Double alpha =0.0;
        Vector returning_ray;
        int cnt=0;
        Random rand = new Random();
        
        for(i=0;i<imageHeight;i++) {
        	for(j=0;j<imageWidth;j++) {
                int ind = (j+(imageHeight-1-i)*imageWidth)*3;
                lightInt[ind] = 0;
                lightInt[ind+1] = 0;
                lightInt[ind+2] = 0;
                lightInt_spec[ind+1] = 0;
                lightInt_spec[ind] = 0;
                lightInt_spec[ind+2] = 0;
                
                for(si=0;si<sample_lev;si++){
                    for(sj=0;sj<sample_lev;sj++){
                        double r=0, g=0, b=0, r_spec=0, g_spec=0, b_spec=0;
                        min_ind=-1;
                        camray = new Ray(this.cam.pos,p_0.plus(j_step.prod((double)j*sample_lev+sj+rand.nextDouble()).plus(i_step.prod((double)i*sample_lev+si+rand.nextDouble()))));
                        for(int k=0;k< this.surfs.size();k++) {
                            temp = this.surfs.get(k).intersect(camray);
                            if(temp >= 0 && temp < min_val) {
                                min_ind = k;
                                min_val = temp;
                            }
                        }

        //        		pixelCord = camleft.prod(((double)j)/((double)imageWidth));
        //        		pixelCord = pixelCord.plus(this.cam.up.prod(((double)i)/((double)imageHeight)));
        //        		pixelCord = pixelCord.plus(this.cam.look.prod(this.cam.screen_dist));
        //        		camray = new Ray()
                        if(min_ind > -1) {
                            normal = this.surfs.get(min_ind).normal(camray.direct.prod(min_val).plus(camray.src));
                            normal = normal.prod(1/normal.size()); //make sure it is normalized

                            for(int k=0; k<this.lights.size();k++) {
                                //for(int n=0;n<=this.lights.get(k).radius)
                                lightray = new Ray(this.lights.get(k).pos, new Vector(this.lights.get(k).pos,camray.getPos(min_val)),this.lights.get(k));
                                returning_ray = camray.direct.minus(normal.prod(normal.dot(camray.direct)));
                                returning_ray = normal.prod(-normal.dot(camray.direct)).plus(returning_ray);
                                returning_ray = returning_ray.prod(1/returning_ray.size());
                                alpha = returning_ray.dot(lightray.direct);
                                
                                alpha = Math.pow(Math.abs(alpha), this.surfs.get(min_ind).mat.phong);
                                
                                if(this.rays_num == 1) {  //hard shadows only 

                                    temp = Double.POSITIVE_INFINITY;
                                    for(int m=0; m<this.surfs.size();m++) {
                                        temp = Math.min(this.surfs.get(m).intersect(lightray),temp);
                                    }
                                    if(new Vector(camray.getPos(min_val),lightray.getPos(temp)).size() < 0.001) {  //surface isn't obscured from light source
                                        //System.out.println("wow");
                                        cnt++;
                                        temp = normal.dot(lightray.direct)*-1;
                                        r += lightray.light.r*temp;
                                        g += lightray.light.g*temp;
                                        b += lightray.light.b*temp;

                                        //specular light
                                        r_spec += lightray.light.spec_intens*lightray.light.r*alpha;
                                        g_spec += lightray.light.spec_intens*lightray.light.g*alpha;
                                        b_spec += lightray.light.spec_intens*lightray.light.b*alpha;
                                    }
                                    else { //surface is obscured from light source
                                        r += lightray.light.r*(1-lightray.light.shadow_intens);
                                        //lightInt[ind] /= 2;
                                        //maxlight[0] = Math.max(maxlight[0], lightInt[ind]);
                                        g += lightray.light.g*(1-lightray.light.shadow_intens);
                                        //lightInt[ind+1] /= 2;
                                        //maxlight[1] = Math.max(maxlight[1], lightInt[ind+1]);
                                        b += lightray.light.b*(1-lightray.light.shadow_intens);
                                        //lightInt[ind+2] /= 2;
                                        //maxlight[2] = Math.max(maxlight[2], lightInt[ind+2]);

                                    }
                                }
                                else { //using soft shadows
                                    dir1 = lightray.direct.getPerp();

                                    dir2 = lightray.direct.cross(dir1);
                                    rect = new Rect(camray.getPos(min_val),dir1,dir2,lightray.light.radius,this.rays_num,lightray.light);
                                    for(int x=0;x<this.rays_num;x++) {
                                        for(int y=0;y<this.rays_num;y++) {
                                            temp = Double.POSITIVE_INFINITY;
                                            for(int m=0;m<this.surfs.size();m++) {
                                                temp = Math.min(this.surfs.get(m).intersect(rect.raygrid[x][y]),temp);
                                            }
                                            if(new Vector(camray.getPos(min_val),rect.raygrid[x][y].getPos(temp)).size() < 0.001) {  //surface isn't obscured from light source
                                                cnt++;

                                            }
                                        }
                                    }
                                    temp = (double) this.rays_num*this.rays_num;
                                    temp = (((double)(cnt)+(temp-(double)(cnt))*((double)1.0-lightray.light.shadow_intens))/temp);
                                    temp = Math.abs(temp*normal.dot(lightray.direct));
                                    //System.out.println(temp);
                                    r += lightray.light.r*temp;
        //        					lightInt[ind] /= 2;
                                    //maxlight[0] = Math.max(maxlight[0], lightInt[ind]);
                                    g += lightray.light.g*temp;
        //        					lightInt[ind+1] /= 2;
                                    //maxlight[1] = Math.max(maxlight[1], lightInt[ind+1]);
                                    b += lightray.light.b*temp;
        //        					lightInt[ind+2] /= 2;
                                    //maxlight[2] = Math.max(maxlight[2], lightInt[ind+2]);
                                    //if(cnt == this.rays_num*this.rays_num ) {
                                        //System.out.println("wow");
                                    temp = (double) this.rays_num*this.rays_num;
                                    temp = (double)cnt/temp;
                                    r_spec += lightray.light.spec_intens*lightray.light.r*alpha*temp;
                                    g_spec += lightray.light.spec_intens*lightray.light.g*alpha*temp;
                                    g_spec += lightray.light.spec_intens*lightray.light.b*alpha*temp;

                                    cnt=0;
                                }
                            }

                            r +=  (this.surfs.get(min_ind).mat.diff_r*255*(r));
                            g += (this.surfs.get(min_ind).mat.diff_g*255*(g));
                            b +=  (this.surfs.get(min_ind).mat.diff_b*255*(b));
                            r +=  (this.surfs.get(min_ind).mat.spec_r*255*(r_spec));
                            g += (this.surfs.get(min_ind).mat.spec_g*255*(g_spec));
                            b +=  (this.surfs.get(min_ind).mat.spec_b*255*(b_spec));
                        }
                        else {
                            /*lightInt[ind] +=  (this.bg_r*255);
                            lightInt[ind+1] += (this.bg_g*255);
                            lightInt[ind+2] +=  (this.bg_b*255);*/
                        }
                        min_ind =-1;
                        min_val = Double.POSITIVE_INFINITY;
                        lightInt[ind] += r;
                        lightInt[ind+1] += g;
                        lightInt[ind+2] += b;
                        lightInt_spec[ind] += r_spec;
                        lightInt_spec[ind+1] += g_spec;
                        lightInt_spec[ind+2] += b_spec;

                    }
                }
                lightInt[ind] /= sample_lev*sample_lev;
                lightInt[ind+1] /= sample_lev*sample_lev;
                lightInt[ind+2] /=  sample_lev*sample_lev;
                lightInt_spec[ind] /= sample_lev*sample_lev;
                lightInt_spec[ind+1] /= sample_lev*sample_lev;
                lightInt_spec[ind+2] /=  sample_lev*sample_lev;
            }
        }
      System.out.println(this.bg_b);
    // maxlight[0] =0;
      for(int k=0; k< rgbData.length/3;k++) {
       //lightInt[k]+= lightInt_spec[k];
       if(lightInt_spec[3*k+2] > 0) {
    	   //System.out.println(lightInt_spec[3*k+1]);
       }
       maxlight[0]=Math.max(maxlight[0], lightInt[3*k]);
       maxlight[0]=Math.max(maxlight[0], lightInt[3*k+1]);
       maxlight[0]=Math.max(maxlight[0], lightInt[3*k+2]);
       maxlight[1]=Math.max(maxlight[1], lightInt_spec[3*k]);
       maxlight[1]=Math.max(maxlight[1], lightInt_spec[3*k+1]);
       maxlight[1]=Math.max(maxlight[1], lightInt_spec[3*k+2]);
      }
    System.out.println(maxlight[0]);
       for(int k=0; k< rgbData.length;k++) {
    	 //System.out.println(rgbData[k]);
    	 //  if(lightInt[k] > 0){
    	   if(maxlight[1] > 0) {
    		   rgbData[k] =(byte) Math.min(255,((1*lightInt[k])+lightInt_spec[k]));
    	   }
    	   else {
    		   rgbData[k] =(byte) Math.min(255,((lightInt[k]/this.lights.size())));
    	   }
    	  ///rgbData[k] =(byte) ((rgbData[k])*(1/(maxlight[1])));
    		 //  rgbData[3*k+2] =(byte) ((rgbData[3*k+2])*(1/(maxlight[2])));
    	  // }
    	 //System.out.println(rgbData[k]);
       }
      
    }
    
}