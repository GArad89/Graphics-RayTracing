package RayTrace;

import java.util.Random;



    public class Rect{  //soft shadows rectangle
    	Ray[][] raygrid;
    	Vector p_0;
    	Vector i_step;
    	Vector j_step;
    	Vector src;
    	Vector direction;
    	public Rect(Vector tar,Vector dir1,Vector dir2, double radius,int N,Light lgt) {
    		raygrid = new Ray[N][N];
    		Random rand = new Random();
    		dir1= dir1.prod(radius/(dir1.size()));
    		dir2= dir2.prod(radius/(dir2.size()));
    		p_0 = lgt.pos.plus(dir1.plus(dir2));
    		i_step = dir1.prod(1/((double)N));
    		j_step = dir2.prod(1/((double)N));
    		for(int i=0; i<N;i++) {
    			for(int j=0;j<N;j++) {
    				src = p_0.plus(i_step.prod(i+rand.nextDouble()));
    				src = p_0.plus(j_step.prod(j+rand.nextDouble()));
    				direction = new Vector(src,tar);
    				raygrid[i][j] = new Ray(src,direction,lgt);
    			}
    		}
    		
    	}
    }
    
