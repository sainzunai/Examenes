
import javax.swing.SwingUtilities;

/** Clase principal de editor de circuitos
 * @author Andoni Egu�luz Mor�n
 * Facultad de Ingenier�a - Universidad de Deusto
 */
public class Main {

	public static void main(String[] args) {
		SwingUtilities.invokeLater( new Runnable() { 
			public void run() {
				VentanaEditorCircuitos v = new VentanaEditorCircuitos();
				v.setVisible( true );
			}
		} );
	}

}
