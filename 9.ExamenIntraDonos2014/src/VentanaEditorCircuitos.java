
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/** Clase de ventana principal de editor de circuitos
 * @author Andoni Eguíluz Morán
 * Facultad de Ingeniería - Universidad de Deusto
 */
@SuppressWarnings("serial")
public class VentanaEditorCircuitos extends JFrame {
	private PanelDeDibujo pDibujo;
	private DibujoCircuito dibujoCircuito;
	/* TAREA 2 - Atributo */
	/* FIN TAREA 2 */
	
	private Point inicioDrag;
	JToggleButton bSeleccion, bPuntoMed, bConexion;  // Botones de selección
	JCheckBox cbAEjes;  // Checkbox para ajustar conexiones a los ejes (horizontal y vertical)
	JSlider sTransparencia;  // Slider de transparencia
	
	/** Constructor de ventana principal
	 */
	public VentanaEditorCircuitos() {
		// Configuración
		setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		setSize( 800, 600 );
		setTitle( "Editor de circuitos" );
		/* TAREA 2 */
		/* FIN TAREA 2 */
		
		// Datos y componentes
		dibujoCircuito = new DibujoCircuito( "" ); //  "bin/examen/donosti201411/circuito1.jpg" para empezar con un fichero gráfico fijo
		pDibujo = new PanelDeDibujo( dibujoCircuito );
		dibujoCircuito.setPanelDibujo( pDibujo );
		JPanel pBotonera = new JPanel();
		cbAEjes = new JCheckBox( "En ejes" );
		bSeleccion = new JToggleButton("Selección"); bSeleccion.setBorder( BorderFactory.createBevelBorder( BevelBorder.RAISED ) );
		bPuntoMed = new JToggleButton("Punto"); bPuntoMed.setBorder( BorderFactory.createBevelBorder( BevelBorder.RAISED ) );
		bConexion = new JToggleButton("Conexión"); bConexion.setBorder( BorderFactory.createBevelBorder( BevelBorder.RAISED ) );
		sTransparencia = new JSlider(0, 255);
		JScrollPane spDibujo = new JScrollPane( pDibujo );
		ButtonGroup bg = new ButtonGroup();
		JButton bGuardar = new JButton("Guardar");
		/* TAREA 3 */
		/* FIN TAREA 3 */
		/* TAREA 5 */
		/* FIN TAREA 5 */
	
		// Configuración componentes
		sTransparencia.setPreferredSize( new Dimension( 80, 20 ));
		cbAEjes.setSelected( true );
		bPuntoMed.setSelected( true );
		
		// Añadir componentes a contenedores
		pBotonera.add( cbAEjes );
		pBotonera.add( bSeleccion ); pBotonera.add( bPuntoMed ); pBotonera.add( bConexion );
		pBotonera.add( new JLabel("Transp.:" ) ); pBotonera.add( sTransparencia );
		pBotonera.add( bGuardar );
		bg.add(bSeleccion); bg.add(bPuntoMed); bg.add(bConexion);
		getContentPane().add( spDibujo, BorderLayout.CENTER );
		getContentPane().add( pBotonera, BorderLayout.SOUTH );
		
		// Eventos
		pDibujo.addMouseListener( new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				if (!bSeleccion.isSelected()) dibujoCircuito.seleccionarElementos( null );  // Quitar selección si la hubiera
				quitaMarcas();
				Point pDestino = e.getPoint();
				if (!dibujoCircuito.existePunto(pDestino).equals(pDestino)) {  // Ya existe punto
					pDestino = dibujoCircuito.existePunto(pDestino);
				} else {
					if (cbAEjes.isSelected() && !bSeleccion.isSelected()) { // Forzar a ejes
						if (Math.abs(inicioDrag.x-pDestino.x) > Math.abs(inicioDrag.y-pDestino.y)) {  // Forzar a horizontal
							pDestino.y = inicioDrag.y;
						} else {  // Forzar a vertical
							pDestino.x = inicioDrag.x;
						}
					}
				}
				if (e.getPoint().equals( inicioDrag )) {
					// Click
					if (bPuntoMed.isSelected()) {  // Añadir punto de conexión
						dibujoCircuito.addPunto( inicioDrag );
					} else if (bSeleccion.isSelected()) {  // Seleccionar elementos
						dibujoCircuito.seleccionarElementos( inicioDrag );
					}
				} else {
					// Drag
					if (bConexion.isSelected()) {  // Añadir conexión
						dibujoCircuito.addConexion( inicioDrag, pDestino );
					} else if (bSeleccion.isSelected()) {  // Seleccionar elementos
						dibujoCircuito.seleccionarElementos( inicioDrag, pDestino );
					}
				}
				inicioDrag = null;
			}
			public void mousePressed(MouseEvent e) {
				inicioDrag = e.getPoint();
				if (!dibujoCircuito.existePunto(inicioDrag).equals(inicioDrag) && !bSeleccion.isSelected())  // Ya existe punto
					inicioDrag = dibujoCircuito.existePunto(inicioDrag);
				marcaInicioDrag();
			}
		});
		pDibujo.addMouseMotionListener( new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				if (bSeleccion.isSelected()) {
					marcaRectDrag( e.getPoint() );
				} else {
					marcaLineaDrag( e.getPoint() );
				}
			}
		});
		sTransparencia.addChangeListener( new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				pDibujo.setTransparencia( sTransparencia.getValue() );
				pDibujo.repaint();
			}
		});
		bGuardar.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dibujoCircuito.guardar();
			}
		});
		/* TAREA 2 */
		/* FIN TAREA 2 */
		/* TAREA 3 */
		/* FIN TAREA 3 */
		/* TAREA 5 */
		/* FIN TAREA 5 */
	}

	// Marca en la ventana el movimiento mientras se hace: inicio
	private void marcaInicioDrag() {
		dibujoCircuito.setMarcaPunto( inicioDrag );
		pDibujo.repaint();
	}

	// Marca en la ventana el movimiento mientras se hace: fin de línea
	private void marcaLineaDrag( Point p ) {
		dibujoCircuito.setMarcaLinea( p );
		pDibujo.repaint();
	}

	// Marca en la ventana el movimiento mientras se hace: fin de rectángulo
	private void marcaRectDrag( Point p ) {
		dibujoCircuito.setMarcaRect( p );
		pDibujo.repaint();
	}

	// Quita las marcas en la ventana del movimiento mientras se hace
	private void quitaMarcas() {
		dibujoCircuito.setMarcaPunto( null );
		dibujoCircuito.setMarcaLinea( null );
		dibujoCircuito.setMarcaRect( null );
		pDibujo.repaint();
	}

}
