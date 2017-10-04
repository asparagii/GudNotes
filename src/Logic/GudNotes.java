package Logic;

import GUI.MainFrame;

import javax.swing.*;

public class GudNotes {

    // TODO link graph to GUI

    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
