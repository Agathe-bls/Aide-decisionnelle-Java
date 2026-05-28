/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package saejava.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;

/**
 *
 * @author Agathe
 */
public class Chemin {
    private MonGraphe grapheAssocie;
            
    private double fiabiliteTotale;
    private int distanceTotale;
    private int dureeTotale;
    private ArrayList<Node> centresTraverses;

    public Chemin(MonGraphe grapheAssocie) {
        this.grapheAssocie = grapheAssocie;
        this.fiabiliteTotale = 0.0;
        this.distanceTotale = 0;
        this.dureeTotale = 0;
        this.centresTraverses = new ArrayList<>();
    }
    
    //NORMALEMENT IL Y A PAS DE PROBLEME MAIS IL FAUT PEUT ETRE METTRE UNE EXCEPTION POUR ETRE SUR QUE DEPART/GRAPH/ARRIVEE VIENNENT BIEN DU MËME GRAPHE
    
    //plus sur et plus rapide => duréeestimée = durée*(20-fiabilité)/10
    public Chemin cheminFiableDijskra(Node depart, Node arrivee){
        this.fiabiliteTotale = 0.0;
        this.distanceTotale = 0;
        this.dureeTotale = 0;
        this.centresTraverses.clear();
        
        HashMap<Node, ArrayList> infosTableauDijkstra = new HashMap<>();
        
        initTableauDijkstra(infosTableauDijkstra, depart);
        
        //regarder pour chaque centre un par un en prenant celui avec la plus petite fiabilité si en le rajoutant aux chemin il permet de diminuer sa fiabilité
        while(!finTraitement(infosTableauDijkstra)){
            Node nTraite = centreAValeurMin(infosTableauDijkstra);
            double fiaActuel = (double)(infosTableauDijkstra.get(nTraite).get(0));
            
            Iterator<Node> itVoisins = nTraite.getNeighborNodeIterator();
            while(itVoisins.hasNext()){
                Node voisin = itVoisins.next();
                if((boolean)(infosTableauDijkstra.get(voisin).get(2)) == false){
                    double fiaAvecVoisin = fiaActuel + (double)(voisin.getEdgeBetween(nTraite).getAttribute("duree"))*(20 - (double)(voisin.getEdgeBetween(nTraite).getAttribute("fiabilite")))/10;
                    if(fiaAvecVoisin < (double)(infosTableauDijkstra.get(voisin).get(0))){
                        infosTableauDijkstra.get(voisin).set(0, fiaAvecVoisin);
                        infosTableauDijkstra.get(voisin).set(1, nTraite);
                    }
                }
            }
            infosTableauDijkstra.get(nTraite).set(2, true);
        }
        
        //en partant du centre d'arrivée trouver toutes les informations pour avoir les infos du chemin final
        Node debut = arrivee;
        this.fiabiliteTotale = (double)(infosTableauDijkstra.get(debut).get(0));
        while(debut != depart){
            Edge entreDeux = debut.getEdgeBetween((Node)infosTableauDijkstra.get(debut).get(1));//FAIRE ATTENTION ICI IL FAUT QUE JE VERIFIE CE QUE J4AI FAIS
            this.centresTraverses.add(debut);
            this.distanceTotale += (Integer)(entreDeux.getAttribute("distance"));
            this.dureeTotale += (Integer)(entreDeux.getAttribute("duree"));

            debut = (Node)(infosTableauDijkstra.get(debut).get(1)); //FAIRE ATTENTION ICI IL FAUT QUE JE VERIFIE CE QUE J4AI FAIS je suis vraiment obligee de cast ???
        }
        this.centresTraverses.add(arrivee);
            
        return this;
    }
    
    private void initTableauDijkstra(HashMap<Node, ArrayList> infosTableauDijkstra, Node depart){
        for(Node n : grapheAssocie.getEachNode()){
            ArrayList fia_prec_traite = new ArrayList();
            fia_prec_traite.add(0, 100.0);  // fiabilité du 
            fia_prec_traite.add(1, null);   // centre précédent dans l'algo de dijcktra
            fia_prec_traite.add(2, false);  // si le centre est traité
            if(n == depart){
                fia_prec_traite.set(0, 0.0);
                
                infosTableauDijkstra.put(n, fia_prec_traite);
            }
            else
                infosTableauDijkstra.put(n, fia_prec_traite);
        }
    }
    
    private boolean finTraitement(HashMap<Node, ArrayList> infosTableauDijkstra){
        boolean traitementFini = true;
        for(Node n : infosTableauDijkstra.keySet()){
            if((boolean)(infosTableauDijkstra.get(n).get(2)) == false){
                traitementFini = false;
            }
        }
        return traitementFini;
    }
    
    //FAIRE ATTENTION AVEC LES EXCEPTIONS IL FAUT PAS QUE LA LISTE DE SOMMETS SOIT VIDE
    private Node centreAValeurMin(HashMap<Node, ArrayList> infosTableauDijkstra){
        double valMin = 2.0;
        Node centreMin = null;
        for(Node n : infosTableauDijkstra.keySet()){
            if((double)(infosTableauDijkstra.get(n).get(0))< valMin && (boolean)(infosTableauDijkstra.get(n).get(2)) == false){
                valMin = (double)(infosTableauDijkstra.get(n).get(0));
                centreMin = n;
            }
        }
        return centreMin;
    }
}
