package sn.l2gl.farimah.daara;

import sn.l2gl.farimah.daara.view.AppDaara;
import javax.swing.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AppDaara().setVisible(true));
    }
}