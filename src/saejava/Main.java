package saejava;

import saejava.classes.MonGraphe;
import saejava.classes.Vehicule;

public class Main {

    public static void main(String[] args) {
        MonGraphe g = new MonGraphe("test");
        g.chargerGraphe("graphe2.csv");

        Vehicule v = new Vehicule(g, g.getNode("S1"));

        // null = livrer tous les centres
        v.calculerTourneeUnVehicule(null);

        v.afficherTournee();

        v.colorierTournee();
        g.display();



        //testerCSV("graphe4.csv");
        //testerCSV("graphe5.csv");


        // MonGraphe gtest = new MonGraphe("gAleatoire");
        //gtest.creerGrapheAleatoire(20);
        
        //Chemin chDuree = new Chemin(gtest);
        //Chemin chAvecFia = new Chemin(gtest);
        //Chemin chDuree = new Chemin(gtest);
        
        //chAvecFia.cheminDureeMinDijkstra(gtest.getNode("S5"), gtest.getNode("S10"));
        //chAvecFia.visualisationChemin("dureeMin");
        
        //chDuree.cheminDureeEstimeeDijkstra(gtest.getNode("S1"), gtest.getNode("S10"));
        //chDuree.visualisationChemin("dureeEstimee");
        
        //gtest.display();
        
        
    }

    private static void testerCSV(String fichier) {
        System.out.println("\nTest du fichier : " + fichier);

        MonGraphe g = new MonGraphe("test-" + fichier);
        g.chargerGraphe(fichier);
        g.afficherInfoGraphe();
    }
    
}
