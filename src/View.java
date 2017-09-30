import javax.swing.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class View extends JPanel implements MouseWheelListener, KeyListener, MouseListener, MouseMotionListener{
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
        nodes.get(cursor).addChild(new_entry);

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

    public Callable paint_callback = () -> {
        repaint();
        return null;
    };

    // Move camera smoothly using moving law defined in Animator
    private Timer animation_timer = null;
    private void moveCameraSmooth(GraphicNode n) {
        camera.position.animate(camera.lookAt(n), 10, true, paint_callback);
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
            // if e is selected, highlight
            e.paintNode(camera, g2, selectedNode() == e, paint_callback);
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

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    Vector2 mousePositionTransform(MouseEvent e){
        Vector2 pos = new Vector2(e.getX(), e.getY());
        return pos.add(camera.getPosition());
    }

    private GraphicNode moving;
    private Vector2 previous_node_position;
    private Vector2 previous_mouse_position;

    @Override
    public void mousePressed(MouseEvent e) {

        Vector2 mouse_relative_position = mousePositionTransform(e);

        // If mouse Left + ctrl
        if((e.getModifiersEx() & (InputEvent.BUTTON1_DOWN_MASK | InputEvent.CTRL_DOWN_MASK)) == (InputEvent.BUTTON1_DOWN_MASK | InputEvent.CTRL_DOWN_MASK)){
            // If mouse is clicking a node
            for(GraphicNode i : nodes){
                if(i.getDragBox().contains(mouse_relative_position.x(), mouse_relative_position.y())){
                    // Store previous position and start moving
                    previous_node_position = i.getPosition();
                    moving = i;
                    return;
                }
            }
        }

        // If mouse Right is clicked while moving node
        if((e.getModifiersEx() & InputEvent.BUTTON2_DOWN_MASK) == InputEvent.BUTTON2_DOWN_MASK){
            // reset
            if(moving != null){
                moving.setPosition((int) previous_node_position.x(),  (int) previous_node_position.y());
                moving = null;
            }


        }
    }




    @Override
    public void mouseReleased(MouseEvent e) {
        moving = null;
        previous_mouse_position = null;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Vector2 mouse_position = new Vector2(e.getX(), e.getY());


        if(previous_mouse_position != null) {
            if (moving != null) {
                Vector2 delta = mouse_position.subtract(previous_mouse_position);
                Vector2 new_node_position = moving.getPosition().add(delta);
                moving.setPosition((int) new_node_position.x(), (int) new_node_position.y());
            }
        }
        previous_mouse_position = mouse_position;
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
