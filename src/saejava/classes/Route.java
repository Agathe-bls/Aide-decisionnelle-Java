package saejava.classes;

public class Route {
    private Centre depart;
    private Centre arrivee;
    private int fiabilite;
    private int distance;
    private int duree;

    public Route(Centre depart, Centre arrivee, int fiabilite, int distance, int duree) {
        this.depart = depart;
        this.arrivee = arrivee;
        this.fiabilite = fiabilite;
        this.distance = distance;
        this.duree = duree;
    }

    public Centre getDepart() {
        return depart;
    }

    public Centre getArrivee() {
        return arrivee;
    }

    public int getFiabilite() {
        return fiabilite;
    }

    public int getDistance() {
        return distance;
    }

    public int getDuree() {
        return duree;
    }

    @Override
    public String toString() {
        return depart.getId() + " - " + arrivee.getId()
                + " | fiabilite=" + fiabilite
                + " | distance=" + distance
                + " | duree=" + duree;
    }
}
