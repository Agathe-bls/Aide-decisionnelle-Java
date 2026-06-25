/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package saejava.classes;

import java.util.ArrayList;

import org.graphstream.graph.Node;

import Exceptions.NombreVehiculeInvalideException;

/**
 * Représente une tournée de livraison effectuée dans un graphe de centres.
 * Une tournée peut être réalisée avec un ou deux véhicules, chacun partant
 * d'un centre de départ donné.
 * 
 * La classe permet de calculer une tournée en choisissant progressivement
 * les centres à livrer, puis de récupérer la durée totale, le chemin suivi
 * et de colorier la tournée sur le graphe.
 *
 * @author Mattis
 */
public class Tournee {

    /**
     * Représente un véhicule utilisé dans une tournée.
     * Un véhicule possède une position actuelle, une liste de centres déjà livrés,
     * une liste de trajets effectués et une durée totale de déplacement.
     *
     * @author Mattis
     */
    private class Vehicule {
        public ArrayList<Node> centresLivres = new ArrayList<>();
        public ArrayList<Chemin> trajets = new ArrayList<>();
        public Node positionActuelle;
        public int dureeTotale;

        /**
         * Crée un véhicule à partir d'une position de départ.
         * La durée totale est initialisée à 0 et le véhicule commence au centre donné.
         *
         * @param positionDepart centre de départ du véhicule
         *
         * @author Mattis
         */
        public Vehicule(Node positionDepart) {
            this.positionActuelle = positionDepart;
            this.dureeTotale = 0;
        }
    }
    private MonGraphe graphe;
    private ArrayList<Vehicule> lstVehicules = new ArrayList<>();

    /**
     * Crée une tournée associée à un graphe, à un centre de départ et à un nombre
     * de véhicules.
     * 
     * La tournée accepte uniquement un ou deux véhicules. Tous les véhicules créés
     * commencent au même centre de départ.
     *
     * @param graphe graphe dans lequel la tournée est calculée
     * @param positionDepart centre de départ des véhicules
     * @param nbVehicule nombre de véhicules utilisés pour la tournée
     *
     * @throws NombreVehiculeInvalideException si le nombre de véhicules est différent de 1 ou 2
     *
     * @author Mattis
     */
    public Tournee(MonGraphe graphe, Node positionDepart, int nbVehicule) throws NombreVehiculeInvalideException {
    if (nbVehicule != 1 && nbVehicule != 2) {
        throw new NombreVehiculeInvalideException();
    }

    this.graphe = graphe;
    this.lstVehicules = new ArrayList<>();

    if (nbVehicule == 1) {
        this.lstVehicules.add(new Vehicule(positionDepart));
    } else {
        this.lstVehicules.add(new Vehicule(positionDepart));
        this.lstVehicules.add(new Vehicule(positionDepart));
    }
}
    
    /**
     * Retourne une chaîne de caractères décrivant les centres livrés par un véhicule.
     * Les centres sont affichés dans l'ordre dans lequel ils ont été visités.
     *
     * @param vehiculeChoisi indice du véhicule dont on veut récupérer le trajet
     *
     * @return une chaîne décrivant les centres livrés par le véhicule
     *
     * @author Mattis
     */
    public String getLabelCentresLivres(int vehiculeChoisi) {
        String resultat = "Chemin pris :";
        if (lstVehicules.get(vehiculeChoisi).trajets.isEmpty()) {
            resultat += "aucun trajet calculé.";
        }
        else {
            for (Node n : lstVehicules.get(vehiculeChoisi).centresLivres) {                
                if (lstVehicules.get(0) == n)
                    resultat = n.getId();
                else 
                    resultat += n.getId() + " > ";
            }
        }
        return resultat;
    }

    /**
     * Retourne la durée totale de la tournée d'un véhicule.
     *
     * @param vehiculeChoisi indice du véhicule concerné
     *
     * @return durée totale de la tournée du véhicule en minutes
     *
     * @author Mattis
     */
    public int getDureeTotale(int vehiculeChoisi) {
        return lstVehicules.get(vehiculeChoisi).dureeTotale;
    }

    /**
     * Retourne la liste des centres à livrer selon un type donné.
     * 
     * Si le type vaut null, tous les centres du graphe sont ajoutés à la liste.
     * Sinon, seuls les centres correspondant au type demandé sont ajoutés.
     *
     * @param type type des centres à livrer : "M", "O", "N" ou null pour tous les types
     *
     * @return liste des centres à livrer
     *
     * @author Mattis
     */
    public ArrayList<Node> getCentresALivrer(String type) { // pour info on mettra null en parametre quand on veut tous les type de centre c plus simple
        ArrayList<Node> centres = new ArrayList<>();

        for (Node centre : graphe.getEachNode()) {
            String typeCentre = centre.getAttribute("type");

            if (type == null || type.equals(typeCentre)) {
                centres.add(centre);
            }
        }
        return centres;
    }

    /**
     * Recherche le centre non livré le plus proche de la position actuelle.
     * 
     * La comparaison se fait avec la durée minimale calculée par l'algorithme
     * de Dijkstra entre la position actuelle et chaque centre non livré.
     *
     * @param centresPasLivres liste des centres qui n'ont pas encore été livrés
     * @param positionActuelle position actuelle du véhicule
     *
     * @return le centre le plus proche en durée, ou null si la liste est vide
     *
     * @author Mattis
     */
    private Node trouverCentrePlusProche(ArrayList<Node> centresPasLivres, Node positionActuelle) {
        if (centresPasLivres.isEmpty()) {
            return null;
        }

        Node meilleureCentreActuelle = centresPasLivres.get(0);
        int meilleureDuree = new Chemin(graphe).cheminDureeMinDijkstra(positionActuelle, meilleureCentreActuelle).getDureeTotale();

        for (Node centre : centresPasLivres) {
            if (!centre.equals(positionActuelle)) {
                Chemin chemin = new Chemin(graphe);
                chemin.cheminDureeMinDijkstra(positionActuelle, centre);

                if (chemin.getDureeTotale() < meilleureDuree) {
                    meilleureDuree = chemin.getDureeTotale();
                    meilleureCentreActuelle = centre;
                }
            }
        }
        return meilleureCentreActuelle;
    }

    /**
     * Vérifie que le type de centre demandé est valide.
     * 
     * Les types acceptés sont "M" pour maternité, "O" pour bloc opératoire,
     * "N" pour nutrition, ou null pour sélectionner tous les types.
     *
     * @param type type de centre à vérifier
     *
     * @throws IllegalArgumentException si le type est différent de "M", "O", "N" ou null
     *
     * @author Mattis
     */
    private void verifierTypeCentre(String type) {
        if (type != null && !type.equals("M") && !type.equals("O") && !type.equals("N")) {
            throw new IllegalArgumentException("Type de centre invalide : " + type);
        }
    }

    /**
     * Calcule une tournée avec un seul véhicule.
     * 
     * Le véhicule part de sa position initiale, puis choisit à chaque étape le
     * centre non livré le plus proche en durée. La tournée peut concerner tous
     * les centres ou seulement un type de centre précis.
     *
     * @param type type des centres à livrer : "M", "O", "N" ou null pour tous les types
     *
     * @throws IllegalStateException si aucun centre accessible ne peut être trouvé
     *
     * @author Mattis
     */
    public void calculerTourneeUnVehicule(String type) {
        verifierTypeCentre(type);

        ArrayList<Node> centresPasLivres = getCentresALivrer(type);
        Node positionActuelle = lstVehicules.get(0).positionActuelle;

        lstVehicules.get(0).centresLivres.add(positionActuelle);

        centresPasLivres.remove(positionActuelle);

        while(!centresPasLivres.isEmpty()) {
            Node centrePlusProche = trouverCentrePlusProche(centresPasLivres, positionActuelle);

            if (centrePlusProche == null) {
                throw new IllegalStateException("Aucun centre accessible depuis " + positionActuelle.getId());
            }

            Chemin trajet = new Chemin(graphe);
            trajet.cheminDureeMinDijkstra(positionActuelle, centrePlusProche);
            lstVehicules.get(0).trajets.add(trajet);
            lstVehicules.get(0).centresLivres.add(centrePlusProche);
            lstVehicules.get(0).dureeTotale += trajet.getDureeTotale();
            positionActuelle = centrePlusProche;
            centresPasLivres.remove(centrePlusProche);
        }
    }

    /**
     * Calcule une tournée avec deux véhicules.
     * 
     * Les deux véhicules partent du même centre de départ. Les centres à livrer
     * sont répartis progressivement entre les deux véhicules en alternant le
     * véhicule utilisé. Chaque véhicule choisit le centre non livré le plus proche
     * depuis sa position actuelle.
     *
     * @throws IllegalStateException si aucun centre accessible ne peut être trouvé
     *
     * @author Mattis
     */
    public void calculerTourneeDeuxVehicules() {
        ArrayList<Node> centresPasLivres = getCentresALivrer(null);
        int vehiculeChoisi = 0;
        
        ArrayList<Node> positionActuelle = new ArrayList<>();
        positionActuelle.add(lstVehicules.get(0).positionActuelle);
        positionActuelle.add(lstVehicules.get(1).positionActuelle);

        lstVehicules.get(0).centresLivres.add(positionActuelle.get(vehiculeChoisi));
        lstVehicules.get(1).centresLivres.add(positionActuelle.get(vehiculeChoisi));
        centresPasLivres.remove(positionActuelle.get(vehiculeChoisi));

        while(!centresPasLivres.isEmpty()) {
            Node centrePlusProche = trouverCentrePlusProche(centresPasLivres, positionActuelle.get(vehiculeChoisi));

            if (centrePlusProche == null) {
                throw new IllegalStateException("Aucun centre accessible depuis " + positionActuelle.get(vehiculeChoisi).getId());
            }

            Chemin trajet = new Chemin(graphe);
            trajet.cheminDureeMinDijkstra(positionActuelle.get(vehiculeChoisi), centrePlusProche);
            lstVehicules.get(vehiculeChoisi).trajets.add(trajet);
            lstVehicules.get(vehiculeChoisi).centresLivres.add(centrePlusProche);
            lstVehicules.get(vehiculeChoisi).dureeTotale += trajet.getDureeTotale();
            positionActuelle.set(vehiculeChoisi, centrePlusProche);
            centresPasLivres.remove(centrePlusProche);

            if (vehiculeChoisi == 0)
                vehiculeChoisi = 1;
            else
                vehiculeChoisi = 0;
        }
    }

    /**
     * Colore graphiquement la tournée d'un véhicule sur le graphe.
     * 
     * Chaque chemin emprunté par le véhicule est visualisé avec la méthode
     * de visualisation prévue dans la classe Chemin.
     *
     * @param vehiculeChoisi indice du véhicule dont la tournée doit être coloriée
     *
     * @author Mattis
     */
    public void colorierTourneeUnVehicule(int vehiculeChoisi) {
        for (Chemin chemin : lstVehicules.get(vehiculeChoisi).trajets) {
            chemin.visualisationTournee(vehiculeChoisi+1);
        }
    }

    /**
     * Affiche dans la console les informations de la tournée d'un véhicule.
     * 
     * Cette méthode sert principalement au débogage. Elle affiche les centres
     * livrés ainsi que la durée totale de la tournée du véhicule choisi.
     *
     * @param vehiculeChosi indice du véhicule dont on veut afficher la tournée
     *
     * @author Mattis
     */
    public void afficherTournee(int vehiculeChosi) {
        System.out.println("===== Tournee du véhicule " + vehiculeChosi + " =====");

        if (lstVehicules.get(vehiculeChosi).trajets.isEmpty()) {
            System.out.println("Aucun trajet calculé.");
        }

        else {
            System.out.println("Centres livrés :");

            for (Node n : lstVehicules.get(vehiculeChosi).centresLivres) {
                System.out.print(n.getId()+";");
            }

            System.out.println("\nDurée totale : " + lstVehicules.get(vehiculeChosi).dureeTotale + " min");
        }
    }
}
