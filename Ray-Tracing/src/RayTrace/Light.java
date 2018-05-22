package RayTrace;

class Light{
     //float x, y, z;
     Vector pos;
     float r, g, b;
    float spec_intens, shadow_intens, radius;

    public Light(float x, float y, float z,
                 float r, float g, float b,
                 float spec_intens, float shadow_intens, float radius){
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