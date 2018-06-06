package RayTrace;

class Wave extends Generic{
    
    Wave(double x, double y, double z, double radius, double amp, double freq, Material mat){
        super(null, mat);
        Func vx = new Minus(new Var('x'), new Scalar(x)), 
             vy = new Minus(new Var('y'), new Scalar(y)),
             vz = new Minus(new Var('z'), new Scalar(z));
        
        this.func = new Minus(new Plus(new Power(vz, 2),
                                       new Power(new Minus(vy, new Prod(new Sin(new Prod(vx, new Scalar(freq))), new Scalar(amp))), 2)),
                              new Scalar(radius*radius));
    }

}