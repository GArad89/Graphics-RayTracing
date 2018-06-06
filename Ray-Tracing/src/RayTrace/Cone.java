package RayTrace;

class Cone extends Generic{
    
    Cone(double x, double y, double z, double slope, Material mat){
        super(null, mat);
        Func vx = new Minus(new Var('x'), new Scalar(x)), 
            vy = new Minus(new Var('y'), new Scalar(y)),
            vz = new Minus(new Var('z'), new Scalar(z));
        this.func = new Minus(new Plus(new Power(vx, 2),
                                       new Power(vz, 2)),
                              new Prod(new Power(vy, 2), new Scalar(slope)));
    }

}