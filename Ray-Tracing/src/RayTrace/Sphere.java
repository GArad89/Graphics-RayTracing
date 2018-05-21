
class Sphere extends Surface{
    Vector normal;
    
    Sphere(float x, float y, float z, Material mat){
        this.normal = new Vector(x, y, z);
        this.mat = mat;
    }
}