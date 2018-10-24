
import java.io.Serializable;

/** Permite crear un JTextField asociado a un objeto de circuito, de modo que puede saberse
 * el objeto al que califica el nombre introducido en el cuadro de texto.
 * @author Andoni Eguíluz Morán
 * Facultad de Ingeniería - Universidad de Deusto
 */
public class TextoDeCircuito extends javax.swing.JTextField implements Serializable {
	private static final long serialVersionUID = 1L;
	Object objetoAsociado;
	
	/** Construye un nuevo cuadro de texto, indicando la anchura en columnas y el objeto de circuito al que se asocia
	 * @param columnas	Número aproximado de columnas (caracteres) que caben en la caja
	 * @param objetoAsociado	Objeto al que se asocia el cuadro de texto
	 */
	public TextoDeCircuito( int columnas, Object objetoAsociado ) {
		super( columnas );
		this.objetoAsociado = objetoAsociado;
	}
	
}
