package GUI;

import javax.swing.*;
import java.awt.*;

class Viewport extends JDesktopPane {

    private Dimension size = new Dimension(1920, 1080);

    Viewport(){
        super();
        setPreferredSize(size);

        Node a = new Node(new Point(30, 30));
        add(a);
    }
}
