


import static org.junit.Assert.*;

import java.awt.Point;
import java.util.ArrayList;

import javax.swing.text.AbstractDocument.LeafElement;

import org.junit.Before;
import org.junit.Test;


public class DibujoCircuitoTest {
	DibujoCircuito dC;
	
	@Before
	public void setUp(){
		dC = new DibujoCircuito("..\\circuito1.jpg");
		dC.addPuntoSiProcede(new Point(10, 10));
	}
	
	
	@Test
	public void testAddPuntoSiProcede() {
		//NO HAY QUE COMPARAR OBJETOS!!
		dC.addPuntoSiProcede(new Point(11, 11));
		assertEquals(1, dC.getlPuntos().size());
		
//		assertNull(dC.getlPuntos().get(1));			//en el 1 no deberia haber, NO FUNCJIONA PORQUE EL PROGRAMA SE PETA, NO DEVUELVE NADA (NULL POINTER EXCEPTION)
		
		
		dC.addPuntoSiProcede(new Point(410, 410));
		assertTrue(dC.getlPuntos().size() == 2);	
		
	}

}
