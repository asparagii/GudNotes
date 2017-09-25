// This class provides a basic camera
public class Camera {
    Vector2 position;
    private float zoom;

    public Camera(int x, int y, float startZoom){
        position = new Vector2(x, y);
        zoom = startZoom;
    }


    // Set camera position to global coordinates (x, y)
    public void setPosition(Vector2 pos){
        position = pos;
    }

    // Returns position that camera should assume to have node n in relative coordinates (400, 200) / zoom
    Vector2 lookAt(GraphicNode n){
        Vector2 offset = new Vector2(400 / zoom, 200 / zoom);
        Vector2 to_look = n.getPosition();
        return to_look.subtract(offset);
    }

    void move(Vector2 speed){
        position = position.add(speed);
    }

    public void setZoom(float z){
        zoom = z;
    }

    // Add to_add to zoom
    public void addZoom(double to_add){ zoom += to_add; }

    public float getZoom(){ return zoom; }

    public Vector2 getPosition(){
        return position;
    }
}
