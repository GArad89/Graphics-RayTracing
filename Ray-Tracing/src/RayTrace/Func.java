package RayTrace;
abstract class Func{

    public abstract double eval(Vector vector);
    
    public abstract Vector grad(Vector vector);
}