import java.awt.*;

public class GraphicNode extends Node{

    private String title;
    private int[] size;
    private int[] position;

    public GraphicNode(String title, int height, int width){
        super();
        this.title = title;
        position = new int[2];
        size = new int[] {width, height};
    }


    // Set position near the edge of the (only) parent
    // To be called only when node has one parent
    public void setAutoPosition(int minSpace){
        try {
            GraphicNode tmp = (GraphicNode) getParents().get(0);
            position[0] = tmp.getPosition()[0] + tmp.getSize()[0] + minSpace;

            int sum = 0;
            for (Object i : tmp.getChildren()) {
                GraphicNode e = (GraphicNode) i;
                sum += e.getSize()[1] + minSpace;
            }
            position[1] = tmp.getPosition()[1] + sum - getSize()[1] - minSpace;

        } catch (ArrayIndexOutOfBoundsException e){
            position[0] = 0;
            position[1] = 0;
        }
    }

    public void setPosition(int x, int y){
        position[0] = x;
        position[1] = y;
    }

    public int[] getPosition(){
        return position;
    }

    public int[] getSize(){
        return size;
    }

    private int[] relativePosition(Camera camera){
        int t_x = position[0] - camera.getPosition()[0];
        int t_y = position[1] - camera.getPosition()[1];
        return new int[] {t_x, t_y};
    }

    void paintNode(Camera camera, Graphics2D g, boolean highlight){

        // If highlight is true, node will be painted with strokeWeight 2
        if(highlight){
            g.setStroke(new BasicStroke(2));
            System.out.println("node is highlighted");
        } else {
            g.setStroke(new BasicStroke(1));
        }

        g.setColor(Color.BLACK);

        int[] rel_pos = relativePosition(camera);
        g.drawRect(rel_pos[0], rel_pos[1], size[0], size[1]);
        g.drawString(title, rel_pos[0], rel_pos[1] - 5);
    }
}
