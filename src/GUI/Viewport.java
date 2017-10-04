package GUI;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.CubicCurve2D;
import java.util.ArrayList;
import java.util.Hashtable;

class Viewport extends JPanel implements MouseMotionListener, MouseListener{

    private Dimension size = new Dimension(1920, 1080);
    private Hashtable<Integer, Node> nodes = new Hashtable<>(10);
    private ArrayList<Integer> keys = new ArrayList<>();

    private Hashtable<Integer, Integer> edges = new Hashtable<>(10);

    Viewport(){
        super();

        setLayout(null);
        setPreferredSize(size);

        addMouseListener(this);
        addMouseMotionListener(this);

        addNode("Root", 0);
        addNode("Node 0", 1);

        addEdge(0, 1);

        for (Integer i : keys) {
            add(nodes.get(i));
        }
    }

    public void addNode(String title, int id){
        Node to_add = new Node(title, id);
        to_add.setLocation(newNodeLocation());
        nodes.put(id, to_add);
        keys.add(id);
    }

    public void addEdge(int parent_id, int child_id){
        edges.put(parent_id, child_id);
    }

    private Point newNodeLocation(){
        return new Point(30, 30);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // TODO Edges
        for (int i : keys) {
            Integer child_id = edges.get(i);
            if(child_id != null)
                paintEdge(i, child_id, g);
        }

    }

    // Drag panels
    private int dX, dY;

    @Override
    public void mousePressed(MouseEvent e) {
        for (Integer i : keys) {
            Node panel = nodes.get(i);
            if(panel.contains(e.getPoint())){
                dX = e.getLocationOnScreen().x - panel.getX();
                dY = e.getLocationOnScreen().y - panel.getY();
                panel.setDraggable(true);
                break;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (Integer i : keys) {
            Node panel = nodes.get(i);
            panel.setDraggable(false);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        for (Integer i : keys) {
            Node panel = nodes.get(i);
            if (panel.isDraggable()) {
                repaint();
                panel.setLocation(e.getLocationOnScreen().x - dX, e.getLocationOnScreen().y - dY);
                dX = e.getLocationOnScreen().x - panel.getX();
                dY = e.getLocationOnScreen().y - panel.getY();
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    // TODO Edge
    // TODO     Draggable on points

    private void paintEdge(int parent_id, int child_id, Graphics g){
        Node parent = nodes.get(parent_id);
        Node child = nodes.get(child_id);

        Vector2 parent_position = new Vector2(parent.getLocation());
        parent_position = parent_position.add(new Vector2(parent.getSize().getWidth(), 0));
        Vector2 child_position = new Vector2(child.getLocation());

        Vector2 control = parent_position.add(child_position).multiply(0.5);

        CubicCurve2D.Double edge = new CubicCurve2D.Double(parent_position.getX(), parent_position.getY(),
                control.getX(), parent_position.getY(),
                control.getX(), child_position.getY(),
                child_position.getX(), child_position.getY());

        Graphics2D g2 = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHints(rh);
        g2.draw(edge);
    }

}