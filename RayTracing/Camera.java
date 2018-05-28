
class Camera{
    
    private float pos_x, pos_y, pos_z;
    private float look_x, look_y, look_z;
    private float up_x, up_y, up_z;
    private float screen_dist, screen_width;
    
    public Camera(float pos_x, float pos_y, float pos_z,
                  float look_x, float look_y, float look_z,
                  float up_x, float up_y, float up_z,
                  float screen_dist, float screen_width){
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        this.pos_z = pos_z;
        this.look_x = look_x;
        this.look_y = look_y;
        this.look_z = look_z;
        this.up_x = up_x;
        this.up_y = up_y;
        this.up_z = up_z;
        this.screen_dist = screen_dist;
        this.screen_width = screen_width;
    }
}