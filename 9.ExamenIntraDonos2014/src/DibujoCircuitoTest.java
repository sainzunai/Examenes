

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
		dC.addPunto(new Point(10, 10));
	}
	
	
	@Test
	public void testAddPuntoSiProcede() {
		//hago una copia de la lista de puntos para compararla despues
		ArrayList<Point> lTemp = dC.getlPuntos();
		//en la lista ya hay un pto en el 10, 10. voy a hacerlo en el 11,11. no deberia anyadirse
		dC.addPunto(new Point(11, 11));
		assertEquals(lTemp, dC.getlPuntos());	//en el 1 no deberia haber nada
		
		//PREGUNTAR EN CLASE: SI HAGO ASSERT NULL DE LA POSICION 1 DE LA LISTA DA ERROR, NO DEBERIA HABER ELEMENTO EN LA POSICION 1¿?¿?¿?¿?
		
	}

}
