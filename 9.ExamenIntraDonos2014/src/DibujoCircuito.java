

import java.awt.*;
import java.awt.geom.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/** Clase de datos del dibujo de circuito. Almacena toda la informaci�n que el usuario edita dentro de un circuito: 
 * los puntos, las conexiones, y dem�s datos auxiliares
 * @author Andoni Egu�luz Mor�n
 * Facultad de Ingenier�a - Universidad de Deusto
 */
public class DibujoCircuito {
	private ArrayList<Point> lPuntos;                 // Puntos gr�ficos
	private ArrayList<TextoDeCircuito> tPuntos;       // Textos de puntos
	private ArrayList<ArrayList<Point>> lConexiones;  // Conexiones entre puntos gr�ficos (al menos 2 puntos cada conexi�n)
	String nomFicheroCircuito;                // Nombre de fichero gr�fico del circuito de fondo

	/* TAREA 6 - Atributos */
	/* FIN TAREA 6 */

	private static Stroke stroke1 = new BasicStroke(1);
	private static Stroke stroke3 = new BasicStroke(3);
	private static Stroke stroke5 = new BasicStroke(5);
	private Point marcaPunto = null;
	private Point marcaLinea = null;
	private Point marcaRect = null;

	private PanelDeDibujo pDibujo = null;

	// public static int RADIO_TEXTO_COMPONENTE = 20;
	// public static int DIST_MINIMA_PUNTOS_COMPONENTE = 26;
	public static int RADIO_MARCA_PUNTO = 4;
	public static int RADIO_CONEXION = 4;
	public static int CERCANIA_PUNTOS = 5;  // Si dos puntos est�n a esta distancia o menor, se consideran el mismo
	public static int DIST_X_TEXTO = 10;    // Distancia x,y del punto a su texto. Anchura y altura de la caja
	public static int DIST_Y_TEXTO = -10;
	public static int ANCH_TEXTO = 50, ALT_TEXTO = 20;
	public static int DIST_X_TEXTO_VERT = -25, DIST_Y_TEXTO_VERT = -30;   // Distancias en vertical
	private static Color COLOR_SELECCION = Color.orange;
	private static Stroke STROKE_SELECCION = new BasicStroke(7);

	
	

	public ArrayList<Point> getlPuntos() {
		return lPuntos;
	}

	public void setlPuntos(ArrayList<Point> lPuntos) {
		this.lPuntos = lPuntos;
	}

	public ArrayList<TextoDeCircuito> gettPuntos() {
		return tPuntos;
	}

	public void settPuntos(ArrayList<TextoDeCircuito> tPuntos) {
		this.tPuntos = tPuntos;
	}

	public Point getMarcaPunto() {
		return marcaPunto;
	}

	/** Crea el objeto de datos de dibujo del circuito
	 * @param nombreFic	Nombre del fichero gr�fico de fondo
	 */
	public DibujoCircuito( String nombreFic ) {
		nomFicheroCircuito = nombreFic;
		lPuntos = new ArrayList<Point>();
		lConexiones = new ArrayList<ArrayList<Point>>();
		tPuntos = new ArrayList<TextoDeCircuito>();
	}

	/** Cambia o refresca el panel de dibujo asociado a estos datos. Inserta en el panel todos los componentes (textfields)
	 * @param pDibujo
	 */
	public void setPanelDibujo( PanelDeDibujo pDibujo ) {
		this.pDibujo = pDibujo;
		if (pDibujo!=null && tPuntos.size()>0)
			for (TextoDeCircuito tf : tPuntos) pDibujo.add( tf );
	}

	public ArrayList<Point> getPuntos() { return lPuntos; }
	public ArrayList<ArrayList<Point>> getConexiones() { return lConexiones; } 

	/** A�ade un punto de conexi�n
	 * @param p	Punto de conexi�n
	 */
	public void addPunto( Point p ) {
		lPuntos.add( p );
		TextoDeCircuito tf = new TextoDeCircuito( 3, p );
		tf.setText( "Punto " + lPuntos.size() );
		tf.setBounds( p.x + DIST_X_TEXTO, p.y + DIST_Y_TEXTO, ANCH_TEXTO, ALT_TEXTO );
		tPuntos.add( tf );
		if (pDibujo!=null) pDibujo.add( tf );
	}

	/** Crea un nuevo punto si no hay otro suficientemente cerca.
	 * @param p	Punto a consultar
	 * @return	Punto ya creado si est� suficientemente cerca, punto nuevo (el mismo recibido) si no hab�a ninguno cerca 
	 */
	public Point addPuntoSiProcede( Point p ) {
		for (Point pAnt : lPuntos) {
			if (pAnt.distance( p.getX(), p.getY() ) <= CERCANIA_PUNTOS) {
				return pAnt;
			}
		}
		addPunto( p );
		return p;
	}

	/** Indica si un punto dado ya existe
	 * @param p	Punto a consultar
	 * @return	Punto ya creado si est� suficientemente cerca, el mismo punto recibido si no hab�a ninguno cerca
	 */
	public Point existePunto( Point p ) {
		for (Point pAnt : lPuntos) {
			if (pAnt.distance( p.getX(), p.getY() ) <= CERCANIA_PUNTOS) {
				return pAnt;
			}
		}
		return p;
	}

	/** A�ade una conexi�n. Si alguno de los puntos no exist�a (de acuerdo a la distancia establecida), se crea
	 * @param p1	Punto inicial de conexi�n
	 * @param p2	Punto final de conexi�n (y puntos intermedios si proceden)
	 */
	public void addConexion( Point p1, Point... p2 ) {
		if (p2.length>=1) {
			ArrayList<Point> nuevaConexion = new ArrayList<Point>();
			p1 = addPuntoSiProcede( p1 );
			nuevaConexion.add( p1 );
			for (Point p : p2) { p = addPuntoSiProcede(p); nuevaConexion.add( p ); }
			lConexiones.add( nuevaConexion );
			reposicionaSiHorOVert( nuevaConexion );
		}
	}

	// Reposiciona los puntos de la conexi�n indicada, si est� en horizontal o vertical
	private void reposicionaSiHorOVert( ArrayList<Point> con ) {
		if (con.size()>1) {
			int distX = Math.abs( con.get(0).x - con.get(1).x ); 
			int distY = Math.abs( con.get(0).y - con.get(1).y );
			if (distX==0) {  // Est� en vertical
				for (Point p : con) {
					int pos = lPuntos.indexOf( p );
					if (pos!=-1) {
						TextoDeCircuito tf = tPuntos.get(pos);
						tf.setLocation( p.x + DIST_X_TEXTO, p.y + DIST_Y_TEXTO );
					}
				}
			} else if (distY==0) {  // Est� en horizontal
				for (Point p : con) {
					int pos = lPuntos.indexOf( p );
					if (pos!=-1) {
						TextoDeCircuito tf = tPuntos.get(pos);
						tf.setLocation( p.x + DIST_X_TEXTO_VERT, p.y + DIST_Y_TEXTO_VERT );
					}
				}
			}
		}
	}

	public void setMarcaPunto( Point p ) { marcaPunto = p; }
	public void setMarcaLinea( Point p ) { marcaLinea = p; marcaRect = null; }
	public void setMarcaRect( Point p ) { marcaRect = p; marcaLinea = null; }

	/** Selecciona los elementos que est�n dentro del rect�ngulo indicado
	 * @param p	Punto para la selecci�n
	 */
	public void seleccionarElementos( Point esquina1, Point esquina2 ) {
		int minX = Math.min( esquina1.x, esquina2.x );
		int maxX = Math.max( esquina1.x, esquina2.x );
		int minY = Math.min( esquina1.y, esquina2.y );
		int maxY = Math.max( esquina1.y, esquina2.y );
		Rectangle2D.Double rect = new Rectangle2D.Double( minX, minY, maxX-minX, maxY-minY );
		/* TAREA 6 */
		for (Point pto : lPuntos) {
			if (rect.contains(pto)) {
				System.out.println( pto );
			}
		}
		for (ArrayList<Point> con : lConexiones) {
			boolean contenido = true;
			for (Point pto : con) {
				if (!rect.contains(pto)) {
					contenido = false;
					break;
				}
			}
			if (contenido) {
				System.out.println( con );
			}
		}
		/* FIN TAREA 6 */
	}

	/** Selecciona cualquier elemento que est� suficientemente cerca del punto indicado
	 * @param p	Punto para la selecci�n, null para borrar la selecci�n
	 */
	public void seleccionarElementos( Point p ) {
		/* TAREA 6 */
		if (p!=null) {
			for (Point pto : lPuntos) {
				if (pto.distance( p.x, p.y ) <= CERCANIA_PUNTOS) {
					System.out.println( pto );
				}
			}
			for (ArrayList<Point> con : lConexiones) {
				Point pIni = con.get(0);
				for (int i=1; i<con.size(); i++) {
					Point pFin = con.get(i);
					Line2D.Float lin = new Line2D.Float( pIni, pFin );
					if (lin.ptSegDist( p ) <= CERCANIA_PUNTOS) {
						System.out.println( con );
						break;
					}
				}
			}
		}
		/* FIN TAREA 6 */
	}


	/** Dibuja todas las marcas en el objeto graphics recibido como par�metro
	 * @param g
	 */
	public void dibuja( Graphics2D g ) {
		// Conexiones
		for (ArrayList<Point> lP : lConexiones) {
			/* TAREA 6 */
			if (false) {  // Cambiar esta condici�n por la pertenencia del punto a la selecci�n (tarea 6)
				g.setColor( COLOR_SELECCION ); g.setStroke( STROKE_SELECCION );
			} 
			/* FIN TAREA 6 */
			else {
				g.setColor( Color.green ); g.setStroke( stroke5 );
			}
			Point pAnt = null;
			for (Point p : lP) {
				if (pAnt != null) dibujaLinea( g, pAnt, p );
				pAnt = p;
			}
		}
		// Puntos
		for (Point p : lPuntos) {
			/* TAREA 6 */
			if (false) {  // Cambiar esta condici�n por la pertenencia de la lista de puntos a la selecci�n (tarea 6)
				g.setColor( COLOR_SELECCION ); g.setStroke( STROKE_SELECCION );
			} 
			/* FIN TAREA 6 */
			else {
				g.setColor( Color.blue ); g.setStroke( stroke3 );
			}
			dibujaPunto( g, p );
		}
		// Marcas (si existen)
		g.setStroke( stroke1 );
		if (marcaPunto!=null) {
			g.setColor( Color.red ); 
			g.drawOval( marcaPunto.x-RADIO_MARCA_PUNTO, marcaPunto.y-RADIO_MARCA_PUNTO, RADIO_MARCA_PUNTO*2, RADIO_MARCA_PUNTO*2 );
			if (marcaLinea!=null) dibujaLinea( g, marcaPunto, marcaLinea );
			if (marcaRect!=null) dibujaRect( g, marcaPunto, marcaRect );
		}
	}

	private void dibujaPunto( Graphics2D g, Point p ) { g.drawOval( p.x-RADIO_CONEXION, p.y-RADIO_CONEXION, RADIO_CONEXION*2, RADIO_CONEXION*2 ); }
	private void dibujaLinea( Graphics2D g, Point p1, Point p2 ) { g.drawLine( p1.x, p1.y, p2.x, p2.y ); }
	private void dibujaRect( Graphics2D g, Point p1, Point p2 ) {
		int minX = Math.min( p1.x, p2.x );
		int maxX = Math.max( p1.x, p2.x );
		int minY = Math.min( p1.y, p2.y );
		int maxY = Math.max( p1.y, p2.y );
		g.drawRect( minX, minY, maxX-minX, maxY-minY ); 
	}
	
		private String path;  // Path de fichero
	/** Pide interactivamente un nombre de fichero y guarda los datos principales de dibujo de circuito en �l
	 */
	public void guardar() {
		File fPath = pedirFicheroDatos( "Indica fichero donde guardar el circuito"); if (fPath==null) return;
		path = fPath.getAbsolutePath();
		if (!path.toUpperCase().endsWith("DAT")) {
			path = path + ".dat";
			fPath = new File(path);
		}
		if (fPath.exists()) {  // Pide confirmaci�n de sobreescritura
			int conf = JOptionPane.showConfirmDialog( null, 
					"�Atenci�n! El fichero indicado ya existe. �Quieres sobreescribirlo?", 
					"Confirmaci�n de fichero ya existente", JOptionPane.YES_NO_OPTION );
			if (conf!=0) return;  // si no hay confirmaci�n no seguimos
		}
		// Salva datos al fichero
		try {
			ObjectOutputStream oos = new ObjectOutputStream( new FileOutputStream(path) );
			oos.writeObject( nomFicheroCircuito );
			oos.writeObject( lPuntos );
			oos.writeObject( tPuntos );
			oos.writeObject( lConexiones );
			oos.close();
			JOptionPane.showMessageDialog( null, "El fichero " + 
					path + " se ha guardado con los datos.", "Salvado correcto", JOptionPane.INFORMATION_MESSAGE );
		} catch (Exception e2) {
			e2.printStackTrace();
			JOptionPane.showMessageDialog( null, "El fichero " + 
					path + " no ha podido salvarse.", "Fichero incorrecto", JOptionPane.ERROR_MESSAGE );
		}
	}

		// Pide interactivamente un fichero existente de datos
		// (null si no se selecciona)
		private File pedirFicheroDatos( String mens ) {
			File dirActual = new File( System.getProperty("user.dir") );
			JFileChooser chooser = new JFileChooser( dirActual );
			chooser.setFileSelectionMode( JFileChooser.FILES_ONLY );
			chooser.setFileFilter( new FileNameExtensionFilter( 
					"Ficheros de datos de circuito", "dat" ) );
			chooser.setDialogTitle( mens );
			int returnVal = chooser.showOpenDialog( null );
			if (returnVal == JFileChooser.APPROVE_OPTION)
				return chooser.getSelectedFile();
			else 
				return null;
		}

	
	/* TAREA 3 */
	/* FIN TAREA 3 */

	/* TAREA 5 - M�todo recursivo */
		// M�todo inicial (no recursivo)
		public void calcCortes() {
			for (int i=0; i<lConexiones.size()-1; i++) {
				ArrayList<Point> seg = lConexiones.get(i);
				for (int j=i+1; j<lConexiones.size(); j++) {
					ArrayList<Point> seg2 = lConexiones.get(j);
					Line2D.Double lin = new Line2D.Double( seg.get(0).x, seg.get(0).y, seg.get(1).x, seg.get(1).y );
					calcCortesRec( lin, seg2.get(0).x, seg2.get(0).y, seg2.get(1).x, seg2.get(1).y, 20 );
				}
			}
		}

		// M�todo recursivo
		// Recibe una l�nea y un segmento de cuatro puntos y recursivamente va aproximando la mitad de ese segmento,
		// en funci�n de cu�l de los dos extremos est� m�s cerca de la l�nea
		private void calcCortesRec( Line2D.Double lin, double x1, double y1, double x2, double y2, int numLlams ) {
			// TODO Hacer el m�todo recursivo
			// Usar el m�todo ptSegDist( x, y ) de la clase Line2D.Double
			// Entender que si esta distancia despu�s de 20 llamadas recursivas es menor que 0.0001, es que hay un punto de corte.
			// Visualizarlo solo en ese caso.
		}
	/* FIN TAREA 5 */
		
}
