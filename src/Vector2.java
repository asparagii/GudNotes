import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.util.concurrent.Callable;
import java.util.stream.StreamSupport;

class Vector2{
    private double x, y;

    Timer animation_timer;

    Vector2(double x, double y){
        this.x = x;
        this.y = y;
    }

    void setPosition(double x, double y){
        this.x = x;
        this.y = y;
    }

    // Starts animation of vector, ending in 'end_position', with 'duration' in ms.
    // If override is true, if the vector was animating, the animation will reset,
    // If override is false, the new animation will not be accepted.
    void animate(Vector2 end_position, int duration, boolean override, Callable everyFrame){
        // if there is already an animation running and !override
        if(animation_timer != null){
            if(animation_timer.isRunning()){
                if(!override) {
                    // Don't do anything
                    return;
                } else {
                    animation_timer.stop();
                }
            }
        }

        animation_timer = new Timer(16, new Animator(this, end_position, 1.0 / duration) {
            @Override
            public void actionPerformed(ActionEvent e) {
                Vector2 next_position = add(step(new Vector2(x, y)));
                setPosition(next_position.x, next_position.y);
                try {
                    everyFrame.call();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                // Stopping condition
                if(end_position.subtract(new Vector2(x, y)).getMagnitude() < initialDistance * speed * 0.2){
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        animation_timer.start();
    }

    double x(){ return x;}
    double y(){ return y;}

    double getMagnitude(){
        return Math.sqrt(x*x + y*y);
    }

    void setX(double x) {
        this.x = x;
    }

    void setY(double y){
        this.y = y;
    }

    Vector2 getDirection(){
        double magnitude = getMagnitude();
        return new Vector2(x/magnitude, y/magnitude);
    }

    Vector2 subtract(Vector2 other){
        return new Vector2(x - other.x, y - other.y);
    }

    Vector2 add(Vector2 other){
        return new Vector2(x + other.x, y + other.y);
    }

    Vector2 multiply(double k){
        return new Vector2(x * k, y * k);
    }
}
