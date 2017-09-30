import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args){
        Workbench view = new Workbench();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                GUI(view);
            }
        });
    }

    private static void GUI(View view){
        JFrame frame = new JFrame("GudNotes");
        frame.setSize(1280, 720);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.add(view);
        frame.setVisible(true);
    }
}

class Workbench extends View {
    Workbench (){
        super();

        // Keyboard input
        addKeyListener(this);
        // Mouse input
        addMouseListener(this);
        addMouseMotionListener(this);

        // Zoom with ctrl + mouseWheel
        addMouseWheelListener(this);
        // --- Set input map ---
        //
        // ctrl + n => append node and move cursor
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK), "appendNodeAndMove");
        // ctrl + shift + n => add brother
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK), "addBrother");
        // ctrl + alt + arrow keys => navigate nodes
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.CTRL_DOWN_MASK | InputEvent.ALT_DOWN_MASK), "changeCursorLeft");
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.CTRL_DOWN_MASK | InputEvent.ALT_DOWN_MASK), "changeCursorRight");
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.CTRL_DOWN_MASK | InputEvent.ALT_DOWN_MASK), "changeCursorUp");
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.CTRL_DOWN_MASK | InputEvent.ALT_DOWN_MASK), "changeCursorDown");



        getActionMap().put("appendNodeAndMove", appendNodeAndMove);
        getActionMap().put("appendNode", appendNode);
        getActionMap().put("changeCursorLeft", changeCursorLeft);
        getActionMap().put("changeCursorRight", changeCursorRight);
        getActionMap().put("changeCursorUp", changeCursorUp);
        getActionMap().put("changeCursorDown", changeCursorDown);
        getActionMap().put("addBrother", addBrother);
    }
}
