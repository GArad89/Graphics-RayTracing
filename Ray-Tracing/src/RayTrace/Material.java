class Material{
    private int diff_r, diff_g, diff_b;
    private int spec_r, spec_g, spec_b;
    private int reflect_r, reflect_g, reflect_b;
    private float phong;
    private float trans;
    
    public Material(int diff_r, int diff_g, int diff_b,
                    int spec_r, int spec_g, int spec_b,
                    int reflect_r, int reflect_g, int reflect_b,
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