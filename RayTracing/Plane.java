
class Plane extends Surface{
    Vector normal;
    float c;

    protected Plane(){
        
    }
     
    public Plane(Vector normal, float c, Material mat){
        this.normal = normal;
        this.c = c;
        this.mat = mat;
    }     
     
    public Plane(float x, float y, float z, float c, Material mat){
        this.normal = new Vector(x, y, z);
        this.c = c;
        this.mat = mat;
    }
    
    public float intersect(Ray ray){
        return (c - normal.dot(ray.src))/normal.dot(ray.direct);
    }
    
    public Vector normal(Vector pos){
        return normal;
    }
}