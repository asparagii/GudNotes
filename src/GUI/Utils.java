package GUI;

import java.awt.Point;

interface animatable{
    void move(Point speed);
}


class Vector2 extends Point.Double {

    Vector2(double x, double y){
        super(x, y);
    }

    Vector2(Point p){
        super(p.x, p.y);
    }

    public Vector2 add(Point b){
        return new Vector2(this.x + b.x, this.y + b.y);
    }

    public Vector2 add(Vector2 b){
        return new Vector2(this.x + b.x, this.y + b.y);
    }

    public Vector2 multiply(double k){
        return new Vector2(this.x * k, this.y * k);
    }

    public int intX(){
        return (int) x;
    }

    public int intY(){
        return (int) y;
    }
}
