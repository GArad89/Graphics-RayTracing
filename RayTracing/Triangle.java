import java.lang.Math;

class Triangle extends Plane{
    Vector v1, v2, v3;
    Vector n12, n23, n13;
    float c12, c23, c13;
    float sign12, sign23, sign13;
    
    public Triangle(float x1, float y1, float z1,
                    float x2, float y2, float z2,
                    float x3, float y3, float z3,             
                    Material mat){
        this.v1 = new Vector(x1, y1, z1);
        this.v2 = new Vector(x2, y2, z2);
        this.v3 = new Vector(x3, y3, z3);
        Vector v12 = v2.minus(v1), v23 = v3.minus(v2), v13 = v3.minus(v1);
        this.normal = v12.cross(v13);
        
        this.c = v1.dot(this.normal);
        this.mat = mat;
        
        Vector center = v1.plus(v2).plus(v3).prod((float)1./3);
        
        this.n12 = v12.cross(this.normal);
        this.c12 = n12.dot(v1);
        this.sign12 = Math.signum(n12.dot(center) + c12);
        
        this.n23 = v23.cross(this.normal);
        this.c23 = n23.dot(v2);
        this.sign23 = Math.signum(v23.dot(center) + c23);
        
        this.n13 = v13.cross(this.normal);
        this.c13 = n13.dot(v1);
        this.sign13 = Math.signum(v13.dot(center) + c13);
        
        
    }
    
    
    public float intersect(Ray ray){
        float t=super.intersect(ray);
        Vector v=ray.src.plus(ray.direct.prod(t));
        if((Math.signum(n12.dot(v) + c12) == -sign12) ||
           (Math.signum(n23.dot(v) + c23) == -sign23) ||
           (Math.signum(n13.dot(v) + c13) == -sign13))
           return Float.POSITIVE_INFINITY;
        return t;
    }
}