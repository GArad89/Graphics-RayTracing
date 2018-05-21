
class Triangle extends Surface{
    Vector normal;
    float x1, y1, z1;
    float x2, y2, z2;
    float x3, y3, z3;
    
    Triangle(float x1, float y1, float z1,
             float x2, float y2, float z2,
             float x3, float y3, float z3,             
             Material mat){
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        this.x2 = x2;
        this.y2 = y2;
        this.z2 = z2;
        this.x3 = x3;
        this.y3 = y3;
        this.z3 = z3;
        this.normal = (new Vector(x2-x1, y2-y1, z2-z1)).cross(new Vector(x3-x1, y3-y1, z3-z1));
        this.mat = mat;
    }
}