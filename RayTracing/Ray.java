class Ray{
    Light light;
    public Vector src, direct;
    public float src2, direct2, src_dot_direct;

    public Ray(Vector src, Vector direct, Light light){
        this.src = src;
        this.direct = direct;
        this.light = light;
        this.src2 = src.dot(src);
        this.direct2 = direct.dot(direct);
        this.src_dot_direct = src.dot(direct);
    }
    
    public Vector getPos(float t){
        return src.plus(direct.prod(t));
    }
        
}