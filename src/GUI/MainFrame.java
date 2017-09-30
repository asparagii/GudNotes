package GUI;

import javax.swing.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        super("GudNotes");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1280, 720);

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

        setLocationRelativeTo(null);
        setLayout(null);

        Node xd = new Node(20, 20);
        add(xd);
    }
}
