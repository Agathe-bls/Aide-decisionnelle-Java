package saejava;

import Fenetre.MaFenetre;
import saejava.classes.MonGraphe;
import saejava.classes.Tournee;

public class Main {

    public static void main(String[] args) {
        MonGraphe g = new MonGraphe("test");
        g.chargerGraphe("graphe1.csv");
        //g.creerGrapheAleatoire(200);

        Tournee v = new Tournee(g, g.getNode("S1"), 2);

        // null = livrer tous les centres
        v.calculerTourneeDeuxVehicules();
        //v.calculerTourneeUnVehicule(null);

        v.afficherTournee(0);
        v.afficherTournee(1);

        v.colorierTourneeUnVehicule(0);
        v.colorierTourneeUnVehicule(1);
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
