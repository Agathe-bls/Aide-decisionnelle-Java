package saejava.classes;

import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import java.util.ArrayList;

//Import csv
import java.io.FileInputStream;
import java.util.Scanner;

//Création graphe aléatoire
import java.util.random.RandomGenerator;

public class MonGraphe extends SingleGraph {

    private boolean estVide;
    
    /**
     * Construit un nouveau graphe vide.
     *
     * @param id_graphe identifiant du graphe
     *
     * @author Balsan Agathe
     */
    public MonGraphe(String id_graphe) {
        super(id_graphe);
        estVide = true;
    }

    /**
     * Indique si le graphe est vide ou non.
     *
     * @return true si le graphe ne contient aucun centre, false sinon
     *
     * @author Balsan Agathe
     */
    public boolean getEstVide() {
        return estVide;
    }

    /**
     * Retourne l'intitulé complet du type d'un centre.
     *
     * @param centre centre dont on souhaite connaître le type complet
     *
     * @return le nom complet du type du centre ("Nutrition", "Maternité" ou "Bloc opératoire")
     *
     * @throws IllegalArgumentException si le type du centre est invalide
     * 
     * @author Balsan Agathe
     */
    public String getTypeComplet(Node centre) {
        String res = "";
        if (centre.getAttribute("type").equals("M")) {
            res = "Maternité";
        } else if (centre.getAttribute("type").equals("O")) {
            res = "Bloc opératoire";
        } else if (centre.getAttribute("type").equals("N")) {
            res = "Nutrition";
        } else {
            throw new IllegalArgumentException("Type invalide : " + centre.getAttribute("type")); //REVERIFIER OU VONT TOUTES LES EXCEPTIONS PAR RAPPORT À CA
        }
        return res;
    }

//Import des graphes des fichiers
    private static ArrayList<String> lireCSV(String nomFichier) {
        ArrayList<String> lignes = new ArrayList<>();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lignes;
    }

    private void creerCentres(ArrayList<String> lignes) {
        for (int i = 0; i < lignes.size(); i++) {
            String[] ligne = lignes.get(i).split(";");

            Node centre = this.addNode(ligne[0]);
            centre.addAttribute("type", ligne[1]);
        }
    }

    private void creerRoutes(ArrayList<String> lignes) {

        for (int i = 0; i < lignes.size(); i++) {
            String[] ligne = lignes.get(i).split(";");

            for (int j = i + 1; j < ligne.length - 2; j++) {
                if (ligne[j + 2].compareTo("0") != 0) {
                    String[] infoRoute = ligne[j + 2].split(",");
                    Node n1 = this.getNode(ligne[0]);
                    Node n2 = this.getNode("S" + (j + 1));
                    Edge route = this.addEdge(ligne[0] + "/S" + (j + 1), n1, n2);
                    route.addAttribute("fiabilite", Double.valueOf(infoRoute[0].trim())/10.0);
                    route.addAttribute("distance", Integer.valueOf(infoRoute[1].trim()));
                    route.addAttribute("duree", Integer.valueOf(infoRoute[2].trim()));
                }
            }
        }
    }

    public void chargerGraphe(String nomFichier) {
        ArrayList<String> lignes = lireCSV(nomFichier);
        creerCentres(lignes);
        creerRoutes(lignes);
        this.estVide = false;
    }

//Création du graphe aléatoire

    /**
     * Génère un graphe aléatoire connexe contenant un nombre donné de centres.
     * Les types des centres ainsi que les caractéristiques des routes sont attribués aléatoirement. 
     * Le centre est de type Maternité avec une probabilité de 3/5, et Bloc Opératoire ou Nutrition avec une probabilité de 1/5.
     * Une route existe avec une probabilité de 1/5.
     * Sa fiabilité est comprise entre 2/10 et 10/10, sa distance entre 10 et 50 km et sa durée entre 10 et 120 minutes. 
     * Le graphe généré est garanti connexe grâce à au moins une route de fiabilité supérieure à 0,25 reliant chaque nouveau centre au début de graphe existant.
     * 
     * @param N nombre de centres à générer
     *
     * @author Balsan Agathe
     */
    public void creerGrapheAlea(int N) {
        ArrayList<Node> noeudsAjoutes = new ArrayList<>();
        for (int i = 1; i <= N; i++) {
            if (noeudsAjoutes.isEmpty()) {
                Node premierCentre = this.addNode("S1");
                premierCentre.addAttribute("type", choisirTypeCAlea());
                noeudsAjoutes.add(premierCentre);
            } else {
                Node newCentre = this.addNode("S" + i);
                newCentre.addAttribute("type", choisirTypeCAlea());

                for (Node centreAj : noeudsAjoutes) {
                    if (RandomGenerator.getDefault().nextInt(1, 6) == 1) {
                        Edge e = this.addEdge(centreAj.getId() + "/" + newCentre.getId(), centreAj, newCentre);
                        e.addAttribute("fiabilite", RandomGenerator.getDefault().nextInt(2, 11) / 10.0);
                        e.addAttribute("distance", RandomGenerator.getDefault().nextInt(10, 51));
                        e.addAttribute("duree", RandomGenerator.getDefault().nextInt(10, 121));
                    }
                }
                boolean estConnexe = false;
                for (Node centreAj : noeudsAjoutes) {
                    if (newCentre.hasEdgeBetween(centreAj) && ((double) newCentre.getEdgeBetween(centreAj).getAttribute("fiabilite")) > 0.25) {
                        estConnexe = true;
                    }
                }
                if (!estConnexe) {
                    ArrayList<Node> centresSansRoute = new ArrayList<>();
                    for (Node centreAj : noeudsAjoutes) {
                        if (!centreAj.hasEdgeBetween(newCentre)) {
                            centresSansRoute.add(centreAj);
                        }
                    }
                    if (centresSansRoute.isEmpty()) {
                        Node cDejaExistant = noeudsAjoutes.get(RandomGenerator.getDefault().nextInt(noeudsAjoutes.size()));
                        newCentre.getEdgeBetween(cDejaExistant).setAttribute("fiabilite", RandomGenerator.getDefault().nextInt(3, 11) / 10.0);
                    } else {
                        Node centreAlea = centresSansRoute.get(RandomGenerator.getDefault().nextInt(centresSansRoute.size()));
                        Edge nouvRouteAlea = this.addEdge(centreAlea.getId() + "/" + newCentre.getId(), centreAlea, newCentre);

                        nouvRouteAlea.addAttribute("fiabilite", RandomGenerator.getDefault().nextInt(3, 11) / 10.0);
                        nouvRouteAlea.addAttribute("distance", RandomGenerator.getDefault().nextInt(10, 51));
                        nouvRouteAlea.addAttribute("duree", RandomGenerator.getDefault().nextInt(10, 121));
                    }
                }
                noeudsAjoutes.add(newCentre);
            }
        }
        this.estVide = false;
    }

    private String choisirTypeCAlea() {
        String choix = "";
        int r = RandomGenerator.getDefault().nextInt(1, 6);
        switch (r) {
            case 1, 2, 3:
                choix = "M";
                break;
            case 4:
                choix = "O";
                break;
            case 5:
                choix = "N";
                break;
            default:
                System.err.println("Mauvais nombre aléatoire");
        }
        return choix;
    }

    /**
     * Configure l'affichage graphique des centres du graphe en fonction de leur
     * type en appliquant les couleurs et les labels associés.
     *
     * @throws IllegalArgumentException si le type du centre est invalide
     * 
     * @author Balsan Agathe
     */
    public void visualisationGraphe() {
        for (Node n : this.getNodeSet()) {
            n.setAttribute("ui.style", "size : 20;");
            if (n.getAttribute("type").equals("M")) {
                n.addAttribute("ui.label", n.getId());
                n.addAttribute("ui.style", "text-size: 20px; fill-color: #FA8FF1;");
            } else if (n.getAttribute("type").equals("O")) {
                n.addAttribute("ui.label", n.getId());
                n.addAttribute("ui.style", "text-size: 20px; fill-color: #8FB6FA;");
            } else if (n.getAttribute("type").equals("N")) {
                n.addAttribute("ui.label", n.getId());
                n.addAttribute("ui.style", "text-size: 20px; fill-color: #93FA8F;");
            } else {
                throw new IllegalArgumentException("Type invalide : " + n.getAttribute("type")); //REVERIFIER OU VONT TOUTES LES EXCEPTIONS PAR RAPPORT A CA
            }
        }
    }
    
    public ArrayList<ArrayList<Object[]>> tri_centre_distance(Node n) {
        //Initialisation des variables locales
        Chemin algoChemin = new Chemin(this);
        ArrayList<Object[]> listeMat = new ArrayList<>();
        ArrayList<Object[]> listeNutri = new ArrayList<>();
        ArrayList<Object[]> listeBo = new ArrayList<>();

        //Parcours des noeuds
        for (Node noeud : this.getEachNode()) {
            String type = this.getTypeComplet(noeud);

            if ("Maternité".equals(type) || "Nutrition".equals(type) || "Bloc opératoire".equals(type)) {
                Object distance;
                if (noeud.equals(n)) {
                    distance = 0;
                } else {
                    distance = algoChemin.cheminDureeMinDijkstra(n, noeud).getDistanceTotale();
                }

                Object[] donneesNoeud = new Object[]{"", noeud.getId(), distance};

                //Tri par type
                if ("Maternité".equals(type)) {
                    listeMat.add(donneesNoeud);
                } else if ("Nutrition".equals(type)) {
                    listeNutri.add(donneesNoeud);
                } else if ("Bloc opératoire".equals(type)) {
                    listeBo.add(donneesNoeud);
                }
            }
        }

        //Ajout des sous-listes dans la liste principale
        ArrayList<ArrayList<Object[]>> listeResultat = new ArrayList<>();
        listeResultat.add(listeMat);  
        listeResultat.add(listeNutri); 
        listeResultat.add(listeBo);    

        return listeResultat;
    }
    
    public ArrayList<ArrayList<Object[]>> tri_centre_duree(Node n) {
        //Initialisation des variables locales
        Chemin algoChemin = new Chemin(this);
        ArrayList<Object[]> listeMat = new ArrayList<>();
        ArrayList<Object[]> listeNutri = new ArrayList<>();
        ArrayList<Object[]> listeBo = new ArrayList<>();

        //Parcours des noeuds
        for (Node noeud : this.getEachNode()) {
            String type = this.getTypeComplet(noeud);

            if ("Maternité".equals(type) || "Nutrition".equals(type) || "Bloc opératoire".equals(type)) {
                Object dureeT;
                Object dureeE;
                if (noeud.equals(n)) {
                    dureeT = 0;
                    dureeE = 0;
                } else {
                    dureeT = algoChemin.cheminDureeMinDijkstra(n, noeud).getDureeTotale();
                    dureeE = algoChemin.cheminDureeMinDijkstra(n, noeud).getDureeEstimee();
                }

                Object[] donneesNoeud = new Object[]{"", noeud.getId(), dureeT, dureeE};

                //Tri par type
                if ("Maternité".equals(type)) {
                    listeMat.add(donneesNoeud);
                } else if ("Nutrition".equals(type)) {
                    listeNutri.add(donneesNoeud);
                } else if ("Bloc opératoire".equals(type)) {
                    listeBo.add(donneesNoeud);
                }
            }
        }

        //Ajout des sous-listes dans la liste principale
        ArrayList<ArrayList<Object[]>> listeResultat = new ArrayList<>();
        listeResultat.add(listeMat);
        listeResultat.add(listeNutri);
        listeResultat.add(listeBo);

        return listeResultat;
    }
}

