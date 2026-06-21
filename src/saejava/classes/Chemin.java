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

    private final MonGraphe grapheAssocie;
    private double fiabiliteTotale;
    private int distanceTotale;
    private int dureeTotale;
    private int dureeEstimee;    //avec la fiabilité
    private ArrayList<Node> centresTraverses;
    private ArrayList<Edge> routesTraversees;

    /**
     * Construit un chemin associé à un graphe donné. Initialise les différentes
     * caractéristiques du chemin.
     *
     * @param grapheAssocie graphe sur lequel les calculs des chemins seront
     * effectués
     *
     * @author Balsan Agathe
     */
    public Chemin(MonGraphe grapheAssocie) {
        this.grapheAssocie = grapheAssocie;
        this.fiabiliteTotale = 0.0;
        this.distanceTotale = 0;
        this.dureeTotale = 0;
        this.centresTraverses = new ArrayList<>();
        this.routesTraversees = new ArrayList<>();
    }

    /**
     * Retourne la distance totale en km du chemin.
     *
     * @return la distance totale parcourue
     *
     * @author Balsan Agathe
     */
    public int getDistanceTotale() {
        return distanceTotale;
    }

    /**
     * Retourne la durée totale en minutes du chemin.
     *
     * @return la durée totale du trajet
     *
     * @author Balsan Agathe
     */
    public int getDureeTotale() {
        return dureeTotale;
    }

    /**
     * Retourne la durée estimée en minutes du chemin.
     *
     * @return la durée estimée du trajet
     *
     * @author Balsan Agathe
     */

    public double getDureeEstimee() {
        return dureeEstimee;
    }

    /**
     * Retourne la liste des centres traversés durant le chemin sous forme textuelle avec les identifiants des centres.
     *
     * @return une chaîne décrivant l'ordre des centres parcourus
     *
     * @author Balsan Agathe
     */
    public String getCentresTraverses() {
        String resultat = "";
        for (Node n : centresTraverses) {
            if (centresTraverses.get(0) == n) {
                resultat = n.getId();
            } else {
                resultat = n.getId() + " > " + resultat;
            }
        }
        resultat = "Chemin pris : " + resultat;
        return resultat;
    }

//    @Override
//    public String toString() {
//        return "Chemin [distanceTotale=" + distanceTotale + ", dureeTotale=" + dureeTotale + ", dureeEstimee="
//                + dureeEstimee + "]";
//    }
    
    /**
     * Calcule le chemin de durée minimale entre deux centres à l'aide de l'algorithme de Dijkstra.
     *
     * @param depart centre de départ du chemin
     * @param arrivee centre d'arrivée du chemin
     *
     * @return le chemin correspondant à la durée minimale du chemin en minutes
     *
     * @author Balsan Agathe
     */
    public Chemin cheminDureeMinDijkstra(Node depart, Node arrivee) {
        HashMap<Node, InfosCentresDijkstra> infosTableauDijkstra = initAlgoDijkstra(depart);

        //regarder pour chaque centre non traité un par un en prenant celui avec la plus petite durée Estimée si en le rajoutant aux chemin il permet de diminuer sa durée Estimée
        while (!finTraitement(infosTableauDijkstra)) {
            Node nTraite = centreAValeurMin(infosTableauDijkstra);
            int dureeActuel = (int) (infosTableauDijkstra.get(nTraite).coutCalcule);

            Iterator<Node> itVoisins = nTraite.getNeighborNodeIterator();
            while (itVoisins.hasNext()) {
                Node voisin = itVoisins.next();
                if ((infosTableauDijkstra.get(voisin).estTraite) == false) {  // NORMALEMENT ON MET PAS CA : && 0.25 <= (Integer) voisin.getAttribute("fiabilite")
                    int dureeAvecVoisin = dureeActuel + (int) (voisin.getEdgeBetween(nTraite).getAttribute("duree"));
                    if (dureeAvecVoisin < (infosTableauDijkstra.get(voisin).coutCalcule)) {
                        infosTableauDijkstra.get(voisin).coutCalcule = dureeAvecVoisin;
                        infosTableauDijkstra.get(voisin).centrePrecedent = nTraite;
                    }
                }
            }
            infosTableauDijkstra.get(nTraite).estTraite = true;
        }

        //en partant du centre d'arrivée trouver toutes les informations pour avoir les infos du chemin final
        Node debut = arrivee;
        this.dureeTotale = (int) (infosTableauDijkstra.get(debut).coutCalcule);
        while (debut != depart) {
            Edge entreDeux = debut.getEdgeBetween(infosTableauDijkstra.get(debut).centrePrecedent);
            this.centresTraverses.add(debut);
            this.routesTraversees.add(entreDeux);
            this.distanceTotale += (Integer) (entreDeux.getAttribute("distance"));
            this.fiabiliteTotale += (Double) (entreDeux.getAttribute("fiabilite"));
            this.dureeEstimee += ((int) (entreDeux.getAttribute("duree")) * (20 - (double) (entreDeux.getAttribute("fiabilite"))) / 10);

            debut = (infosTableauDijkstra.get(debut).centrePrecedent);
        }
        this.centresTraverses.add(depart);

        return this;
    }

    /**
     * Calcule le chemin minimisant la durée estimée avec les fiabilités des routes entre deux centres à l'aide
     * de l'algorithme de Dijkstra.
     * La durée estimée étant égale à : durée * (20-fiabilité)/10
     *
     * @param depart centre de départ
     * @param arrivee centre d'arrivée
     *
     * @return le chemin correspondant à la durée estimée minimale
     *
     * @author Balsan Agathe
     */
    public Chemin cheminDureeEstimeeDijkstra(Node depart, Node arrivee) {
        HashMap<Node, InfosCentresDijkstra> infosTableauDijkstra = initAlgoDijkstra(depart);

        //regarder pour chaque centre non traité un par un en prenant celui avec la plus petite durée Estimée si en le rajoutant aux chemin il permet de diminuer sa durée Estimée
        while (!finTraitement(infosTableauDijkstra)) {
            Node nTraite = centreAValeurMin(infosTableauDijkstra);
            double EstimationActuel = (infosTableauDijkstra.get(nTraite).coutCalcule);

            Iterator<Node> itVoisins = nTraite.getNeighborNodeIterator();
            while (itVoisins.hasNext()) {
                Node voisin = itVoisins.next();
                if ((infosTableauDijkstra.get(voisin).estTraite) == false && 0.25 <= (double) voisin.getEdgeBetween(nTraite).getAttribute("fiabilite")) {
                    double EstimationAvecVoisin = EstimationActuel + (int) (voisin.getEdgeBetween(nTraite).getAttribute("duree")) * (20 - (double) (voisin.getEdgeBetween(nTraite).getAttribute("fiabilite"))) / 10;
                    if (EstimationAvecVoisin < (infosTableauDijkstra.get(voisin).coutCalcule)) {
                        infosTableauDijkstra.get(voisin).coutCalcule = EstimationAvecVoisin;
                        infosTableauDijkstra.get(voisin).centrePrecedent = nTraite;
                    }
                }
            }
            infosTableauDijkstra.get(nTraite).estTraite = true;
        }
        //en partant du centre d'arrivée trouver toutes les informations pour avoir les infos du chemin final
        Node debut = arrivee;
        this.dureeEstimee = ((int) (infosTableauDijkstra.get(debut).coutCalcule));
        while (debut != depart) {
            Edge entreDeux = debut.getEdgeBetween(infosTableauDijkstra.get(debut).centrePrecedent);
            this.centresTraverses.add(debut);
            this.routesTraversees.add(entreDeux);
            this.distanceTotale += (Integer) (entreDeux.getAttribute("distance"));
            this.dureeTotale += (Integer) (entreDeux.getAttribute("duree"));
            this.fiabiliteTotale += (Double) (entreDeux.getAttribute("fiabilite"));

            debut = (infosTableauDijkstra.get(debut).centrePrecedent);
        }
        this.fiabiliteTotale = this.fiabiliteTotale / (this.centresTraverses.size() - 1);
        this.centresTraverses.add(depart);

        return this;
    }

//FONCTIONS SECONDAIRES UTILISEES POUR L'ALGO :
    
    public class InfosCentresDijkstra {

        private double coutCalcule;
        private Node centrePrecedent;
        private boolean estTraite;

        /**
         * Construit une structure de données utilisée par l'algorithme de
         * Dijkstra pour stocker les informations associées à un centre.
         * Le cout, le centre qui le précède pour y arriver et s'il est traité ou non.
         *
         * @author Balsan Agathe
         */
        public InfosCentresDijkstra() {
            this.coutCalcule = Double.POSITIVE_INFINITY;
            this.centrePrecedent = null;
            this.estTraite = false;
        }
    }

    private HashMap<Node, InfosCentresDijkstra> initAlgoDijkstra(Node depart) {
        this.fiabiliteTotale = 0.0;
        this.distanceTotale = 0;
        this.dureeTotale = 0;
        this.dureeEstimee = 0;
        this.centresTraverses.clear();
        this.routesTraversees.clear();

        HashMap<Node, InfosCentresDijkstra> infosTableauDijkstra = new HashMap<>();

        for (Node n : grapheAssocie.getEachNode()) {
            InfosCentresDijkstra infosN = new InfosCentresDijkstra();
            if (n == depart) {
                infosN.coutCalcule = 0.0;
            }
            infosTableauDijkstra.put(n, infosN);
        }
        return infosTableauDijkstra;
    }

    private boolean finTraitement(HashMap<Node, InfosCentresDijkstra> infosTableauDijkstra) {
        boolean traitementFini = true;
        for (Node n : infosTableauDijkstra.keySet()) {
            if ((infosTableauDijkstra.get(n).estTraite) == false) {
                traitementFini = false;
            }
        }
        return traitementFini;
    }

    //FAIRE ATTENTION AVEC LES EXCEPTIONS IL FAUT PAS QUE LA LISTE DE SOMMETS SOIT VIDE
    private Node centreAValeurMin(HashMap<Node, InfosCentresDijkstra> infosTableauDijkstra) {
        double valMin = Double.POSITIVE_INFINITY;
        Node centreMin = null;
        for (Node n : infosTableauDijkstra.keySet()) {
            if ((infosTableauDijkstra.get(n).coutCalcule) <= valMin && (infosTableauDijkstra.get(n).estTraite) == false) {
                valMin = (infosTableauDijkstra.get(n).coutCalcule);
                centreMin = n;
            }
        }
        return centreMin;
    }

//VISUALISATION : 
    /**
     * Met en évidence graphiquement le chemin calculé selon le type de chemin sélectionné.
     *
     * @param typeChemin type du chemin à visualiser ("dureeMin" ou "dureeEstimee")
     *
     * @author Balsan Agathe
     */
    public void visualisationChemin(String typeChemin) {
        if (typeChemin.equals("dureeMin")) {
            for (Edge e : routesTraversees) {
                e.setAttribute("ui.style", "fill-color: RED; size: 3px;");
            }
        } else if (typeChemin.equals("dureeEstimee")) {
            for (Edge e : routesTraversees) {
                e.setAttribute("ui.style", "fill-color: #07b817; size: 3px; shape : cubic-curve; arrow-shape : arrow;");
            }
        }
    }

    /**
     * Met en évidence graphiquement une tournée en fonction du véhicule sélectionné.
     *
     * @param nbVehicule numéro du véhicule dont la tournée doit être visualisée (soit le 1 soit le 2)
     *
     * @author Balan Agathe
     */
    public void visualisationTournee(int nbVehicule) {
        switch (nbVehicule) {
            case 1:
                for (Edge e : routesTraversees) {
                    e.setAttribute("ui.style", "fill-color: black; size: 3px;");
                }
                centresTraverses.get(0).addAttribute("ui.label", "" + centresTraverses.get(0).getId());
                centresTraverses.get(0).addAttribute("ui.style", "text-size: 20px; text-color: black;");
                break;
            case 2:
                for (Edge e : routesTraversees) {
                    e.setAttribute("ui.style", "fill-color: #b4083e; size: 3px;");
                }
                centresTraverses.get(0).addAttribute("ui.label", "" + centresTraverses.get(0).getId());
                centresTraverses.get(0).addAttribute("ui.style", "text-size: 20px;");
                break;

            default: //Faire une fenetre d'info pour dire qu'il n'y a pas assez de vehicules
                break;
        }
    }

    /**
     * Réinitialise l'affichage du graphe en supprimant les visualisations actuelles des chemins et tournées.
     *
     * @author Balsan Agathe
     */
    public void clearVisualisations() {
        grapheAssocie.visualisationGraphe();
        for (Edge e : grapheAssocie.getEachEdge()) {
            if (e.hasAttribute("ui.style")) {
                e.changeAttribute("ui.style", "fill-color: darck; size: 1px;");
            }
        }
    }
}
