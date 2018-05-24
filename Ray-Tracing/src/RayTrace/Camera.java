package RayTrace;

class Camera{
    
    Vector pos; //double pos_x, pos_y, pos_z;
    Vector look; //double look_x, look_y, look_z;
    Vector look_v;
    Vector up; //double up_x, up_y, up_z;
    double screen_dist, screen_width;
    
    public Camera(double pos_x, double pos_y, double pos_z,
                  double look_x, double look_y, double look_z,
                  double up_x, double up_y, double up_z,
                  double screen_dist, double screen_width){
    	pos = new Vector(pos_x,pos_y,pos_z);
    	look = new Vector(look_x,look_y,look_z);
    	up = new Vector(up_x,up_y,up_z);
    	look_v = new Vector(pos,look);
    	look_v = look_v.prod(1/look_v.size()); //normalize look_v
    	up = up.minus(look_v.prod(up.dot(look_v)/(look_v.dot(look_v))));  //remove the component of up in the direction of look_v
    	up = up.prod(1/up.size()); //normalize up
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