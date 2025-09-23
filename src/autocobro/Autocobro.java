package autocobro;

import autocobro.UI.FrameP;
import javax.swing.SwingUtilities;

public class Autocobro {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrameP().setVisible(true));
    }
    
}
