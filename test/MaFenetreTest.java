/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */

import Fenetre.MaFenetre;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
<<<<<<< HEAD

=======
>>>>>>> f7cf99dc351c3d91201fd31a6cfddd2b5dcd4d42
import saejava.classes.MonGraphe;

/**
 *
 * @author Agathe
 */
public class MaFenetreTest {
    private MaFenetre f;
    private MonGraphe grapheActu;
    
    public MaFenetreTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        f = new MaFenetre();
        grapheActu = f.getGrapheActuel();

        grapheActu.addNode("S1").setAttribute("type", "N");

        grapheActu.addNode("S2").setAttribute("type", "M");
        grapheActu.addNode("S3").setAttribute("type", "M");

        grapheActu.addNode("S4").setAttribute("type", "O");
        grapheActu.addNode("S5").setAttribute("type", "O");
        grapheActu.addNode("S6").setAttribute("type", "O");
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void nbCentresParTypeTest(){
        int[] nbTypes = f.nbCentresParType();
        
        assertEquals(nbTypes[0], 1);
        assertEquals(nbTypes[1], 2);
        assertEquals(nbTypes[2], 3);
    }
}
