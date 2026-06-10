package saejava.classes;

import static java.awt.Color.lightGray;

import java.util.ArrayList;

import org.graphstream.graph.Node;

public class Tournee {
    private class Vehicule {
        public ArrayList<Node> centresLivres = new ArrayList<>();
        public ArrayList<Chemin> trajets = new ArrayList<>();
        public Node positionActuelle;
        public int dureeTotale;
        public Vehicule(Node positionDepart) {
            this.positionActuelle = positionDepart;
            this.dureeTotale = 0;
        }
    }
    private MonGraphe graphe;
    private ArrayList<Vehicule> lstVehicules = new ArrayList<>();

    public Tournee(MonGraphe graphe, Node positionDepart, int nbVehicule) {
        this.graphe = graphe;
        if (nbVehicule == 1)
            this.lstVehicules.add(new Vehicule(positionDepart));
        else
            this.lstVehicules.add(new Vehicule(positionDepart));
            this.lstVehicules.add(new Vehicule(positionDepart));
    }

    public Node getPositionActuelle(Vehicule vehicule) {
        return vehicule.positionActuelle;
    }

    public ArrayList<Node> getCentresLivres(Vehicule v) {
        return v.centresLivres;
    }

    public ArrayList<Chemin> getTrajets(Vehicule v) {
        return v.trajets;
    }

    public int getDureeTotale(int vehiculeChoisi) {
        return lstVehicules.get(vehiculeChoisi).dureeTotale;
    }

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

    public void calculerTourneeUnVehicule(String type) {
        ArrayList<Node> centresPasLivres = getCentresALivrer(type);
        Node positionActuelle = lstVehicules.get(0).positionActuelle;

        lstVehicules.get(0).centresLivres.add(positionActuelle);
        centresPasLivres.remove(positionActuelle); // j'ai enlevé le centre de départ car deja visité en theorie...

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

    public void colorierTourneeUnVehicule(int vehiculeChoisi) {
        for (Chemin chemin : lstVehicules.get(vehiculeChoisi).trajets) {
            chemin.visualisationTournee(vehiculeChoisi+1);
        }
    }

    // pour debugger et afficher
    public void afficherTournee(int vehiculeChosi) {
        System.out.println("===== Tournee du véhicule =====");

        if (lstVehicules.get(vehiculeChosi).trajets.isEmpty()) {
            System.out.println("Aucun trajet calculé.");
            return;
        }

        System.out.println("Centres livrés :");

        for (Node n : lstVehicules.get(vehiculeChosi).centresLivres) {
            System.out.println("- " + n.getId() + " (" + n.getAttribute("type") + ")");
        }

        System.out.println("\nTrajets :");

        for (Chemin c : lstVehicules.get(vehiculeChosi).trajets) {
            System.out.println(c);
        }

        System.out.println("\nDurée totale : " + lstVehicules.get(vehiculeChosi).dureeTotale + " min");
    }
}
