package saejava;

import saejava.classes.MonGraphe;
import org.graphstream.graph.Graph;

public class Main {

    public static void main(String[] args) {
        System.out.println("Neuille");
        Graph gtest = MonGraphe.createRandom(30);
        gtest.display();
    }
    
}
