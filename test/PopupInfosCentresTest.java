/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */

import Fenetre.MaFenetre;
import Fenetre.PopupInfosCentres;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import org.graphstream.graph.Node;
import saejava.classes.MonGraphe;

/**
 *
 * @author Agathe
 */
public class PopupInfosCentresTest {
    
    public PopupInfosCentresTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    
    @Test
    public void testGetTypeComplet() {
        MonGraphe graphe = new MonGraphe("test");

        Node n = graphe.addNode("M1");
        n.setAttribute("type", "M");

        assertEquals("Maternité", graphe.getTypeComplet(n));
    }
}