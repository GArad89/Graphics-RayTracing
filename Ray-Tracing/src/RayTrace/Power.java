package RayTrace;

import java.lang.Math;


class Power extends Func{
    Func func;
    int power, dec_power;

    public Power(Func func, int power){
        this.func = func;
        this.power = power;
        this.dec_power = power-1;
    }

    public double eval(Vector vector){
        return Math.pow(func.eval(vector), power);
    }
    

    public Vector grad(Vector vector){
        return func.grad(vector).prod(Math.pow(func.eval(vector), dec_power)).prod(power);
    }
    
    public String toString(){
        return String.format("(%s^%d)", func.toString(), power);
    }
}