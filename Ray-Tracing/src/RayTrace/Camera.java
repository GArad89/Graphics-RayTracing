package RayTrace;

class Camera{
    
    Vector pos; //float pos_x, pos_y, pos_z;
    Vector look; //float look_x, look_y, look_z;
    Vector look_v;
    Vector up; //float up_x, up_y, up_z;
    float screen_dist, screen_width;
    
    public Camera(float pos_x, float pos_y, float pos_z,
                  float look_x, float look_y, float look_z,
                  float up_x, float up_y, float up_z,
                  float screen_dist, float screen_width){
    	pos = new Vector(pos_x,pos_y,pos_z);
    	look = new Vector(look_x,look_y,look_z);
    	up = new Vector(up_x,up_y,up_z);
    	look_v = new Vector(pos,look);
    	up = up.minus(look_v.prod(up.dot(look_v)/(look_v.dot(look_v))));  //remove the component of up in the direction of look_v
//        this.pos_x = pos_x;
//        this.pos_y = pos_y;
//        this.pos_z = pos_z;
//        this.look_x = look_x;
//        this.look_y = look_y;
//        this.look_z = look_z;
//        this.up_x = up_x;
//        this.up_y = up_y;
//        this.up_z = up_z;
        this.screen_dist = screen_dist;
        this.screen_width = screen_width;
    }
}