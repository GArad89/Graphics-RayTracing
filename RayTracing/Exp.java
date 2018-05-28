import java.lang.Math;

class Exp{
    public static void main(String[] args){
        Light light = new Light(0, 0 ,0, 0, 0, 0, 0, 0, 0);
        Material mat = new Material(0, 0 ,0, 0, 0, 0, 0, 0, 0, 0, 0);;
        Ray ray = new Ray(new Vector(0, 0, 0), new Vector(1, 1, 1), light);
        Triangle tr = new Triangle(1, 0, 0, 0, 1, 0, 0, 0, 1, mat);
        Plane p = new Plane(1, 1, 1, 1, mat);
        Sphere s = new Sphere(2, 2, 2, 1, mat);
        System.out.println(ray.getPos(s.intersect(ray)));
    }
}