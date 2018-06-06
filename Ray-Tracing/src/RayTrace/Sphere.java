package RayTrace;

class Sphere extends Surface{
    Vector center;
    double radius, radius2, center2;
    double min_x,max_x,min_y,max_y,min_z,max_z;
    
    Sphere(double x, double y, double z, double radius, Material mat){
        this.center = new Vector(x, y, z);
        this.radius = radius;
        this.radius2 = radius*radius;
        this.center2 = center.dot(center);
        this.mat = mat;
        min_x = center.x - radius;
        min_y = center.y - radius;
        min_z = center.z - radius;
        max_x = center.x + radius;
        max_y = center.y + radius;
        max_z = center.z + radius;
        
    }
    
    public Vector normal(Vector pos){
        Vector n = pos.minus(center);
        return n;
    }
    
    
    public double intersect(Ray ray){
        double a, b;
        
        //checking if the ray is even moving to the area of the sphere on the x dimension
        a = ray.src.x;
        b = ray.direct.x;
        if( b > 0) {  //the ray is going to the positive direction of x
        	if( a > max_x) //the ray is moving away from the sphere on the x axis
        		return Double.POSITIVE_INFINITY;
        }
        if( b < 0) {  //the ray is going to the negative direction of x
        	if( a < min_x) //the ray is moving away from the sphere on the x axis
        		return Double.POSITIVE_INFINITY;
        }
        //same thing but with y
        a = ray.src.y;
        b = ray.direct.y;
        if( b > 0) {  
        	if( a > max_y) 
        		return Double.POSITIVE_INFINITY;
        }
        if( b < 0) {  
        	if( a < min_y) 
        		return Double.POSITIVE_INFINITY;
        }
        //same thing but with z
        a = ray.src.z;
        b = ray.direct.z;
        if( b > 0) {  
        	if( a > max_z) 
        		return Double.POSITIVE_INFINITY;
        }
        if( b < 0) {  
        	if( a < min_z) 
        		return Double.POSITIVE_INFINITY;
        }
        
        Vector perp; //perpendicular
        Vector tocenter = new Vector(ray.src,this.center); //from the ray source to the sphere's center

        a = ray.direct.dot(tocenter);

        if(a==0){ //unlikely to happen but just making sure
        	if(tocenter.size() < radius){

        		return 0;
        	}
        }
        perp = new Vector(this.center,ray.direct.prod(a).plus(ray.src));


        if(perp.size() > this.radius){ //ray is intersecting with the perpendicular outside of the sphere
        	return Double.POSITIVE_INFINITY;
        }
        
        b = Math.sqrt(this.radius2 - perp.size()*perp.size());


        if(a-b < 0.000001){
        	return Double.POSITIVE_INFINITY;
        }
        return a-b;


    }

}