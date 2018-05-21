
class Plane extends Surface{
    Vector normal;
    
    Plane(float x, float y, float z, Material mat){
        this.normal = new Vector(x, y, z);
        this.mat = mat;
    }
}