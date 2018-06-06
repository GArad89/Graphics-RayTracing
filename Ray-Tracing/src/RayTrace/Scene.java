package RayTrace;

import java.util.ArrayList;
import java.util.Random;


class Scene{
    
    private double bg_r, bg_g, bg_b;
    private int rays_num, max_recurs, sample_lev, sample_lev2, rays_num2;
    private Camera cam;
    private ArrayList<Surface> surfs;
    private ArrayList<Light> lights;

    
    public Scene(double bg_r, double bg_g, double bg_b, int rays_num, int max_recurs, int sample_lev){
        this.bg_r = bg_r;
        this.bg_g = bg_g;
        this.bg_b = bg_b;
        this.rays_num = rays_num;
        this.rays_num2 = rays_num*rays_num;;
        this.max_recurs = max_recurs;
        this.sample_lev = sample_lev;
        this.sample_lev2 = sample_lev*sample_lev;
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
    
    /*  main logic method in the package. 
     * the methods "shoot" camray and returns the rgb values calculated
     * camray- the ray to be calculated (the ray source is not necessarily from the camera)
     * rec - the ray current recursion value (max_recurs - x ,where x is how many rays "away" from the camera ray is the current ray)
     * ignore - the ray will ignore the surface with this index value if there is intersection with the ray at distance < 0.001.
     *          this makes sure the ray will "come out" if it's source is a bit inside the object.
     * 
     */
    public double[] trace(Ray camray, int rec,int ignore){
        if(rec == -1){
            double rgb[] = {this.bg_r, this.bg_g, this.bg_b};
            return rgb;
        }
  
        Vector dir1, dir2;
        int min_ind=-1;
        double cnt=0;
        double temp, min_val=Double.POSITIVE_INFINITY, alpha;
        double r=0, g=0, b=0, r_spec=0, g_spec=0, b_spec=0, reflect_r=0, reflect_g=0, reflect_b=0;
        double bg_r=this.bg_r, bg_g=this.bg_g, bg_b=this.bg_b;
        double trans;
        Material mat;
        Ray lightray;
        Vector normal, returning_ray, pos=null;
        Rect rect;
        for(int k=0;k< this.surfs.size();k++) {
        	
            temp = this.surfs.get(k).intersect(camray);
	            if( k == ignore && temp < 0.001) { //the ray is starting a bit "inside" the object it is returning from. 
	            	temp = -1;
	            }
            if(temp >= 0 && temp < min_val) {
                min_ind = k;
                min_val = temp;
            }
	        
        }


        if(min_ind > -1) {
            pos = camray.getPos(min_val);
            normal = this.surfs.get(min_ind).normal(pos);
            normal = normal.prod(1/normal.size()); //make sure it is normalized

            mat = this.surfs.get(min_ind).mat;
            trans = mat.trans;
            double light_val,light_total=1,temp2;
            double bg_rgb[], reflect_rgb[];
            if(trans < 1){
                bg_rgb = trace(new Ray(pos, camray.direct), rec-1,min_ind);
                bg_r = bg_rgb[0];
                bg_g = bg_rgb[1];
                bg_b = bg_rgb[2];            
            }            
            returning_ray = camray.direct.minus(normal.prod(normal.dot(camray.direct)));
            returning_ray = normal.prod(-normal.dot(camray.direct)).plus(returning_ray);
            returning_ray = returning_ray.prod(1/returning_ray.size());
            if(mat.reflect){
                reflect_rgb = trace(new Ray(pos, returning_ray), rec-1,min_ind);
                reflect_r = reflect_rgb[0]*mat.reflect_r;
                reflect_g = reflect_rgb[1]*mat.reflect_g;
                reflect_b = reflect_rgb[2]*mat.reflect_b;  
            }
            
            for(int k=0; k<this.lights.size();k++) {
                
                lightray = new Ray(this.lights.get(k).pos, new Vector(this.lights.get(k).pos, pos), this.lights.get(k));
                alpha = returning_ray.dot(lightray.direct);

                alpha = Math.pow(Math.abs(alpha), mat.phong);

                dir1 = lightray.direct.getPerp();
                dir2 = lightray.direct.cross(dir1);
                rect = new Rect(pos,dir1,dir2,lightray.light.radius,this.rays_num,lightray.light);
                for(int x=0;x<this.rays_num;x++) {
                    for(int y=0;y<this.rays_num;y++) {
                        temp2 = Double.POSITIVE_INFINITY;
                        light_val = this.surfs.get(min_ind).intersect(rect.raygrid[x][y]);
                        for(int m=0;m<this.surfs.size();m++) {
                            temp = this.surfs.get(m).intersect(rect.raygrid[x][y]);
                            if(temp >= 0) {
                            	temp2 = Math.min(temp2, temp);
                                if( temp < light_val) {
                                	light_total*= this.surfs.get(m).mat.trans;
                                	if(light_total == 0) {
                                		break;
                                	}
                                }
                            }

                        }

                        if(new Vector(camray.getPos(min_val),rect.raygrid[x][y].getPos(light_val)).size() > 0.001)  //object's surface is obscured from light source by the object itself
                        	light_total*= this.surfs.get(min_ind).mat.trans;
                        else {
                        	if(this.surfs.get(min_ind) instanceof Plane) {
                        		if(Math.signum(camray.direct.dot(normal)) != Math.signum(lightray.direct.dot(normal))){ //object is a plane or triangle and the light is coming from the other side of the camera
                        			light_total *= this.surfs.get(min_ind).mat.trans;
                        		}
                        	}
                        }
                        
                        cnt+= light_total;
                       
                        light_total = 1;
                    }
                }
                
                //i'm not sure why it is happening but we get better results for a sphere without the shadow_intes part and better results for planes with that part.
                int mod =1; //since it is required to add shadow_intesity we keep it "on" but we get closer results (to the examples) for the spheres without it for some reason...

           //the code for removing shadow_intesity from the spheres if someone wants to check it     
//                if(this.surfs.get(min_ind) instanceof Sphere) {
//                	mod =0 ;
//                }
//                else {
//                	mod = 1;
//                }
                temp = (((double)(cnt)+mod*(rays_num2-(double)(cnt))*((double)1.0-lightray.light.shadow_intens))/rays_num2);
     
                temp = Math.abs(temp*normal.dot(lightray.direct));
              
                r += lightray.light.r*temp;
                g += lightray.light.g*temp;
                b += lightray.light.b*temp;


                temp = (double)cnt/rays_num2;
                r_spec += lightray.light.spec_intens*lightray.light.r*alpha*temp;
                g_spec += lightray.light.spec_intens*lightray.light.g*alpha*temp;
                b_spec += lightray.light.spec_intens*lightray.light.b*alpha*temp;

                cnt=0;
            }                            

            r = mat.diff_r*r;
            g = mat.diff_g*g;
            b = mat.diff_b*b;
            r_spec = mat.spec_r*r_spec;
            g_spec = mat.spec_g*g_spec;
            b_spec = mat.spec_b*b_spec;
            
        }
        else {
            double rgb[]={this.bg_r, bg_g, bg_b};
            return rgb;
        }
        double comp_trans=1-trans;
        double rgb[] = {(r+r_spec)*comp_trans+bg_r*trans+reflect_r, (g+g_spec)*comp_trans+bg_g*trans+reflect_g, (b+b_spec)*comp_trans+bg_b*trans+reflect_b};

        return rgb;
    }
    
    /*  main rendering method
     *   rgbData - the array of the rgb values for the image's pixels.
     *   imageWidth,imageHeight - the dimensions of the image
     */
    public void Render(byte[] rgbData, int imageWidth,int imageHeight) {
        int i=0, j=0, si=0, sj=0;   //index variables
        double[] lightInt = new double[rgbData.length];  //light Intensity array
        Random rand = new Random();
        
        //setting up the coordinates for the image's screen
        Vector camleft = this.cam.look_v.cross(this.cam.up); //the "left direction" of the camera.
        camleft = camleft.prod(1/camleft.size()); //normalize camleft
        Vector p_0 = this.cam.look_v.prod(this.cam.screen_dist);  //the vector to pixel[0,0]
        p_0 = p_0.plus(camleft.prod((double)(0.5*this.cam.screen_width)));
        double screen_height = ((double)imageHeight/(double)imageWidth)*this.cam.screen_width;
        p_0 = p_0.plus(this.cam.up.prod((double)(-0.5*screen_height))); //this is a vector from pos to p_0. NOT the coordinates of p_0
        Vector j_step = camleft.prod((double)((-this.cam.screen_width)/((double)imageWidth*sample_lev))); 
        Vector i_step = this.cam.up.prod((double)((screen_height)/((double)imageHeight*sample_lev))); 
        Ray camray = null;
        
        //used to give progression output during the run
        int perc_pixs=imageHeight*imageWidth/100;
        int pixs=0;


  
        
        for(i=0;i<imageHeight;i++) {
        	for(j=0;j<imageWidth;j++) {
                double rgb[];
                int ind = (j+(imageHeight-1-i)*imageWidth)*3;    
                
                //super sampling
                for(si=0;si<sample_lev;si++){
                    for(sj=0;sj<sample_lev;sj++){
                    	
                    	//the "+0.1+0.8*rand.nextDouble()" part is for making sure the the sampling is done inside the sampling area and not on the edge
                        camray = new Ray(this.cam.pos, p_0.plus(j_step.prod((double)j*sample_lev+sj+0.1+0.8*rand.nextDouble()).plus(i_step.prod((double)i*sample_lev+si+0.1+0.8*rand.nextDouble()))));
                        rgb = trace(camray, this.max_recurs,-1); //main logic method
                        lightInt[ind] += rgb[0];
                        lightInt[ind+1] += rgb[1];
                        lightInt[ind+2] += rgb[2];
                        
                        
                    }
                }
                
                //print '.' for each %1 of pixels calculated
                pixs++;
                if(pixs% perc_pixs == 0)
                    System.out.print('.');
                lightInt[ind] /= sample_lev2;
                lightInt[ind+1] /= sample_lev2;
                lightInt[ind+2] /=  sample_lev2;
            }
        }
        System.out.println('.');
       for(int k=0; k< rgbData.length;k++){
           rgbData[k] =(byte) (255*(Math.min(1, lightInt[k])));
       }
      
    }
    
}