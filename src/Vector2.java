public class Vector2{
    private double x, y;

    public Vector2(double x, double y){
        this.x = x;
        this.y = y;
    }

    public void setPosition(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double x(){ return x;}
    public double y(){ return y;}

    public double getMagnitude(){
        return Math.sqrt(x*x + y*y);
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y){
        this.y = y;
    }

    public Vector2 getDirection(){
        double magn = getMagnitude();
        return new Vector2(x/magn, y/magn);
    }

    public Vector2 subtract(Vector2 other){
        return new Vector2(x - other.x, y - other.y);
    }

    public Vector2 add(Vector2 other){
        return new Vector2(x + other.x, y + other.y);
    }

    public Vector2 multiply(double k){
        return new Vector2(x * k, y * k);
    }
}
