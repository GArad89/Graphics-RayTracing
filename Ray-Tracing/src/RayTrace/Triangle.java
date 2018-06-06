package RayTrace;
import java.lang.Math;

class Triangle extends Plane{
    Vector v1, v2, v3;
    Vector n12, n23, n13; //normals to the v12,v23,v13 vectors accordingly
    double min_x,max_x,min_y,max_y,min_z,max_z; //min-max values of the vertexes
    double sign12, sign23, sign13; // the sign of the center compared to the normals. 
    //points inside the triangle should have the same sign as the center
    
    public Triangle(double x1, double y1, double z1,
                    double x2, double y2, double z2,
                    double x3, double y3, double z3,             
                    Material mat){
        this.v1 = new Vector(x1, y1, z1);
        this.v2 = new Vector(x2, y2, z2);
        this.v3 = new Vector(x3, y3, z3);
        
        min_x = Math.min(x1, Math.min(x2,x3));
        max_x = Math.max(x1, Math.max(x2,x3));
        min_y = Math.min(y1, Math.min(y2,y3));
        max_y = Math.max(y1, Math.max(y2,y3));
        min_z = Math.min(z1, Math.min(z2,z3));
        max_z = Math.max(z1, Math.max(z2,z3));
        Vector v12 = v2.minus(v1), v23 = v3.minus(v2), v13 = v3.minus(v1);
        this.normal = v12.cross(v13);
        
        this.c = v1.dot(this.normal);
        this.mat = mat;
        
        Vector center = v1.plus(v2).plus(v3).prod((double)1./3);
        
        this.n12 = v12.cross(this.normal);
  
        this.sign12 = Math.signum(n12.dot(v1.minus(center)));
        
        this.n23 = v23.cross(this.normal);

        this.sign23 = Math.signum(n12.dot(v2.minus(center)));
        
        this.n13 = v13.cross(this.normal);
   
        this.sign13 = Math.signum(n13.dot(v3.minus(center)));
        
        
    }
    
    
    public double intersect(Ray ray){
        double t=super.intersect(ray);
        if(t < 0) // the ray is not directed at the triangle
        	return t;
        Vector v=ray.src.plus(ray.direct.prod(t));
        
        //making sure that the intersection is in the area of the triangle before making further calculations
        if(v.x < min_x || v.x > max_x || v.y < min_y || v.y > max_y || v.z < min_z || v.z > max_z)
        	return Double.POSITIVE_INFINITY;
        
        if((Math.signum(n12.dot(v1.minus(v))) == -sign12) ||
           (Math.signum(n23.dot(v2.minus(v))) == -sign23) ||
           (Math.signum(n13.dot(v3.minus(v))) == -sign13))
           return Double.POSITIVE_INFINITY;
        return t;
    }
}