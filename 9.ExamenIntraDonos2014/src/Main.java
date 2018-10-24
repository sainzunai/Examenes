
import javax.swing.SwingUtilities;

/** Clase principal de editor de circuitos
 * @author Andoni Eguíluz Morán
 * Facultad de Ingeniería - Universidad de Deusto
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
