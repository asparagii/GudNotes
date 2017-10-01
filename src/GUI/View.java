package GUI;

import javax.swing.*;
import java.awt.*;

public class View extends JScrollPane implements movable {

    View(JDesktopPane content){
        super(content, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

    @Override
    public void move(Point speed) {

    }
}

