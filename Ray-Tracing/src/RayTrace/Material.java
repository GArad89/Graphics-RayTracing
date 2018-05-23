package RayTrace;

class Material{
     double diff_r, diff_g, diff_b;
     double spec_r, spec_g, spec_b;
     double reflect_r, reflect_g, reflect_b;
     double phong;
     double trans;
    
    public Material(double diff_r, double diff_g, double diff_b,
                    double spec_r, double spec_g, double spec_b,
                    double reflect_r, double reflect_g, double reflect_b,
                    double phong, double trans){
        this.diff_r = diff_r;
        this.diff_g = diff_g;
        this.diff_b = diff_b;
        this.spec_r = spec_r;
        this.spec_g = spec_g;
        this.spec_b = spec_b;
        this.reflect_r = reflect_r;
        this.reflect_g = reflect_g;
        this.reflect_b = reflect_b;
        this.phong = phong;
        this.trans = trans;        
    }
}