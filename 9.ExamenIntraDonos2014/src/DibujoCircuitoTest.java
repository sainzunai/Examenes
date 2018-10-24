

import static org.junit.Assert.*;

import java.awt.Point;

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
		//en la lista ya hay un pto en el 10, 10. voy a hacerlo en el 11,11. no deberia anyadirse
		dC.addPunto(new Point(11, 11));
		assertNull(dC.getlPuntos().get(1));	//en el 1 no deberia haber nada
		fail("");
	}

}
