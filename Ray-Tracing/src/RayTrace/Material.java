package RayTrace;

class Material{
     float diff_r, diff_g, diff_b;
     float spec_r, spec_g, spec_b;
     float reflect_r, reflect_g, reflect_b;
     float phong;
     float trans;
    
    public Material(float diff_r, float diff_g, float diff_b,
                    float spec_r, float spec_g, float spec_b,
                    float reflect_r, float reflect_g, float reflect_b,
                    float phong, float trans){
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