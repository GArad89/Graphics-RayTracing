package RayTrace;

class Ellipse extends Generic{
    
    Ellipse(double x, double y, double z, double a, double b, double c, Material mat){
        super(null, mat);
        Func vx = new Minus(new Var('x'), new Scalar(x)), 
            vy = new Minus(new Var('y'), new Scalar(y)),
            vz = new Minus(new Var('z'), new Scalar(z));
        this.func = new Minus(new Plus(new Prod(new Power(vz, 2), new Scalar(c)),
                                       new Plus(new Prod(new Power(vy, 2), new Scalar(b)),
                                                new Prod(new Power(vx, 2), new Scalar(a)))),
                              new Scalar(1));
    }

}