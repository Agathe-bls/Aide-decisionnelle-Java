/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */

import java.util.ArrayList;
import java.util.Iterator;
import org.graphstream.graph.Node;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import saejava.classes.MonGraphe;

/**
 *
 * @author Agathe
 */
public class MonGrapheTest {

    private static MonGraphe graphe;

    public MonGrapheTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        graphe = new MonGraphe("gTest");
    }

    @After
    public void tearDown() {
    }

    @Test
    public void GetEstVideTest() {
        assertTrue(graphe.getEstVide());
    }

    @Test
    public void getTypeCompletTest() {
        graphe.addNode("S1").setAttribute("type", "M");
        graphe.addNode("S2").setAttribute("type", "N");
        graphe.addNode("S3").setAttribute("type", "O");

        assertEquals("Nutrition", graphe.getTypeComplet(graphe.getNode("S2")));
        assertEquals("Maternité", graphe.getTypeComplet(graphe.getNode("S1")));
        assertEquals("Bloc opératoire", graphe.getTypeComplet(graphe.getNode("S3")));

    }

    @Test
    public void GetTypeCompletExceptionTest() {
        graphe.addNode("S1").setAttribute("type", "X");

        try {
            graphe.getTypeComplet(graphe.getNode("S1"));
            fail("Aurait du lever une exception");
        } catch (IllegalArgumentException e) {
            //L'exception attendue est bien levée
        }
    }

    @Test
    public void CreerGrapheAleaNombreNoeudsTest() {
        graphe.creerGrapheAlea(20);

        assertEquals(20, graphe.getNodeCount());
    }

    @Test
    public void CreerGrapheAleaPlusVideTest() {
        graphe.creerGrapheAlea(10);

        assertFalse(graphe.getEstVide());
    }

    @Test
    public void CreerGrapheAleaConnexeTest() {

        for (int i = 0; i < 20; i++) {
            graphe.clear();
            graphe.creerGrapheAlea(50);

            ArrayList<Node> centresVisites = new ArrayList<>();
            ArrayList<Node> centresATraiter = new ArrayList<>();

            Node depart = graphe.getNode("S1");
            centresVisites.add(depart);
            centresATraiter.add(depart);

            while (!centresATraiter.isEmpty()) {
                Node centreActuel = centresATraiter.get(0);
                centresATraiter.remove(0);

                Iterator<Node> itVoisins = centreActuel.getNeighborNodeIterator();
                while (itVoisins.hasNext()) {
                    Node voisin = itVoisins.next();
                    if (!centresVisites.contains(voisin)) {
                        centresVisites.add(voisin);
                        centresATraiter.add(voisin);
                    }
                }
            }
            assertEquals(graphe.getNodeCount(), centresVisites.size());
        }
    }

    @Test
    public void visualisationGrapheTest() {
        graphe.addNode("S1");
        graphe.visualisationGraphe();

        assertEquals("S1", graphe.getNode("S1").getAttribute("ui.label"));
    }
}
