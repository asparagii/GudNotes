package GUI;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;

public class Node extends JInternalFrame implements movable{
    private JTextPane text = new JTextPane();

    private JButton btn_1 = new JButton("");
    private JButton btn_2 = new JButton("");
    private JButton btn_3 = new JButton("");

    private JPanel pnlButtons = new JPanel();
    private JScrollPane textContainer;
    private Dimension size = new Dimension(100, 80);
    private Point position;

    Node(Point pos){
        super("Root", true, false, false, false);

        position = pos;
        setLocation(position);

        pnlButtons.setPreferredSize(new Dimension(30, 80));
        pnlButtons.setLayout(new BoxLayout(pnlButtons, BoxLayout.Y_AXIS));

        pnlButtons.add(btn_1);
        pnlButtons.add(btn_2);
        pnlButtons.add(btn_3);

        textContainer = new JScrollPane(text, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        textContainer.setPreferredSize(new Dimension(100, 80));

        add(textContainer, BorderLayout.CENTER);
        add(pnlButtons, BorderLayout.EAST);

        pack();
        setVisible(true);
    }

    @Override
    public void move(Point speed){
        position.translate(speed.x, speed.y);
    }
}
