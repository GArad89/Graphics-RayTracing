package RayTrace;

class Ray{
    Light light=null;
    public Vector src, direct;
    public double src2, direct2, src_dot_direct;

    public Ray(Vector src, Vector direct, Light light){
        this.src = src;
        this.direct = direct.prod(1/direct.size());
        this.light = light;
        this.src2 = src.dot(src);
        this.direct2 = direct.dot(direct);
        this.src_dot_direct = src.dot(direct);
    }
    
    public Ray(Vector src, Vector direct){ //for tracing rays from the eye (backward tracing)
        this.src = src;
        this.direct = direct.prod(1/direct.size());
        this.src2 = src.dot(src);
        this.direct2 = direct.dot(direct);
        this.src_dot_direct = src.dot(direct);
    }
    
    public Vector getPos(double t){
        return src.plus(direct.prod(t));
    }
        
}