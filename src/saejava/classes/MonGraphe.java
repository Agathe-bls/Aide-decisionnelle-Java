package saejava.classes;

import java.io.FileInputStream;
import java.util.ArrayList;

import java.util.Scanner;

//Création graphe aléatoire
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import java.util.random.RandomGenerator;


public class MonGraphe extends SingleGraph{
    
    private boolean estVide;
    
     public MonGraphe(String id_graphe) {
        super(id_graphe);
        estVide = true;
    }

    public boolean getEstVide() {
        return estVide;
    }
    
    public String getTypeComplet(Node centre){
        String res = "";
        if (centre.getAttribute("type").equals("M"))
            res = "Maternite";
        else if (centre.getAttribute("type").equals("O"))
            res = "Bloc operatoire";
        else if (centre.getAttribute("type").equals("N"))
            res = "Nutrition";
        else
            throw new IllegalArgumentException("Type invalide : " + centre.getAttribute("type")); //REVERIFIER OU VONT TOUTES LES EXCEPTIONS PAR RAPPORT A CA
        
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
                if (ligne[j+2].compareTo("0") != 0) {
                    String[] infoRoute = ligne[j+2].split(",");
                    Node n1 = this.getNode(ligne[0]);
                    Node n2 = this.getNode("S"+(j+1));
                    Edge route = this.addEdge(ligne[0]+"/S"+(j+1), n1, n2);
                    route.addAttribute("fiabilite", Double.valueOf(infoRoute[0].trim()));
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

//    public void creerGrapheAleatoire(int N){        
//        for (int i=1; i<=N; i++){
//            Node n = this.addNode("S"+i);
//            n.addAttribute("type", choisirTypeCAlea());
//        }
//        
////        ajToutesRoutes();
////        //ATTENTION IL FAUT QUIL Y AI PLUSIEURS SOMMETS ICI
////        supprimerRoutesAlea();
////        
////        verifierCentresSeuls();
//
//        
//        this.estVide = false;
//    }
    
    private String choisirTypeCAlea(){
        String choix = "";
        int r = RandomGenerator.getDefault().nextInt(1,6);
        switch(r){
            case 1,2,3 : choix =  "M"; break;
            case 4 : choix = "O"; break;
            case 5 : choix = "N"; break;
            default : System.err.println("Mauvais nombre aléatoire");
        }
        return choix;
    }
    
    public void creaGraphealea(int N){
        ArrayList<Node> noeudsAjoutes = new ArrayList<>();
        for(int i = 1; i<=N; i++){
            if(noeudsAjoutes.isEmpty()){
                Node premierCentre = this.addNode("S1");
                premierCentre.addAttribute("type", choisirTypeCAlea());
                noeudsAjoutes.add(premierCentre);
            }
            else{
                boolean estConnexe = false;
                Node newCentre = this.addNode("S" + i);
                newCentre.addAttribute("type", choisirTypeCAlea());
                
                for(Node centreAj : noeudsAjoutes){
                    if (RandomGenerator.getDefault().nextInt(1, 6) == 1) {
                        Edge e = this.addEdge(centreAj.getId()+ "/" +newCentre.getId(), centreAj, newCentre);
                        e.addAttribute("fiabilite", RandomGenerator.getDefault().nextInt(2, 11) / 10.0);
                        e.addAttribute("distance", RandomGenerator.getDefault().nextInt(10, 51));
                        e.addAttribute("duree", RandomGenerator.getDefault().nextInt(10, 121));
                    }
                }
                for(Node centreAj : noeudsAjoutes){
                    if(newCentre.hasEdgeBetween(centreAj) && ((double)newCentre.getEdgeBetween(centreAj).getAttribute("fiabilite")) > 0.25)
                        estConnexe = true;
                }
                if(!estConnexe) {
                    String randomIdCentre = "";
                    if (newCentre.getId().equals("S2")) {
                        randomIdCentre = "S1";
                    } else {
                        randomIdCentre = "S" + RandomGenerator.getDefault().nextInt(1, noeudsAjoutes.size());

                        System.out.println(randomIdCentre);
                        while (randomIdCentre.equals(newCentre.getId()) || this.getNode(randomIdCentre).hasEdgeBetween(newCentre)) {
                            randomIdCentre = "S" + RandomGenerator.getDefault().nextInt(1, noeudsAjoutes.size());
                        }
                    }
                    
                    Node nAlea = this.getNode(randomIdCentre);
                    Edge e = this.addEdge(nAlea.getId() + "/" + newCentre.getId(), nAlea, newCentre);

                    e.addAttribute("fiabilite", RandomGenerator.getDefault().nextInt(3, 11) / 10.0);
                    e.addAttribute("distance", RandomGenerator.getDefault().nextInt(10, 51));
                    e.addAttribute("duree", RandomGenerator.getDefault().nextInt(10, 121));
                    estConnexe = true;
                }
                noeudsAjoutes.add(newCentre);
            }
        }
        this.estVide = false;
    }
    //REGARDER COMMENT FAIRE QUAND IL Y A TOUTES LES ROUTES MAIS PAS LES FIA > 0.25
    
//    private boolean testSiConnexe(ArrayList<Node> noeudsAjoutes, Node newCentre) {
//        boolean estConnexe = false;
//        for (Node centreAj : noeudsAjoutes) {
//            if (newCentre.hasEdgeBetween(centreAj) && ((double) newCentre.getEdgeBetween(centreAj).getAttribute("fiabilite")) > 0.25) {
//                estConnexe = true;
//            }
//        }
//        return estConnexe;
//    }
    
    
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
    
    public void visualisationRoutes(){ // 0 = fiabilité  / 1 = duree / 2 = distance
        for(Edge e : this.getEachEdge()){
            e.addAttribute("ui.label", e.getAttribute("fiabilite"));
            e.addAttribute("ui.style", "text-size: 20px;");
        }
    }
     
     
}
