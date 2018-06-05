package RayTrace;

import java.util.Random;



    public class Rect{  //soft shadows rectangle
    	Ray[][] raygrid;
    	static double[][] steps = null;
    	Vector p_0;
    	Vector i_step;
    	Vector j_step;
    	Vector src;
    	Vector direction;
    	Random rand = new Random();
    	public Rect(Vector tar,Vector dir1,Vector dir2, double radius,int N,Light lgt) {
    		raygrid = new Ray[N][N];
    		if(N == 1) {
    			raygrid[0][0] = new Ray(lgt.pos,new Vector(lgt.pos,tar),lgt);
    		}
    		else {
    		dir1= dir1.prod(0.5*radius/(dir1.size()));
    		dir2= dir2.prod(0.5*radius/(dir2.size()));
    		p_0 = lgt.pos.minus(dir2.plus(dir1));
    		i_step = dir1.prod(2/((double)N));
    		j_step = dir2.prod(2/((double)N));
//    		if(N == 1) {
//				src = p_0.plus(0); //rand.nextDouble()
//				src = p_0.plus(j_step.prod(j+rand.nextDouble())); //rand.nextDouble()
//    		}
    		for(int i=0; i<N;i++) {
    			for(int j=0;j<N;j++) {
    				src = p_0.plus(i_step.prod(i+0.2+0.8*rand.nextDouble())); 
    				src = src.plus(j_step.prod(j+0.2+0.8*rand.nextDouble())); //rand.nextDouble()
    				direction = new Vector(src,tar);
    				raygrid[i][j] = new Ray(src,direction,lgt);
    			}
    		}
    		
    		}
    	}
    }
    		
    	

    
    
