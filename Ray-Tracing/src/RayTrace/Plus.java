package RayTrace;

class Plus extends Func{
    Func func1, func2;

    public Plus(Func func1, Func func2){
        this.func1 = func1;
        this.func2 = func2;
    }

    public double eval(Vector vector){
        return func1.eval(vector) + func2.eval(vector);
    }
    

    public Vector grad(Vector vector){
        return func1.grad(vector).plus(func2.grad(vector));
    }
    
    public String toString(){
        return String.format("(%s + %s)", func1.toString(), func2.toString());
    }
}