package RayTrace;
import java.lang.Math;

class Triangle extends Plane{
    Vector v1, v2, v3;
    Vector n12, n23, n13;

    double sign12, sign23, sign13;
    
    public Triangle(double x1, double y1, double z1,
                    double x2, double y2, double z2,
                    double x3, double y3, double z3,             
                    Material mat){
        this.v1 = new Vector(x1, y1, z1);
        this.v2 = new Vector(x2, y2, z2);
        this.v3 = new Vector(x3, y3, z3);
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
        Vector v=ray.src.plus(ray.direct.prod(t));
        if((Math.signum(n12.dot(v1.minus(v))) == -sign12) ||
           (Math.signum(n23.dot(v2.minus(v))) == -sign23) ||
           (Math.signum(n13.dot(v3.minus(v))) == -sign13))
           return Double.POSITIVE_INFINITY;
        return t;
    }
}