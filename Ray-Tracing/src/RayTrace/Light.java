package RayTrace;



class Light{
     //double x, y, z;
     Vector pos;
     double r, g, b;
    double spec_intens, shadow_intens, radius;

    public Light(double x, double y, double z,
                 double r, double g, double b,
                 double spec_intens, double shadow_intens, double radius){
    	this.pos = new Vector(x,y,z);
       // this.x = x;
       // this.y = y;
       // this.z = z;
        this.r = r;
        this.g = g;
        this.b = b;;
        this.spec_intens = spec_intens;
        this.shadow_intens = shadow_intens;
        this.radius = radius;                
    }

   
    
}