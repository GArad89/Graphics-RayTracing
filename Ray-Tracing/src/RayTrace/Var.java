package RayTrace;

class Var extends Func{
    char var;
    Vector g;
    
    public Var(char var){
        this.var = var;
        if (var == 'x')
           this.g = new Vector(1, 0, 0);
        if (var == 'y')
           this.g = new Vector(0, 1, 0);
        if (var == 'z')
           this.g = new Vector(0, 0, 1);
    }

    public double eval(Vector vector){
        if(var == 'x')
           return vector.x;
        if(var == 'y')
           return vector.y;
        if(var == 'z')
           return vector.z;
        return Double.NaN;
    }
    
    public Vector grad(Vector vector){
        return g;
    }
    
    public String toString(){
        return String.format("%c", var);
    }

}