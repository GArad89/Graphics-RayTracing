package RayTrace;

import java.lang.Math;


class Sin extends Func{
    Func func;
    Vector nan;

    public Sin(Func func){
        this.func = func;
    }

    public double eval(Vector vector){
        return Math.sin(func.eval(vector));
    }
    

    public Vector grad(Vector vector){
        return func.grad(vector).prod(Math.cos(func.eval(vector)));

    }
    
    public String toString(){
        return String.format("sqrt%s", func.toString());
    }
}