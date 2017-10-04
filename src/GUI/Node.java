package GUI;

import org.w3c.dom.css.Rect;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

public class Node extends JPanel implements animatable{

    private String title;

    private JTextPane text = new JTextPane();

    private JButton btn_1 = new JButton("");
    private JButton btn_2 = new JButton("");
    private JButton btn_3 = new JButton("");

    private JPanel pnlButtons = new JPanel();
    private JScrollPane textContainer;

    private Dimension size = new Dimension(150, 100);
    private Point position;

    private int ID;

    private boolean draggable = false;

    Node(String name, int id){
        super();

        ID = id;
        title = name;

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(2, 5, 5, 5));

        position = new Point(0, 0);
        setLocation(position);

        JLabel lblTitle = new JLabel(title);

        add(lblTitle, BorderLayout.PAGE_START);

        pnlButtons.setPreferredSize(new Dimension(10, 80));
        pnlButtons.setLayout(new BoxLayout(pnlButtons, BoxLayout.Y_AXIS));
        pnlButtons.setOpaque(false);

        pnlButtons.add(btn_1);
        pnlButtons.add(btn_2);
        pnlButtons.add(btn_3);

        textContainer = new JScrollPane(text, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        add(textContainer, BorderLayout.CENTER);
        add(pnlButtons, BorderLayout.LINE_END);

        setBounds(position.x, position.y, size.width, size.height);
        setVisible(true);
    }

    int getID(){ return ID; }

    @Override
    public void move(Point speed){
        position.translate(speed.x, speed.y);
    }

    @Override
    protected void paintBorder(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        g.fillRoundRect(0, 0, size.width, size.height, 10, 10);
    }

    @Override
    public void setLocation(int x, int y) {
        position = new Point(x, y);
        setBounds(position.x, position.y, size.width, size.height);
    }

    @Override
    public boolean contains(Point p) {
        return getBounds().contains(p);
    }

    public void setDraggable(boolean b) {
        draggable = b;
    }

    public boolean isDraggable(){
        return draggable;
    }
}
