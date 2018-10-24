

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.ArrayList;

import javax.swing.text.AbstractDocument.LeafElement;

import org.junit.Before;
import org.junit.Test;

public class DibujoCircuitoTest {
	DibujoCircuito dC = new DibujoCircuito("circuito1.jpg");
	@Before
	public void setUp(){
		dC.addPuntoSiProcede(new Point(10, 10));
	}
	
	
	@Test
	public void testAddPuntoSiProcede() {
		//hago una copia de la lista de puntos para compararla despues
		ArrayList<Point> lTemp = dC.getlPuntos();
		//en la lista ya hay un pto en el 10, 10. voy a hacerlo en el 11,11. no deberia anyadirse
		dC.addPuntoSiProcede(new Point(11, 11));
		assertEquals(lTemp, dC.getlPuntos());	
		
//		assertNull(dC.getlPuntos().get(1));			//en el 1 no deberia haber nada NO DEBERIA DUNCIONAR¿?¿?

		
		dC.addPuntoSiProcede(new Point(410, 410));
		assertTrue(dC.getlPuntos() != lTemp);	//NO TIENE SENTIDO, PREGUNTAR¿??¿¿?¿?¿?
//		assertTrue(lTemp.size() == 1);
		
	}

}
