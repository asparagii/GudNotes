import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.Callable;

class NodeContent {
    private Vector2 size;
    private StringBuffer text_buffer;
    private ArrayList<String> text;
    private int cursor;
    private int line_cursor;
    private int border_size;

    static final public int LEFT = 0;
    static final public int RIGHT = 1;
    static final public int END = 4;
    static final public int START = 5;


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

    // 'bring' will be the content of the new line
    private void newLine(String bring){
            text.add("");
            text_buffer.delete(0, text_buffer.length());
            text_buffer.insert(0, bring);
            cursor = 0;
            line_cursor++;
    }

    void moveCursor(int m){
        switch(m){
            case RIGHT:
                if(cursor < text_buffer.length())
                    cursor++;
                break;

            case LEFT:
                if(cursor > 0)
                    cursor--;
                break;

            case END:
                cursor = text_buffer.length();
                break;

            case START:
                cursor = 0;
                break;
        }
    }

    void addChar(char c){
        // Delete character
        if(c == '\b'){
            if(cursor > 0)
                text_buffer.deleteCharAt(--cursor);
        }

        // New line
        if(c == '\n'){
            String to_bring = text_buffer.substring(cursor);
            text.set(line_cursor, text.get(line_cursor).substring(0, cursor));
            newLine(to_bring);
        }

        if(isPrintableChar(c)) {
            text_buffer.insert(cursor, c);
            cursor++;
        }
        text.set(line_cursor, text_buffer.toString());
    }

    void setSize(Vector2 new_size){
        size = new_size;
    }

    Vector2 getSize(){ return size; }

    private void drawCursor(Graphics2D g, Vector2 position){
        g.setStroke(new BasicStroke(1));
        Vector2 cursor_position = position.add(new Vector2(border_size + g.getFontMetrics().stringWidth(text_buffer.substring(0, cursor)), 3 + line_cursor * g.getFontMetrics().getHeight()));
        g.drawLine((int) cursor_position.x(), (int) cursor_position.y(), (int) cursor_position.x(), (int) cursor_position.y() + g.getFontMetrics().getHeight());
    }

    boolean print(Graphics2D g, Vector2 position, Callable everyFrame){
        boolean resized = false;
        if(text != null) {
            int i = 0;

            drawCursor(g, position);

            for(String line : text) {
                // If resize is needed
                g.drawString(line, (int) position.x() + border_size, (int) position.y() + g.getFontMetrics().getHeight() * (i + 1));

                if(g.getFontMetrics().stringWidth(line) > (size.x() - border_size)){
                    size.animate(new Vector2(g.getFontMetrics().stringWidth(line) + border_size + 40, size.y()), 5, false, everyFrame);
                }

                i++;
            }
        }

        // If content is resizing
        if(size.animation_timer != null)
            resized = size.animation_timer.isRunning();

        return resized;
    }
}
