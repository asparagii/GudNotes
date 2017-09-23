// This class provides a basic camera
public class Camera {
    private int[] position;
    private float zoom;

    public Camera(int x, int y, float startZoom){
        position = new int[] {x, y};
        zoom = startZoom;
    }


    // Set camera position to global coordinates (x, y)
    public void setPosition(int x, int y){
        position[0] = x;
        position[1] = y;
    }

    // Set camera position so that n is in relative coordinates (400, 200) / zoom
    void lookAt(GraphicNode n){
        int[] to_look = n.getPosition();
        setPosition(to_look[0] - (int) (400 / zoom), to_look[1] - (int) (200 / zoom));
    }

    public void setZoom(float z){
        zoom = z;
    }

    public float getZoom(){ return zoom; }

    public int[] getPosition(){
        return position;
    }
}
