package RayTrace;
class Vector{

    double x, y, z;

    public Vector(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector(Vector v1,Vector v2) { //v1,v2 are points in x,y,z coordinates
    	this.x = v2.x - v1.x;
    	this.y = v2.y - v1.y;
    	this.z = v2.z - v1.z;
    	//normalizing vector size
    	//double size = (double) Math.sqrt(this.x*this.x+this.y*this.y+this.z*this.z);
    	//this.x /=size;
    	//this.y /=size;
    	//this.z /=size;
    }
    public double dot(Vector other){
        return this.x*other.x + this.y*other.y + this.z*other.z;
    }
    
    public Vector cross(Vector other){
        return new Vector(this.y*other.z - this.z*other.y,
                      this.z*other.x - this.x*other.z,
                      this.x*other.y - this.y*other.x);
    }
    
    public Vector prod(double scalar){
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
    
    public Vector getPerp(){
    	if(this.x == 0) {
    		return new Vector (1,0,0);
    	}
    	if(this.y == 0) {
    		return new Vector(0,1,0);
    	}
    	if(this.z == 0) {
    		return new Vector(0,0,1);
    	}
    	double x2=1,y2,z2=0;
    	y2 = -(this.x/this.y);
    	return new Vector(x2,y2,z2);
    }
    
    public double size() {
    	return (double)Math.sqrt(this.dot(this));
    }
    
    public boolean equals(Vector other){
    	if((this.x == other.x)&&(this.y == other.y )&&(this.z == other.z)){
    		return true;
    	}
    	return false;
    }
    public String toString(){
        return String.format("%f %f %f", x, y, z);
    }
}