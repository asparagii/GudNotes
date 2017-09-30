package DataStructures;

import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyledDocument;

class ConnectedString {
    private StyledDocument text;

    ConnectedString(String initial_text){
        text = new DefaultStyledDocument();
    }

    void setText(StyledDocument new_text){
        text = new_text;
    }

    StyledDocument getText() {
        return text;
    }
}
