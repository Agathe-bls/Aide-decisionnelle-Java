package saejava;

import saejava.classes.MonGraphe;
import org.graphstream.graph.Graph;

public class Main {

    public static void main(String[] args) {
        System.out.println("Test");
        Graph gtest = MonGraphe.chargerGraphe("graphe2.csv");
        System.out.println("Sommets : " + gtest.getNodeCount());
        System.out.println("Routes : " + gtest.getEdgeCount());
        gtest.display();
    }
    
}
