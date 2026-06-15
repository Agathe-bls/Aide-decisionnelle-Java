package saejava.classes;

import java.io.FileInputStream;
import java.util.ArrayList;
<<<<<<< HEAD
=======

>>>>>>> Agathe-Graphe_aleatoire
import java.util.Scanner;
import java.util.random.RandomGenerator;

<<<<<<< HEAD
import org.graphstream.graph.Edge;
//Création graphe aléatoire
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
=======
public class MonGraphe extends SingleGraph {
>>>>>>> Agathe-Graphe_aleatoire

    private boolean estVide;

    public MonGraphe(String id_graphe) {
        super(id_graphe);
        estVide = true;
    }

    public boolean getEstVide() {
        return estVide;
    }

    public String getTypeComplet(Node centre) {
        String res = "";
<<<<<<< HEAD
        String type = centre.getAttribute("type");

        if ("M".equals(type))
            res = "Maternite";
        else if ("O".equals(type))
            res = "Bloc operatoire";
        else if ("N".equals(type))
            res = "Nutrition";
        else
            throw new IllegalArgumentException("Type invalide : " + type); //REVERIFIER OU VONT TOUTES LES EXCEPTIONS PAR RAPPORT A CA
        
=======
        if (centre.getAttribute("type").equals("M")) {
            res = "Maternite";
        } else if (centre.getAttribute("type").equals("O")) {
            res = "Bloc operatoire";
        } else if (centre.getAttribute("type").equals("N")) {
            res = "Nutrition";
        } else {
            throw new IllegalArgumentException("Type invalide : " + centre.getAttribute("type")); //REVERIFIER OU VONT TOUTES LES EXCEPTIONS PAR RAPPORT A CA
        }
>>>>>>> Agathe-Graphe_aleatoire
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
<<<<<<< HEAD
                    Node n2 = this.getNode("S"+(j+1));
                    Edge route = this.addEdge(ligne[0]+"/S"+(j+1), n1, n2);
                    route.addAttribute("fiabilite", Double.valueOf(infoRoute[0].trim())/10);
=======
                    Node n2 = this.getNode("S" + (j + 1));
                    Edge route = this.addEdge(ligne[0] + "/S" + (j + 1), n1, n2);
                    route.addAttribute("fiabilite", Double.valueOf(infoRoute[0].trim()));
>>>>>>> Agathe-Graphe_aleatoire
                    route.addAttribute("distance", Integer.valueOf(infoRoute[1].trim()));
                    route.addAttribute("duree", Integer.valueOf(infoRoute[2].trim()));
                }
            }
        }
    }

    public void chargerGraphe(String nomFichier) {
        this.clear();

        ArrayList<String> lignes = lireCSV(nomFichier);
        creerCentres(lignes);
        creerRoutes(lignes);
        this.estVide = false;
    }

//Création du graphe aléatoire
    public void creerGrapheAleatoire(int N) {
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

<<<<<<< HEAD
    // j'ai fait ça pour plus tard et pour debuggé les graphe si jamais avec la fonction d'affichage des infos du graphe 
    public int compterCentresParType(String typeRecherche) {
        int compteur = 0;

        for (Node n : this.getEachNode()) {
            String type = n.getAttribute("type");

            if (typeRecherche.equals(type)) {
                compteur++;
            }
        }

        return compteur;
    }

    public int getNbMaternites() {
        return compterCentresParType("M");
    }

    public int getNbBlocsOperatoires() {
        return compterCentresParType("O");
    }

    public int getNbCentresNutrition() {
        return compterCentresParType("N");
    }

    public void afficherInfoGraphe() {
        System.out.println("===== Info du graphe =====");
        System.out.println("Nombre de sommets : " + this.getNodeCount());
        System.out.println("Nombre de routes : " + this.getEdgeCount());

        System.out.println("Maternités : " + getNbMaternites());
        System.out.println("Blocs opératoires : " + getNbBlocsOperatoires());
        System.out.println("Centres de nutrition : " + getNbCentresNutrition());

        System.out.println("\n--- Sommets ---");
        for (Node n : this.getEachNode()) {
            System.out.println(n.getId() + " | type = " + n.getAttribute("type"));
        }

        System.out.println("\n--- Routes ---");
        for (Edge e : this.getEachEdge()) {
            System.out.println(
                e.getId()
                + " : " + e.getSourceNode().getId()
                + " -- " + e.getTargetNode().getId()
                + " | fiabilité = " + e.getAttribute("fiabilite")
                + " | distance = " + e.getAttribute("distance")
                + " | durée = " + e.getAttribute("duree")
            );
        }
    }
    
    //Si j'ai le temps Je fais le connexité
    
    
//    private void verifierCentresSeuls(){
//        for(Node n : this){
//            if(n.getDegree() == 0){
//                String randomIdCentre = "S" + RandomGenerator.getDefault().nextInt(this.getNodeCount());
//                while(randomIdCentre.equals(n.getId())){
//                    randomIdCentre = "S" + RandomGenerator.getDefault().nextInt(this.getNodeCount());
//                }
//                Node nAlea = this.getNode(randomIdCentre);
//                Edge e = this.addEdge(nAlea.getId()+ "/" +n.getId(), nAlea, n);
//                
//                e.addAttribute("fiabilite", RandomGenerator.getDefault().nextDouble(2,10.1)/10);
//                e.addAttribute("distance", RandomGenerator.getDefault().nextInt(10,51));
//                e.addAttribute("duree", RandomGenerator.getDefault().nextInt(10,121));
//                
//            }
//                
//        }
//    }
    
    
     public void visualisationGraphe() {
         for(Node n : this.getNodeSet()){
             if (n.getAttribute("type").equals("M")) {
                 n.addAttribute("ui.label", "" + n.getAttribute("type"));
                 n.addAttribute("ui.style", "text-size: 20px; fill-color: #FA8FF1;");
             } else if (n.getAttribute("type").equals("O")) {
                 n.addAttribute("ui.label", "" + n.getAttribute("type"));
                 n.addAttribute("ui.style", "text-size: 20px; fill-color: #8FB6FA;");
             } else if (n.getAttribute("type").equals("N")) {
                 n.addAttribute("ui.label", "" + n.getAttribute("type"));
                 n.addAttribute("ui.style", "text-size: 20px; fill-color: #93FA8F;");
             } else {
                 throw new IllegalArgumentException("Type invalide : " + n.getAttribute("type")); //REVERIFIER OU VONT TOUTES LES EXCEPTIONS PAR RAPPORT A CA
             }
         }
     }
=======
    public void visualisationRoutes() { // 0 = fiabilité  / 1 = duree / 2 = distance
        for (Edge e : this.getEachEdge()) {
            e.addAttribute("ui.label", e.getAttribute("fiabilite"));
            e.addAttribute("ui.style", "text-size: 20px;");
        }
    }
>>>>>>> Agathe-Graphe_aleatoire
}
