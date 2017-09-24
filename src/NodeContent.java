import java.awt.*;
import java.util.ArrayList;

class NodeContent {
    private Vector2 size;
    private ArrayList<String> text;
    private int[] cursor;

    NodeContent(double width, double height){
        size = new Vector2(width, height);
        text = new ArrayList<>(1);
        text.add("");
        cursor = new int[]{0, 0};
    }

    private boolean isPrintableChar( char c ) {
        Character.UnicodeBlock block = Character.UnicodeBlock.of( c );
        return (!Character.isISOControl(c)) &&
                block != null &&
                block != Character.UnicodeBlock.SPECIALS;
    }

    void addChar(char c){
        if(isPrintableChar(c)) {
            String line = text.get(cursor[0]);
            text.set(cursor[0], line.substring(0, cursor[1]) + c + line.substring(cursor[1], line.length()));
            cursor[1]++;
        }
    }

    void setSize(Vector2 new_size){
        size = new_size;
    }

    void print(Graphics2D g, Vector2 position){
        if(text != null) {
            for (int i = 0; i < text.size(); i++) {
                g.drawString(text.get(i), (int) position.x() + 3, (int) position.y() + g.getFontMetrics().getHeight() * (i + 1));

            }
        }
    }
}
