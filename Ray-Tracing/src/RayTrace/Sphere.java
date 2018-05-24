package RayTrace;

class Sphere extends Surface{
    Vector center;
    double radius, radius2, center2;
    
    Sphere(double x, double y, double z, double radius, Material mat){
        this.center = new Vector(x, y, z);
        this.radius = radius;
        this.radius2 = radius*radius;
        this.center2 = center.dot(center);
        this.mat = mat;
    }
    
    public Vector normal(Vector pos){
        Vector n = pos.minus(center);
        return n;
    }
    
    
    public double intersect(Ray ray){
        double a, b, c, delta;
        
        Vector perp;
        Vector tocenter = new Vector(ray.src,this.center);

        a = ray.direct.dot(tocenter);
        //if(ray.light != null){
        	
        //}
        if(a==0){ //unlikely to happen but just making sure
        	if(tocenter.size() < radius){

        		return 0;
        	}
        }
        perp = new Vector(this.center,ray.direct.prod(a).plus(ray.src));
//        if(perp.size()<3){
//        	System.out.println(perp.size());
//        }
        if(perp.size() > this.radius){
        	return Double.POSITIVE_INFINITY;
        }
        
        b = Math.sqrt(this.radius2 - perp.size()*perp.size());
      //  System.out.println(b);
        if(a-b < 0.000001){
        	return Double.POSITIVE_INFINITY;
        }
        return a-b;
//        a = ray.direct2;
//        b = 2*(ray.src_dot_direct - center.dot(ray.direct));
//        c = ray.src2 - 2*center.dot(ray.src) + center2 - radius2;
//       // System.out.println(new Vector(a, b, c));
//        if(b >= 0)
//            if(c > 0)
//                return Double.POSITIVE_INFINITY;
//            else { 
//                delta = b*b - 4*a*c;
//                if (delta >= 0)
//                    return (-b+(double)Math.sqrt(delta))/(2*a);
//                else
//                    return Double.POSITIVE_INFINITY;
//            }
//        delta = b*b - 4*a*c;
//        if(delta < 0)
//            return Double.POSITIVE_INFINITY;
//        return (-b-(double)Math.sqrt(delta))/(2*a);    
    }

}