import java.awt.Graphics2D;
import java.util.ArrayList;

class NodeContent {
    private Vector2 size;
    private StringBuffer text_buffer;
    private ArrayList<String> text;
    private int cursor;
    private int line_cursor;
    private int border_size;

    NodeContent(double width, double height, int border_size){
        size = new Vector2(width, height);
        text_buffer = new StringBuffer();
        text = new ArrayList<>(1);
        text.add("");
        cursor = 0;
        line_cursor = 0;
        this.border_size = border_size;
    }

    private boolean isPrintableChar( char c ) {
        Character.UnicodeBlock block = Character.UnicodeBlock.of( c );
        return (!Character.isISOControl(c)) &&
                block != null &&
                block != Character.UnicodeBlock.SPECIALS;
    }

    private void newLine(){
            text.add("");
            text_buffer.delete(0, text_buffer.length());
            cursor = 0;
            line_cursor++;
    }

    void addChar(char c){
        // Delete character
        if(c == '\b'){
            if(cursor > 0)
                text_buffer.deleteCharAt(--cursor);
        }

        // New line
        if(c == '\n'){
            newLine();
        }

        if(isPrintableChar(c)) {
            text_buffer.append(c);
            cursor++;
        }
        text.set(line_cursor, text_buffer.toString());
    }

    void setSize(Vector2 new_size){
        size = new_size;
    }

    boolean print(Graphics2D g, Vector2 position){
        if(text != null) {
            int i = 0;


            for(String line : text) {
                if(g.getFontMetrics().stringWidth(line) > (size.x() - border_size)){
                    System.out.println(size.x() + " , " + size.y());
                    return true;
                }
                g.drawString(line, (int) position.x() + border_size, (int) position.y() + g.getFontMetrics().getHeight() * (i + 1));
                i++;
            }
        }
        return false;
    }
}
