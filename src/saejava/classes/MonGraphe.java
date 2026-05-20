package saejava.classes;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Scanner;

import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.generator.RandomGenerator;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

public class MonGraphe {
    
    private static ArrayList<String> lireCSV(String nomFichier) {
        ArrayList<String> lignes = new ArrayList<String>();
        
        try {
            FileInputStream file = new FileInputStream(nomFichier);
            Scanner scanner = new Scanner(file);
            

            while (scanner.hasNext()) {
                String ligne = scanner.nextLine();
                if (!ligne.equals("") && !ligne.startsWith("//")) {
                    lignes.add(ligne);
                }
            }
            scanner.close();
        }
        catch(Exception e) {
            e.printStackTrace() ;
        }
        
        return lignes;
    }

    private static ArrayList<Centre> creerCentres(ArrayList<String> lignes) {
        ArrayList<Centre> centres = new ArrayList<Centre>();

        for (int i = 0; i < lignes.size(); i++) {
            String[] ligne = lignes.get(i).split(";");
            Centre centre = new Centre(ligne[0], new TypeCentre(ligne[1].charAt(0)));
            centres.add(centre);
        }

        return centres;
    }

    private static ArrayList<Route> creerRoutes(ArrayList<String> lignes, ArrayList<Centre> centres) {
        ArrayList<Route> routes = new ArrayList<Route>();

        for (int i = 0; i < lignes.size(); i++) {
            String[] ligne = lignes.get(i).split(";");

            for (int j = i + 1; j < ligne.length - 2; j++) {
                if (ligne[j+2].compareTo("0") != 0) {
                    String[] infoRoute = ligne[j+2].split(",");
                    Route route = new Route(centres.get(i), centres.get(j), Integer.parseInt(infoRoute[0].trim()), Integer.parseInt(infoRoute[1].trim()), Integer.parseInt(infoRoute[2].trim()));
                    routes.add(route);
                }
            }
        }

        return routes;
    } 

    public static Graph chargerGraphe(String nomFichier) {
        ArrayList<String> lignes = lireCSV(nomFichier);
        ArrayList<Centre> centres = creerCentres(lignes);
        ArrayList<Route> routes = creerRoutes(lignes, centres);

        Graph graph = new SingleGraph("Graphe CSV");

        for (int i = 0; i < centres.size(); i++) {
            graph.addNode("" + centres.get(i).getId());
        }

        for (int i = 0; i < routes.size(); i++) {
		    graph.addEdge("" + routes.get(i).getDepart().getId() + routes.get(i).getArrivee().getId(), routes.get(i).getDepart().getId(), routes.get(i).getArrivee().getId());
        }

        return graph;
    }

    public static Graph createRandom(int N){
        Graph graph = new SingleGraph("G3");
        Generator gen = new RandomGenerator(3);//REGARDER QUOI METTRE EN AVG pour 
        gen.addSink(graph);
        gen.begin();
        for(int i=graph.getNodeCount(); i<N; i++) //ATTENTION AU DEBUT DU I
            gen.nextEvents();
        gen.end();
        
        return graph;
    }
}
