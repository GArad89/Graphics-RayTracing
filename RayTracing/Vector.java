class Vector{

    float x, y, z;

    public Vector(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
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