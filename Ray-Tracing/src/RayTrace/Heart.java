package RayTrace;

class Heart extends Generic{
    
    Heart(double x, double y, double z, double radius, Material mat){
        super(null, mat);
        Func vx = new Minus(new Var('x'), new Scalar(x)), 
            vy = new Minus(new Var('y'), new Scalar(y)),
            vz = new Minus(new Var('z'), new Scalar(z));
        this.func = new Minus(new Plus(new Power(vz, 2),
                                       new Plus(new Power(vx, 2),
                                                new Power(new Minus(vy, new Sqrt(vx)), 2))),
                              new Scalar(radius*radius));
    }

}