package saejava;

import saejava.classes.MonGraphe;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import saejava.classes.Chemin;

public class Main {

    public static void main(String[] args) {
//        System.out.println("Test");
//        MonGraphe gtest = new MonGraphe("ffc");
//        gtest.chargerGraphe("graphe2.csv");
//        System.out.println("Sommets : " + gtest.getNodeCount());
//        System.out.println("Routes : " + gtest.getEdgeCount());
//        
//        gtest.display();

        MonGraphe gtest = new MonGraphe("gAleatoire");
        gtest.creerGrapheAleatoire(20);
        
        gtest.visualisationGraphe();
        //Chemin chDuree = new Chemin(gtest);
        Chemin chAvecFia = new Chemin(gtest);
        Chemin chDuree = new Chemin(gtest);
        
        chAvecFia.cheminDureeMinDijkstra(gtest.getNode("S5"), gtest.getNode("S10"));
        chAvecFia.visualisationChemin("dureeMin");
        
        chDuree.cheminDureeEstimeeDijkstra(gtest.getNode("S1"), gtest.getNode("S10"));
        chDuree.visualisationChemin("dureeEstimee");
        
        gtest.display();
        
        
    }
    
}
