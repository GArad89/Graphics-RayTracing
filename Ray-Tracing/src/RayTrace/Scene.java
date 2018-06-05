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
    
    public double[] trace(Ray camray, int rec){
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
            pos = camray.getPos(min_val);
            normal = this.surfs.get(min_ind).normal(pos);
            normal = normal.prod(1/normal.size()); //make sure it is normalized
            mat = this.surfs.get(min_ind).mat;
            trans = mat.trans;
            double light_val,light_total=1,temp2;
            double bg_rgb[], reflect_rgb[];
            if(trans < 1){
                bg_rgb = trace(new Ray(pos, camray.direct), rec-1);
                bg_r = bg_rgb[0];
                bg_g = bg_rgb[1];
                bg_b = bg_rgb[2];            
            }            
            returning_ray = camray.direct.minus(normal.prod(normal.dot(camray.direct)));
            returning_ray = normal.prod(-normal.dot(camray.direct)).plus(returning_ray);
            returning_ray = returning_ray.prod(1/returning_ray.size());
            if(mat.reflect){
                reflect_rgb = trace(new Ray(pos, returning_ray), rec-1);
                reflect_r = reflect_rgb[0]*mat.reflect_r;
                reflect_g = reflect_rgb[1]*mat.reflect_g;
                reflect_b = reflect_rgb[2]*mat.reflect_b;  
            }
            
            for(int k=0; k<this.lights.size();k++) {
                //for(int n=0;n<=this.lights.get(k).radius)
                
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
//                                	if(light_total > 0)
//                                		System.out.println(light_total);
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
                        			light_total = 0;
                        		}
                        	}
                        }
                        
                        cnt+= light_total;
                       
                        light_total = 1;
                    }
                }
                temp = (((double)(cnt)+(rays_num2-(double)(cnt))*((double)1.0-lightray.light.shadow_intens))/rays_num2);
     
                temp = Math.abs(temp*normal.dot(lightray.direct));
              
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
        /*if(trans == 0.8)
            for(int i=0;i<3;i++)
               System.out.println(rgb[i]);*/
        return rgb;
    }
    
    public void Render(byte[] rgbData, int imageWidth,int imageHeight) {
        int i=0, j=0, si=0, sj=0;
        double[] lightInt = new double[rgbData.length];
       // double mod=0; //need a better name
        Vector camleft = this.cam.look_v.cross(this.cam.up); //the "left direction" of the camera.
        camleft = camleft.prod(1/camleft.size()); //normalize camleft
       // Vector pixelCord= null; //the coordinate of the pixel (bottom left corner for now, no supersampling yet)
        Vector p_0 = this.cam.look_v.prod(this.cam.screen_dist);  //the vector to pixel[0,0]
        //mod = this.cam.look.minus(this.cam.pos).size();
        p_0 = p_0.plus(camleft.prod((double)(0.5*this.cam.screen_width)));
        int perc_pixs=imageHeight*imageWidth/100;
        int pixs=0;
        double screen_height = ((double)imageHeight/(double)imageWidth)*this.cam.screen_width;
        p_0 = p_0.plus(this.cam.up.prod((double)(-0.5*screen_height))); //this is a vector from pos to p_0. NOT the coordinates of p_0
        Vector j_step = camleft.prod((double)((-this.cam.screen_width)/((double)imageWidth*sample_lev))); 
        Vector i_step = this.cam.up.prod((double)((screen_height)/((double)imageHeight*sample_lev))); 
        Ray camray = null;

        Random rand = new Random();
        
        for(i=0;i<imageHeight;i++) {
        	for(j=0;j<imageWidth;j++) {
                double rgb[];
                int ind = (j+(imageHeight-1-i)*imageWidth)*3;                
                for(si=0;si<sample_lev;si++){
                    for(sj=0;sj<sample_lev;sj++){
                        camray = new Ray(this.cam.pos, p_0.plus(j_step.prod((double)j*sample_lev+sj+rand.nextDouble()).plus(i_step.prod((double)i*sample_lev+si+rand.nextDouble()))));
                        rgb = trace(camray, this.max_recurs);
                        lightInt[ind] += rgb[0];
                        lightInt[ind+1] += rgb[1];
                        lightInt[ind+2] += rgb[2];
                        
                        
                    }
                }
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