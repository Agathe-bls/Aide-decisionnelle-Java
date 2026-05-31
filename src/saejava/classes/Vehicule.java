package saejava.classes;

import java.util.ArrayList;

import org.graphstream.graph.Node;

public class Vehicule {
    private MonGraphe graphe;
    private Node positionActuelle;
    private ArrayList<Node> centresLivres;
    private ArrayList<Chemin> trajets;
    private int dureeTotale;

    public Vehicule(MonGraphe graphe, Node positionActuelle) {
        this.graphe = graphe;
        this.positionActuelle = positionActuelle;
        this.centresLivres = new ArrayList<>();
        this.trajets = new ArrayList<>();
        this.dureeTotale = 0;
    }

    public Node getPositionActuelle() {
        return positionActuelle;
    }

    public ArrayList<Node> getCentresLivres() {
        return centresLivres;
    }

    public ArrayList<Chemin> getTrajets() {
        return trajets;
    }

    public int getDureeTotale() {
        return dureeTotale;
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

    private Node trouverCentrePlusProche(ArrayList<Node> centresPasLivres) {
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

        centresPasLivres.remove(positionActuelle); // j'ai enlevé le centre de départ car deja visité en theorie...

        while(!centresPasLivres.isEmpty()) {
            Node centrePlusProche = trouverCentrePlusProche(centresPasLivres);

            if (centrePlusProche == null) {
                throw new IllegalStateException("Aucun centre accessible depuis " + positionActuelle.getId());
            }

            Chemin trajet = new Chemin(graphe);
            trajet.cheminDureeMinDijkstra(positionActuelle, centrePlusProche);
            trajets.add(trajet);
            centresLivres.add(centrePlusProche);
            dureeTotale += trajet.getDureeTotale();
            positionActuelle = centrePlusProche;
            centresPasLivres.remove(centrePlusProche);
        }
    }

    public void colorierTournee() {
        for (Chemin chemin : trajets) {
            chemin.visualisationChemin("dureeMin");
        }
    }

    // pour debugger et afficher
    public void afficherTournee() {
        System.out.println("===== Tournee du véhicule =====");

        if (trajets.isEmpty()) {
            System.out.println("Aucun trajet calculé.");
            return;
        }

        System.out.println("Centres livrés :");

        for (Node n : centresLivres) {
            System.out.println("- " + n.getId() + " (" + n.getAttribute("type") + ")");
        }

        System.out.println("\nTrajets :");

        for (Chemin c : trajets) {
            System.out.println(c);
        }

        System.out.println("\nDurée totale : " + dureeTotale + " min");
    }
}
