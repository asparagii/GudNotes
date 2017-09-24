import javax.swing.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.*;
import java.util.ArrayList;

public class View extends JPanel implements MouseWheelListener, KeyListener{
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
    }


    // Actions for binding
    Action appendNodeAndMove = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            appendNode("new", true);
            repaint();
        }
    };

    Action appendNode = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            appendNode("new", false);
            repaint();
        }
    };

    Action changeCursorLeft = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            cursorToParent();
        }
    };

    Action changeCursorRight = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            cursorToChild();
        }
    };

     Action changeCursorUp = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            navigateBrother(-1);
        }
    };

    Action changeCursorDown = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            navigateBrother(1);
        }
    };

    Action addBrother = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            addBrother("new", true);
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

            repaint();
        }
    }

    private void addBrother(String title, boolean move){
        GraphicNode parent;
        try {
            parent = (GraphicNode) selectedNode().getParents().get(0);
        } catch(IndexOutOfBoundsException e){
            return;
        }

        GraphicNode new_entry = new GraphicNode(title, 80, 100);
        parent.addChild(new_entry);
        new_entry.addParent(parent);
        nodes.add(new_entry);
        new_entry.setAutoPosition(20);

        if(move){
            moveCameraSmooth(new_entry);
            cursor = nodes.indexOf(new_entry);
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
    private void appendNode(String title, boolean move){
        GraphicNode new_entry = new GraphicNode(title, 80, 100);

        new_entry.addParent(nodes.get(cursor));
        selectedNode().addChild(new_entry);

        new_entry.setAutoPosition(20);

        nodes.add(new_entry);

        // If 'move' is true, move the cursor to the new node
        if(move) {
            moveCameraSmooth(new_entry);
            cursor = nodes.indexOf(new_entry);
        }
    }

    // Moves cursor to the (first) parent of the current node
    private void cursorToParent(){
        try {
            cursor = nodes.indexOf((GraphicNode) selectedNode().getParents().get(0));
        } catch(IndexOutOfBoundsException e){
            return;
        }
        moveCameraSmooth(selectedNode());
    }

    // Moves cursor to the first child of the current node
    private void cursorToChild(){
        try {
            cursor = nodes.indexOf((GraphicNode) selectedNode().getChildren().get(0));
        } catch(IndexOutOfBoundsException e){
            return;
        }
        moveCameraSmooth(selectedNode());
    }

    private void navigateBrother(int offset){

        GraphicNode parent;
        try {
            parent = (GraphicNode) selectedNode().getParents().get(0);
        } catch (IndexOutOfBoundsException e){
            return;
        }
        // index of the current node in the parent's children array
        int index_of_current = parent.getChildren().indexOf(selectedNode());

        // if this is NOT the first child and we are NOT moving up
        if(index_of_current + offset > -1){
            try {
                cursor = nodes.indexOf((GraphicNode) parent.getChildren().get(index_of_current + offset));
            } catch(IndexOutOfBoundsException e){
                return;
            }
        }
        moveCameraSmooth(selectedNode());
    }

    // Move camera smoothly using moving law defined in Animator
    private Timer animation_timer = null;
    private void moveCameraSmooth(GraphicNode n) {
        // If there is already a timer running, then stop timer
        if(animation_timer != null && animation_timer.isRunning())
            animation_timer.stop();

        // Create a new timer with Animator ActionListener
        animation_timer = new Timer(16, new Animator(camera.getPosition(), camera.lookAt(n)) {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Move using step function in Animator
                camera.move(step(camera.getPosition()));
                repaint();

                // When final_position is reached (with 5px error) stop moving
                if(camera.getPosition().subtract(this.final_position).getMagnitude() < 5){
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        animation_timer.start();
    }

    private GraphicNode selectedNode(){
        return nodes.get(cursor);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // Antialiasing
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHints(rh);

        g2.scale(camera.getZoom(), camera.getZoom());

        for(GraphicNode e : nodes){
            // if paintNode returns true, repaint() needed
            // if e is selected, highlight
            if(e.paintNode(camera, g2, selectedNode() == e)){
                repaint();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        selectedNode().appendChar(e.getKeyChar());
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(!e.isAltDown()) {
            int keyCode = e.getKeyCode();
            switch (keyCode) {
                case KeyEvent.VK_RIGHT:
                    selectedNode().moveCursor(NodeContent.RIGHT);
                    repaint();
                    break;
                case KeyEvent.VK_LEFT:
                    selectedNode().moveCursor(NodeContent.LEFT);
                    repaint();
                    break;
                case KeyEvent.VK_END:
                    selectedNode().moveCursor(NodeContent.END);
                    repaint();
                    break;
                case KeyEvent.VK_HOME:
                    selectedNode().moveCursor(NodeContent.START);
                    repaint();
                    break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
