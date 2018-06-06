package RayTrace;

import java.lang.Math;

class Generic extends Surface{
    
    Func func;
    double eps;
    
    public Generic(Func func, Material mat){
        this.func = func;
        this.eps = 0.000001;
        this.mat = mat;
    }
        
    public Vector normal(Vector pos){
        return func.grad(pos);
    }
    
    private double derive(Vector direct, Vector pos){
        //f'(a+bt)=grad f(a+bt)b
        return func.grad(pos).dot(direct);
    }
    
    public double intersect(Ray ray){
        double t=0;
        Vector pos = ray.getPos(t);
        double ft = func.eval(pos);
        
        for(int i=0; i<5 && Math.abs(ft) > eps;i++){
            t -= ft/derive(ray.direct, pos);
            pos = ray.getPos(t);
            ft = func.eval(pos);
        }
        if(t < 0)
            return -1;
        if(Math.abs(ft) < eps)
            return t;
        for(int i=0; i<5 && Math.abs(ft) > eps;i++)
        {
            t -= ft/derive(ray.direct, pos);
            pos = ray.getPos(t);
            ft = func.eval(pos);
        }
        if(Math.abs(ft) < eps)
            return t;
        return -1;
    }

}