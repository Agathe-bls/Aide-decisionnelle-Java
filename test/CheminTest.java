/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import saejava.classes.Chemin;
import saejava.classes.MonGraphe;

/**
 *
 * @author Agathe
 */
public class CheminTest {
    private MonGraphe graphe;
    private Chemin chemin;
    
    @Before
    public void setUp() {
         graphe = new MonGraphe("test");
         chemin = new Chemin(graphe);

        Node s1 = graphe.addNode("S1");
        Node s2 = graphe.addNode("S2");
        Node s3 = graphe.addNode("S3");
        
        s1.setAttribute("type", "M");
        s2.setAttribute("type", "N");
        s3.setAttribute("type", "N");

        Edge e1_2 = graphe.addEdge("1/2", s1, s2);

        e1_2.setAttribute("distance", 20);
        e1_2.setAttribute("duree", 10);
        e1_2.setAttribute("fiabilite", 0.8);
        
        Edge e1_3 = graphe.addEdge("1/3", s1, s3);

        e1_3.setAttribute("distance", 15);
        e1_3.setAttribute("duree", 5);
        e1_3.setAttribute("fiabilite", 0.7);
        
        Edge e3_2 = graphe.addEdge("3/2", s3, s2);

        e3_2.setAttribute("distance", 13);
        e3_2.setAttribute("duree", 3);
        e3_2.setAttribute("fiabilite", 0.6);
    }
    
    @Test
    public void cheminDureeMinDijkstraTest(){
        chemin.cheminDureeMinDijkstra(graphe.getNode("S1"), graphe.getNode("S2"));
        
        assertEquals(8, chemin.getDureeTotale());
    }
    
    @Test
    public void getDistanceTotaleTest() {
        chemin.cheminDureeMinDijkstra(graphe.getNode("S1"), graphe.getNode("S2"));

        assertEquals(28, chemin.getDistanceTotale());
    }
    
    @Test
    public void cheminDureeEstimeeDijkstra() {
        chemin.cheminDureeEstimeeDijkstra(graphe.getNode("S1"), graphe.getNode("S2"));

        assertEquals((int) Math.round(5*(20-0.7)/10 + 3*(20-0.6)/10), chemin.getDureeEstimee(), 0.01);
    }

    @Test
    public void GetCentresTraversesTest() {
        chemin.cheminDureeMinDijkstra(graphe.getNode("S1"), graphe.getNode("S2"));

        assertTrue(
            chemin.getCentresTraverses().contains("S1")
        );
        
        assertTrue(
            chemin.getCentresTraverses().contains("S3")
        );

        assertTrue(
            chemin.getCentresTraverses().contains("S2")
        );
    }
}
