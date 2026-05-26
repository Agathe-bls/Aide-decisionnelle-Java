package saejava;

import saejava.classes.MonGraphe;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

public class Main {

    public static void main(String[] args) {
        System.out.println("Test");
        MonGraphe gtest = new MonGraphe("ffc");
        gtest.chargerGraphe("graphe3.csv");
        System.out.println("Sommets : " + gtest.getNodeCount());
        System.out.println("Routes : " + gtest.getEdgeCount());
        gtest.display();

//        MonGraphe gtest = new MonGraphe("gAleatoire");
//        gtest.creerGrapheAleatoire(20);
//        gtest.display();
    }
    
}
