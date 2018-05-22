package RayTrace;
class Vector{

    float x, y, z;

    public Vector(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector(Vector v1,Vector v2) { //v1,v2 are points in x,y,z coordinates
    	this.x = v2.x - v1.x;
    	this.y = v2.y - v1.y;
    	this.z = v2.z - v1.z;
    	//normalizing vector size
    	float size = (float) Math.sqrt(this.x*this.x+this.y*this.y+this.z*this.z);
    	this.x /=size;
    	this.y /=size;
    	this.z /=size;
    }
    public float dot(Vector other){
        return this.x*other.x + this.y*other.y + this.z*other.z;
    }
    
    public Vector cross(Vector other){
        return new Vector(this.y*other.z - this.z*other.y,
                      this.z*other.x - this.x*other.z,
                      this.x*other.y - this.y*other.x);
    }
    
    public Vector prod(float scalar){
        return new Vector(scalar*this.x,
                          scalar*this.y,
                          scalar*this.z);
    }

    
    public Vector plus(Vector other){
        return new Vector(this.x + other.x,
                      this.y + other.y,
                      this.z + other.z);
    }

    public Vector minus(Vector other){
        return new Vector(this.x - other.x,
                      this.y - other.y,
                      this.z - other.z);
    }
    
    public String toString(){
        return String.format("%f %f %f", x, y, z);
    }
}