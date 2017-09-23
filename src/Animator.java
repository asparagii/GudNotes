import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class Animator implements ActionListener {
    Vector2 final_position;

    // returns the vector to be added to the object now moving
    Vector2 step(Vector2 current_position){

        // TODO Add easy-ease
        Vector2 distance = final_position.subtract(current_position);
        Vector2 speed = distance.getDirection();
        speed = speed.multiply(4);
        return speed;
    }

    Animator(Vector2 final_position){
        this.final_position = final_position;
    }

    public abstract void actionPerformed(ActionEvent e);
}
