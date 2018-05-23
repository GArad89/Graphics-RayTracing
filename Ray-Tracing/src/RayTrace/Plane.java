package RayTrace;

class Plane extends Surface{
    Vector normal;
    double c;

    protected Plane(){
        
    }
     
    public Plane(Vector normal, double c, Material mat){
        this.normal = normal;
        this.c = c;
        this.mat = mat;
    }     
     
    public Plane(double x, double y, double z, double c, Material mat){
        this.normal = new Vector(x, y, z);
        this.c = c;
        this.mat = mat;
    }
    
    public double intersect(Ray ray){
    	if(ray.direct.dot(normal) == 0) {
    		return Double.POSITIVE_INFINITY;
    	}
        return (c - normal.dot(ray.src))/normal.dot(ray.direct);
    }
    
    public Vector normal(Vector pos){
        return normal;
    }
}