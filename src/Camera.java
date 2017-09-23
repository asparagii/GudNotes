public class Camera {
    int[] position;
    float zoom;

    public Camera(int x, int y, float startZoom){
        position = new int[] {x, y};
        zoom = startZoom;
    }


    // x and y will be at the top left of the screen, moved to the center a little bit
    // (Yeah, I know, best code)
    public void setPosition(int x, int y){
        position[0] = x;
        position[1] = y;
    }

    void lookAt(GraphicNode n){
        int[] to_look = n.getPosition();
        setPosition(to_look[0] - 400, to_look[1] - 200);
    }

    public void setZoom(float z){
        zoom = z;
    }

    public int[] getPosition(){
        return position;
    }
}
