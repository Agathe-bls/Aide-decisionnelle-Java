package saejava.classes;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.Scanner;

//Création graphe aléatoire
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import java.util.random.RandomGenerator;

import java.util.Iterator;

public class MonGraphe extends SingleGraph{ //C'est PARCE QUE J'AI EXTENDS SINGLEGRAPH FAUDRA QUE JE VOUS EN PARLE
    
     public MonGraphe(String id_graphe) {
        super(id_graphe);
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
        }
        catch(Exception e) {
            e.printStackTrace() ;
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
    }
    
    
//Création du graphe aléatoire

    public Graph creerGrapheAleatoire(int N){        
        for (int i=0; i<N; i++){
            Node n = this.addNode("S"+i);
            n.addAttribute("type", choisirTypeCAlea());
        }
        
        ajToutesRoutes();
        //ATTENTION IL FAUT QUIL Y AI PLUSIEURS SOMMETS ICI
        supprimerRoutesAlea();
        
        verifierCentresSeuls();
        
        return this;
    }
    
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
    
    private void ajToutesRoutes(){
        for (Node n : this.getEachNode()) {
            for (Node nV : this){
                if(n != nV){
                    if(!n.hasEdgeBetween(nV)){
                        Edge e = this.addEdge(nV.getId()+ "/" +n.getId(), nV, n);
                        e.addAttribute("fiabilite", RandomGenerator.getDefault().nextDouble(2,10.1)/10);
                        e.addAttribute("distance", RandomGenerator.getDefault().nextInt(10,51));
                        e.addAttribute("duree", RandomGenerator.getDefault().nextInt(10,121));
                    }
                }
            }
        }
    }
    
    private void supprimerRoutesAlea(){
        ArrayList<Edge> aSuppr = new ArrayList<>();
        for(Edge e : this.getEachEdge())
            aSuppr.add(e);
        
        for(Edge eAS : aSuppr){
            if (RandomGenerator.getDefault().nextInt(1,6) != 1){
                this.removeEdge(eAS);
            }
        }
    }
    
    private void verifierCentresSeuls() {
        //et graphe non connexe donc je fais une liaison entre chaque centre mais est ce que ducoup c'est vraiment aléatoire ?
        //AVEC FIABILITE plus grande que 0.25 pour pas faire beuguer pour le plus cours chemin !!!
        for (int i = 1; i < this.getNodeCount(); i++) {
            Node precedent = this.getNode("S" + (i - 1));
            Node actuel = this.getNode("S" + i);

            if (!actuel.hasEdgeBetween(precedent)) {
                Edge e = this.addEdge(precedent.getId() + "/" + actuel.getId(), precedent, actuel);

                e.addAttribute("fiabilite", RandomGenerator.getDefault().nextDouble(3, 10.1) / 10);
                e.addAttribute("distance", RandomGenerator.getDefault().nextInt(10, 51));
                e.addAttribute("duree", RandomGenerator.getDefault().nextInt(10, 121));
            }
            if ((double)(actuel.getEdgeBetween(precedent).getAttribute("fiabilite")) < 0.25){
                Edge e = actuel.getEdgeBetween(precedent);
                
                e.setAttribute("fiabilite", RandomGenerator.getDefault().nextDouble(3, 10.1) / 10);
            }
        }
    }
    
    
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
}
