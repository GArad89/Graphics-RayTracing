package RayTrace;

abstract class Surface{
    Material mat;
    
    public abstract Vector normal(Vector pos);
    
    public abstract float intersect(Ray ray);
}