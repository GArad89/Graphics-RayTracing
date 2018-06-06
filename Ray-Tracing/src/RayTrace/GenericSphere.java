package RayTrace;

class GenericSphere extends Generic{
    
    GenericSphere(double x, double y, double z, double radius, Material mat){
        super(null, mat);
        this.func = new Minus(new Plus(new Power(new Minus(new Var('x'), new Scalar(x)), 2),
                              new Plus(new Power(new Minus(new Var('y'), new Scalar(y)), 2),
                                       new Power(new Minus(new Var('z'), new Scalar(z)), 2))),
                              new Scalar(radius*radius));
    }

}