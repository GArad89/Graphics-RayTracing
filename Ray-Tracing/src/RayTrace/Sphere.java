package RayTrace;

class Sphere extends Surface{
    Vector center;
    float radius, radius2, center2;
    
    Sphere(float x, float y, float z, float radius, Material mat){
        this.center = new Vector(x, y, z);
        this.radius = radius;
        this.radius2 = radius*radius;
        this.center2 = center.dot(center);
        this.mat = mat;
    }
    
    public Vector normal(Vector pos){
        Vector n = pos.minus(center);
        return n;
    }
    
    
    public float intersect(Ray ray){
        float a, b, c, delta;
        a = ray.direct2;
        b = 2*(ray.src_dot_direct - center.dot(ray.direct));
        c = ray.src2 - 2*center.dot(ray.src) + center2 - radius2;
        System.out.println(new Vector(a, b, c));
        if(b >= 0)
            if(c > 0)
                return Float.POSITIVE_INFINITY;
            else { 
                delta = b*b - 4*a*c;
                if (delta >= 0)
                    return (-b+(float)Math.sqrt(delta))/(2*a);
                else
                    return Float.POSITIVE_INFINITY;
            }
        delta = b*b - 4*a*c;
        if(delta < 0)
            return Float.POSITIVE_INFINITY;
        return (-b-(float)Math.sqrt(delta))/(2*a);    
    }

}