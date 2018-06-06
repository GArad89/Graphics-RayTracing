package RayTrace;

class Cylinder extends Generic{
    
    Cylinder(double x, double z, double radius, Material mat){
        super(null, mat);
        this.func = new Minus(new Plus(new Power(new Minus(new Var('x'), new Scalar(x)), 2),
                                       new Power(new Minus(new Var('z'), new Scalar(z)), 2)),
                              new Scalar(radius*radius));
    }

}