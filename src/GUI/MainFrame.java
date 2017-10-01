package GUI;

import javax.swing.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        super("GudNotes");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1280, 720);

        Viewport viewport = new Viewport();
        View view = new View(viewport);
        getContentPane().add(view);
    }
}
