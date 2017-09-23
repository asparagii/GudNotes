import java.awt.*;
import java.awt.geom.CubicCurve2D;

public class GraphicNode extends Node{

    private String title;
    private Vector2 size;
    private Vector2 position;

    public GraphicNode(String title, int height, int width){
        super();
        this.title = title;
        position = new Vector2(0, 0);
        size = new Vector2(width, height);
    }


    // Set position near the edge of the (only) parent
    // To be called only when node has one parent
    public void setAutoPosition(int minSpace){
        try {
            GraphicNode father = (GraphicNode) getParents().get(0);
            position.setX(father.getPosition().x() + father.getSize().x() + minSpace);

            int sum = 0;
            for (Object i : father.getChildren()) {
                GraphicNode e = (GraphicNode) i;
                sum += e.getSize().y() + minSpace;
            }
            position.setY(father.getPosition().y() + sum - getSize().y() - minSpace);

        } catch (ArrayIndexOutOfBoundsException e){
            position.setPosition(0, 0);
        }
    }

    public void setPosition(int x, int y){
        position.setPosition(x, y);
    }

    public Vector2 getPosition(){
        return position;
    }

    public Vector2 getSize(){
        return size;
    }

    private Vector2 relativePosition(Camera camera){
        return position.subtract(camera.getPosition());
    }

    void paintNode(Camera camera, Graphics2D g, boolean highlight){
        // If highlight is true node will be painted with strokeWeight 2
        if(highlight){
            g.setStroke(new BasicStroke(2));
        } else {
            g.setStroke(new BasicStroke(1));
        }

        Vector2 rel_pos = relativePosition(camera);
        g.setColor(Color.BLACK);
        g.drawRect((int) rel_pos.x(), (int) rel_pos.y(), (int) size.x(), (int) size.y());
        g.drawString(title, (int) rel_pos.x(), (int) rel_pos.y() - 5);

        for (Object i : getChildren()) {
            GraphicNode tmp = (GraphicNode) i;

            Vector2 start_line = rel_pos.add(new Vector2(size.x(), 10));
            Vector2 end_line = tmp.relativePosition(camera);
            paintArc(g, start_line, end_line);
        }
    }

    void paintArc(Graphics2D g, Vector2 start, Vector2 end){
        // control = start + (end - start) / 2
        Vector2 control = start.add(end.subtract(start).multiply(0.5));

        CubicCurve2D.Double arc = new CubicCurve2D.Double(start.x(), start.y(),
                control.x(), start.y(),
                control.x(), end.y(),
                end.x(), end.y());
        g.draw(arc);

    }
}
