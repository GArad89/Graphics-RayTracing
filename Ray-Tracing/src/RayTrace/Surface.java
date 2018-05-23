package RayTrace;

abstract class Surface{
    Material mat;
    
    public abstract Vector normal(Vector pos);
    
    public abstract double intersect(Ray ray);
}