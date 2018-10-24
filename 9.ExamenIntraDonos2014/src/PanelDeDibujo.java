
import java.awt.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

/** Clase de panel de Swing especializado para dibujar circuitos encima de un gráfico bitmap de un circuito.
 * Muestra un panel con un fondo gráfico semitransparente, partiendo de un fichero (jpg, png, gif).
 * @author Andoni Eguíluz Morán
 * Facultad de Ingeniería - Universidad de Deusto
 */
@SuppressWarnings("serial")
public class PanelDeDibujo extends JPanel {
	private static int transparenciaFondo = 180; // 0 = imagen tal cual, 255 = completamente transparente
	private ImageIcon imagen;
	private DibujoCircuito dibujoCircuito;
	
	/** Construye el panel de dibujo
	 * @param dibujo	Objeto de dibujo de circuito asociado
	 */
	public PanelDeDibujo( DibujoCircuito dibujo ) {
		dibujoCircuito = dibujo;
		if (dibujo.nomFicheroCircuito==null || dibujo.nomFicheroCircuito.equals(""))
			dibujo.nomFicheroCircuito = pedirFicheroGrafico();
		try {
			imagen = new ImageIcon( dibujo.nomFicheroCircuito );
			if (imagen.getIconHeight()==-1) throw new NullPointerException();
			setPreferredSize( new Dimension( imagen.getIconWidth(), imagen.getIconHeight() ));
		} catch (Exception e) {
			System.err.println( "Error en carga de recurso: " + dibujo.nomFicheroCircuito + " no encontrado" );
			imagen = null;
			setName( (dibujo.nomFicheroCircuito==null) ? "null" : (new File(dibujo.nomFicheroCircuito)).getName() );
		}
		setLayout( null );
	}
	
	/** Cambia el fondo gráfico por uno nuevo
	 * @param nomFicheroCircuito	Nuevo fichero gráfico
	 */
	public void setFondoImagen( String nomFicheroCircuito ) {
		try {
			ImageIcon nuevaImagen = new ImageIcon( nomFicheroCircuito );
			if (imagen.getIconHeight()==-1) throw new NullPointerException();
			setPreferredSize( new Dimension( imagen.getIconWidth(), imagen.getIconHeight() ));
			dibujoCircuito.nomFicheroCircuito = nomFicheroCircuito;
			imagen = nuevaImagen;
			repaint();
		} catch (Exception e) {
			System.err.println( "Error en carga de recurso: " + nomFicheroCircuito + " no encontrado" );
		}
	}

	/** Cambia el valor de transparencia del fondo gráfico
	 * @param valTransp
	 */
	public void setTransparencia( int valTransp ) {
		if (valTransp>=0 && valTransp <=255) transparenciaFondo = valTransp;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;  // El Graphics realmente es Graphics2D
		if (imagen!=null) {
			Image img = imagen.getImage();
			// Escalado más fino con estos 3 parámetros:
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);	
	        // Dibujado de la imagen
	        g2.drawImage( img, 0, 0, imagen.getIconWidth(), imagen.getIconHeight(), null );
	        g2.setColor( new Color( 255, 255, 255, transparenciaFondo ));
	        g2.fillRect( 0, 0, imagen.getIconWidth(), imagen.getIconHeight() );
	        g2.drawLine( 0, 0, 50, 50 );
		} else {
			super.paintComponent(g);
			g2.setColor( Color.white );
			g2.drawString( getName(), 10, 10 );
		}
		if (dibujoCircuito!=null) dibujoCircuito.dibuja( g2 );
	}

		// Pide interactivamente un fichero gráfico
		// (null si no se selecciona)
		private String pedirFicheroGrafico() {
			File dirActual = new File( System.getProperty("user.dir") );
			JFileChooser chooser = new JFileChooser( dirActual );
			chooser.setFileSelectionMode( JFileChooser.FILES_ONLY );
			chooser.setFileFilter( new FileNameExtensionFilter( 
					"Ficheros de gráfico de circuito", "jpg", "png", "gif" ) );
			chooser.setDialogTitle( "Selecciona gráfico de circuito" );
			int returnVal = chooser.showOpenDialog( null );
			if (returnVal == JFileChooser.APPROVE_OPTION)
				return chooser.getSelectedFile().getAbsolutePath();
			else 
				return null;
		}
	
}
