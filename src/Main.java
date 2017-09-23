import javax.swing.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args){
        View view = new View();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                GUI(view);
            }
        });
    }

    private static void GUI(View view){
        JFrame frame = new JFrame("Appunti");
        frame.setSize(1280, 720);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.add(view);
        frame.setVisible(true);
    }
}

class View extends JPanel implements MouseWheelListener{
    private ArrayList<GraphicNode> nodes;
    private Camera camera;
    private int cursor;

    View(){
        nodes = new ArrayList<>(1);
        GraphicNode root = new GraphicNode("Root",80, 100);
        root.setPosition(0, 0);
        nodes.add(root);

        cursor = 0;

        camera = new Camera(0, 0, 1);
        camera.setPosition(camera.lookAt(root));


        // Zoom with ctrl + mouseWheel
        addMouseWheelListener(this);

        // --- Set input map ---
        //
        // ctrl + n => append node and move cursor
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK), "appendNodeAndMove");
        // ctrl + shift + n => append node
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK), "appendNode");

        getActionMap().put("appendNodeAndMove", appendNodeAndMove);
        getActionMap().put("appendNode", appendNode);
    }


    // Actions for binding
    private Action appendNodeAndMove = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            appendNode("new", true);
            repaint();
        }
    };

    private Action appendNode = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            appendNode("new", false);
            repaint();
        }
    };

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if(e.isControlDown()){
            if(e.getWheelRotation() < 0) {
                camera.addZoom(0.1);
            } else {
                camera.addZoom(-0.1);
            }
            // recalculate offset
            camera.lookAt(selectedNode());
            repaint();
        }
    }


    // Necessary to have focus for input events
    public void addNotify(){
        super.addNotify();
        requestFocus();
    }

    // Appends a node to the current selected node (indexed by cursor)
    // title: label of the next node
    // move: if true, new node will be selected
    public void appendNode(String title, boolean move){
        GraphicNode new_entry = new GraphicNode(title, 80, 100);

        new_entry.addParent(nodes.get(cursor));
        nodes.get(cursor).addChild(new_entry);

        new_entry.setAutoPosition(20);

        nodes.add(new_entry);


        // If 'move' is true, move the cursor to the new node
        if(move) {
            moveCameraSmooth(new_entry);
            cursor = nodes.indexOf(new_entry);
            repaint();
        }
    }

    private void moveCameraSmooth(GraphicNode n) {

        // TODO Prevent simultaneous Animator to start
        // When multiple nodes are created rapidly, animations are running simultaneously

        Timer timer = new Timer(16, new Animator(camera.lookAt(n)) {
            @Override
            public void actionPerformed(ActionEvent e) {
                camera.move(step(camera.getPosition()));
                repaint();

                // When final_position is reached (with 1px error) stop moving
                if(camera.getPosition().subtract(this.final_position).getMagnitude() < 1){
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        timer.start();

    }

    GraphicNode selectedNode(){
        return nodes.get(cursor);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.scale(camera.getZoom(), camera.getZoom());

        for(GraphicNode e : nodes){
            // if e is selected, highlight
            e.paintNode(camera, g2, selectedNode() == e);
        }
    }

}
