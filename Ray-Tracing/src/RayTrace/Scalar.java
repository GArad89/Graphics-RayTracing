package RayTrace;

class Scalar extends Func{
    double value;
    Vector g;
    
    public Scalar(double value){
        this.value = value;
        this.g = new Vector(0, 0, 0);
    }

    public double eval(Vector vector){
        return value;
    }
    
    public Vector grad(Vector vector){
        return g;
    }
    
    public String toString(){
        return String.format("%f", value);
    }

}