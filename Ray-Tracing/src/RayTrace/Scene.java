
class Scene{
    
    private int bg_r, bg_g, bg_b;
    private int rays_num, max_recurs, sample_lev;
    private Camera cam;
    
    public Scene(int bg_r, int bg_g, int bg_b, int rays_num, int max_recurs, int sample_lev){
        this.bg_r = bg_r;
        this.bg_g = bg_g;
        this.bg_b = bg_b;
        this.rays_num = rays_num;
        this.max_recurs = max_recurs;
        this.sample_lev = sample_lev;
    }
    
    public void setCam(Camera cam){
        this.cam = cam;
    }
}