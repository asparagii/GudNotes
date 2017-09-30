package GUI;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

public class Node extends JPanel{
    private JTextPane text = new JTextPane();
    private Rectangle box = new Rectangle();

    private JButton btn_1 = new JButton("");
    private JButton btn_2 = new JButton("");
    private JButton btn_3 = new JButton("");

    private JPanel pnlButtons = new JPanel();
    private JScrollPane textContainer;
    private Dimension size;
    private Point position;

    Node(int x, int y){
        super(new BorderLayout());

        size = new Dimension(200, 100);
        position = new Point(x, y);

        updateBox();
        setBounds(box);
        pnlButtons.setPreferredSize(new Dimension(30, 30));
        pnlButtons.setLayout(new BoxLayout(pnlButtons, BoxLayout.Y_AXIS));

        pnlButtons.add(btn_1);
        pnlButtons.add(btn_2);
        pnlButtons.add(btn_3);

        text.setText("");

//        // Set title border
//        Border etched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
//        TitledBorder title = BorderFactory.createTitledBorder(
//                etched, "Root");
//        title.setTitleJustification(TitledBorder.LEFT);
//        setBorder(title);

        textContainer = new JScrollPane(text, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        add(textContainer, BorderLayout.CENTER);
        add(pnlButtons, BorderLayout.EAST);

    }

    @Override
    public void setSize(Dimension new_size){
        size.setSize(new_size);
        updateBox();
    }

    private void updateBox(){
        box.setLocation(position);
        box.setSize(size);
    }
}
