package RayTrace;

import java.lang.Math;


class Sqrt extends Func{
    Func func;
    Vector nan;

    public Sqrt(Func func){
        this.func = func;
        this.nan = new Vector(Double.NaN, Double.NaN, Double.NaN);
    }

    public double eval(Vector vector){
        return Math.sqrt(Math.abs(func.eval(vector)));
    }
    

    public Vector grad(Vector vector){
        double f=func.eval(vector);
        if(f > 0)
           return func.grad(vector).prod(0.5/Math.sqrt(f));
        if(f < 0)
           return func.grad(vector).prod(-0.5/Math.sqrt(-f));
        return nan;

    }
    
    public String toString(){
        return String.format("sqrt%s", func.toString());
    }
}