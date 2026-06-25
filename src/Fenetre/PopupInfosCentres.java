/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Fenetre;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import org.graphstream.graph.Node;
import saejava.classes.MonGraphe;

/**
 * Gère l'affichage d'une boîte de dialogue et de fenêtres graphiques (tableaux)
 * présentant des informations analytiques sur un centre (nœud) sélectionné du graphe.
 * Permet d'obtenir les centres les plus proches ou des tableaux de distances et de durées.
 *
 * @author Auryann
 */
public class PopupInfosCentres {
    /** La fenêtre principale de l'application. */
    private MaFenetre fenetreActuelle;
    
    /** Le graphe d'étude contenant l'ensemble des nœuds et des arêtes. */
    private MonGraphe grapheActuel;
    
    /** Label affichant l'identifiant du centre. */
    private JLabel titre_ic;
    
    /** Label affichant le type textuel complet du centre. */
    private JLabel t_nom_ic;
    
    /**
     * Constructeur de la classe. Initialise les composants et ouvre immédiatement 
     * un menu de sélection (OptionDialog) pour guider l'utilisateur vers le type d'affichage désiré.
     *
     * @param fenetreA    La fenêtre parente de l'application.
     * @param grapheA     Le graphe de données actuel.
     * @param centreClic  Le nœud (centre) qui a été cliqué et sur lequel porte la requête.
     */
    public PopupInfosCentres(MaFenetre fenetreA, MonGraphe grapheA, Node centreClic){
        fenetreActuelle = fenetreA;
        grapheActuel = grapheA;
        titre_ic = new JLabel("ID :" + centreClic.getId());
        t_nom_ic = new JLabel("Type de centre : " + grapheActuel.getTypeComplet(centreClic));
        
        String[] options = {"Centres les plus proches", "Table des distances", "Tables des durées"};
        int choix = JOptionPane.showOptionDialog(fenetreActuelle, "Id : " + centreClic.getId() + "\nType de centre : " + grapheActuel.getTypeComplet(centreClic) + "\nQuelles informations voulez-vous ? ", "Information du centre", 0, JOptionPane.DEFAULT_OPTION, null, options, options[2]);
        
        switch (choix) {
            case 0:
               centre_proche(centreClic);
                break;

            case 1:
                table_centre_km(centreClic);
                break;

            case 2:
                table_centre_duree(centreClic);
                break;
                
            default:
                break;
        }
    }
    
    /**
     * Calcule, extrait et affiche dans un tableau graphique (`JTable`) les centres médicaux 
     * les plus proches (Maternité, Nutrition, Bloc Opératoire) depuis le nœud donné,
     * classés par durée totale croissante. Applique un code couleur distinct par type de centre.
     *
     * @param n Le nœud source à partir duquel chercher les plus proches voisins.
     */
    public void centre_proche(Node n) {
        ArrayList<ArrayList<Object[]>> resultatTri = grapheActuel.tri_centre_duree(n);

        // Initialisation des sous-listes
        ArrayList<Object[]> listeMat   = resultatTri.get(0);
        ArrayList<Object[]> listeNutri = resultatTri.get(1); 
        ArrayList<Object[]> listeBo    = resultatTri.get(2);

        // Tri des listes par durée totale croissante
        listeMat.sort(java.util.Comparator.comparing(ligne -> (Integer) ligne[2]));
        listeNutri.sort(java.util.Comparator.comparing(ligne -> (Integer) ligne[2]));
        listeBo.sort(java.util.Comparator.comparing(ligne -> (Integer) ligne[2]));

        //Titre des colonnes
        String[] titre = {"Centre", "Id", "Durée Totale (min)", "Durée Estimée (min)"};

        //Liste Finale
        ArrayList<Object[]> listeProches = new ArrayList<>();

        //Maternité
        if (!listeMat.isEmpty()) {
            Object[] plusProcheMat = listeMat.get(0);
            if (n.getId().equals(plusProcheMat[1])) {
                plusProcheMat = (listeMat.size() > 1) ? listeMat.get(1) : null;
            }

            if (plusProcheMat != null) {
                listeProches.add(new Object[]{"Maternité la plus proche", plusProcheMat[1], plusProcheMat[2], plusProcheMat[3]});
            } else {
                listeProches.add(new Object[]{"Maternité la plus proche", "Aucune autre", "", ""});
            }
        } else {
            listeProches.add(new Object[]{"Maternité la plus proche", "Aucune", "", ""});
        }

        // Nutrition
        if (!listeNutri.isEmpty()) {
            Object[] plusProcheNutri = listeNutri.get(0);
            if (n.getId().equals(plusProcheNutri[1])) {
                plusProcheNutri = (listeNutri.size() > 1) ? listeNutri.get(1) : null;
            }

            if (plusProcheNutri != null) {
                listeProches.add(new Object[]{"Nutrition la plus proche", plusProcheNutri[1], plusProcheNutri[2], plusProcheNutri[3]});
            } else {
                listeProches.add(new Object[]{"Nutrition la plus proche", "Aucun autre", "", ""});
            }
        } else {
            listeProches.add(new Object[]{"Nutrition la plus proche", "Aucune", "", ""});
        }

        //Bloc Opératoire
        if (!listeBo.isEmpty()) {
            Object[] plusProcheBo = listeBo.get(0);
            if (n.getId().equals(plusProcheBo[1])) {
                plusProcheBo = (listeBo.size() > 1) ? listeBo.get(1) : null;
            }

            if (plusProcheBo != null) {
                listeProches.add(new Object[]{"Bloc opératoire le plus proche", plusProcheBo[1], plusProcheBo[2], plusProcheBo[3]});
            } else {
                listeProches.add(new Object[]{"Bloc opératoire le plus proche", "Aucun autre", "", ""});
            }
        } else {
            listeProches.add(new Object[]{"Bloc opératoire le plus proche", "Aucun", "", ""});
        }

        //Conversion des listes en objet pour le tableau 
        Object[][] donnee = listeProches.toArray(new Object[listeProches.size()][]);

        // Affichage Graphique
        JFrame fenetreTableau = new JFrame("Centres les plus proches de " + n.getId());
        fenetreTableau.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTable tableau = new JTable(donnee, titre);
        
        //Ajout de couleurs
        tableau.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value,
                    boolean isSelected, boolean hasFocus,
                    int row, int column) {
                Component composant = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                
                composant.setBackground(Color.WHITE);
                
                switch (row) {
                    case 0:
                        composant.setBackground(Color.decode("#FA8FF1"));
                        break;
                    case 1:
                        composant.setBackground(Color.decode("#93FA8F"));
                        break;
                    case 2:
                        composant.setBackground(Color.decode("#8FB6FA"));
                        break;
                    default:
                        break;
                }
                return composant;
            }
        });

        fenetreTableau.getContentPane().add(new JScrollPane(tableau));
        fenetreTableau.setPreferredSize(new Dimension(700, listeProches.size() * tableau.getRowHeight() + 60));
        fenetreTableau.pack();
        
        fenetreTableau.setLocationRelativeTo(null);
        fenetreTableau.setVisible(true);
        
    }
    
    /**
     * Génère et affiche une fenêtre contenant un tableau des distances réelles (en kilomètres) 
     * entre le nœud sélectionné et l'intégralité des autres centres triés par catégorie.
     * La hauteur maximale de la fenêtre s'adapte dynamiquement mais reste bridée à 23 lignes.
     *
     * @param n Le nœud source utilisé pour calculer les distances.
     */
    public void table_centre_km(Node n) {
        
         //Initialisation de la liste de liste (fonction tri_centre)
         ArrayList<ArrayList<Object[]>> resultatTri;
         resultatTri = grapheActuel.tri_centre_distance(n);

         //Liste Finale
         ArrayList<Object[]> listeDonnees = new ArrayList<>();

         //Sous-liste
         ArrayList<Object[]> listeMat   = resultatTri.get(0);
         ArrayList<Object[]> listeNutri = resultatTri.get(1); 
         ArrayList<Object[]> listeBo    = resultatTri.get(2);

         //Maternité
         if (!listeMat.isEmpty()) {
             listeDonnees.add(new Object[]{"Distances aux maternités", "", ""});
             listeDonnees.addAll(listeMat);
         }

        //Nutrition
        if (!listeNutri.isEmpty()) {
            listeDonnees.add(new Object[]{"Distances aux nutrition", "", ""});
            listeDonnees.addAll(listeNutri);
        }

        //Bloc Opératoire
        if (!listeBo.isEmpty()) {
            listeDonnees.add(new Object[]{"Distances aux blocs opératoires", "", ""}); 
            listeDonnees.addAll(listeBo);
        }

        //Ajout des données dans le tableau
        String[] titre = {"Centre", "Id", "Distance (km)"};
        Object[][] donnee = listeDonnees.toArray(new Object[listeDonnees.size()][]);

        //Affichage
        JFrame fenetreTableau = new JFrame("Distances depuis " + n.getId());
        JTable tableau = new JTable(donnee, titre);
        
        AfficheCouleursPlusieursLignes(tableau);
        
        fenetreTableau.getContentPane().add(new JScrollPane(tableau));
        if(listeDonnees.size() > 23){
            fenetreTableau.setPreferredSize(new Dimension(700, 23 * tableau.getRowHeight() + 60));
        }
        else {
            fenetreTableau.setPreferredSize(new Dimension(700, listeDonnees.size() * tableau.getRowHeight() + 60));
        }
        fenetreTableau.pack();
        fenetreTableau.setLocationRelativeTo(null);
        fenetreTableau.setVisible(true);
        
    }

    /**
     * Génère et affiche une fenêtre contenant un tableau des durées de trajet (en minutes)
     * calculées entre le nœud sélectionné et l'ensemble des autres centres regroupés par catégorie.
     * La taille de l'interface s'ajuste pour un confort de lecture optimal limité à 23 lignes.
     *
     * @param n Le nœud source servant de point de départ aux calculs temporels.
     */
    public void table_centre_duree(Node n) {
         //Initialisation de la liste de liste (fonction tri_centre)
         ArrayList<ArrayList<Object[]>> resultatTri;
         resultatTri = grapheActuel.tri_centre_duree(n);

         //Liste Finale
         ArrayList<Object[]> listeDonnees = new ArrayList<>();

         //Sous-liste
         ArrayList<Object[]> listeMat   = resultatTri.get(0);
         ArrayList<Object[]> listeNutri = resultatTri.get(1); 
         ArrayList<Object[]> listeBo    = resultatTri.get(2);

         //Maternité
         if (!listeMat.isEmpty()) {
             listeDonnees.add(new Object[]{"Distances aux maternités", "", ""});
             listeDonnees.addAll(listeMat);
         }

        //Nutrition
        if (!listeNutri.isEmpty()) {
            listeDonnees.add(new Object[]{"Distances aux nutrition", "", ""});
            listeDonnees.addAll(listeNutri);
        }

        //Bloc Opératoire
        if (!listeBo.isEmpty()) {
            listeDonnees.add(new Object[]{"Distances aux blocs opératoires", "", ""}); 
            listeDonnees.addAll(listeBo);
        }

        //Ajout des données dans le tableau
        String[] titre = {"Centre", "Id", "Durée (min)"};
        Object[][] donnee = listeDonnees.toArray(new Object[listeDonnees.size()][]);

        //Affichage
        JFrame fenetreTableau = new JFrame("Distances depuis " + n.getId());
        JTable tableau = new JTable(donnee, titre);
        
        AfficheCouleursPlusieursLignes(tableau);
        
        fenetreTableau.getContentPane().add(new JScrollPane(tableau));
        if(listeDonnees.size() > 23){
            fenetreTableau.setPreferredSize(new Dimension(700, 23 * tableau.getRowHeight() + 60));
        }
        else {
            fenetreTableau.setPreferredSize(new Dimension(700, listeDonnees.size() * tableau.getRowHeight() + 60));
        }
        fenetreTableau.pack();
        fenetreTableau.setLocationRelativeTo(null);
        fenetreTableau.setVisible(true);
    }
    
    /**
     * Applique un gestionnaire de rendu personnalisé (`TableCellRenderer`) sur le tableau fourni
     * afin de colorer différemment les en-têtes de sections et les plages de lignes
     * selon le type de centre (Maternité, Nutrition, Bloc Opératoire). S'appuie sur le 
     * décompte total des types fourni par `fenetreActuelle.nbCentresParType()`.
     *
     * @param tableau Le `JTable` dont les cellules doivent être colorées dynamiquement.
     */
    public void AfficheCouleursPlusieursLignes(JTable tableau) {
        tableau.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value,
                    boolean isSelected, boolean hasFocus,
                    int row, int column) {
                Component composant = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                composant.setBackground(Color.decode("#d4e3fd"));

                if (row == 0) {
                    composant.setBackground(Color.decode("#FA8FF1"));
                } else if (row > 0 && row < 1 + fenetreActuelle.nbCentresParType()[1]) {
                    composant.setBackground(Color.decode("#fcc7f7"));
                } else if (row == 1 + fenetreActuelle.nbCentresParType()[1]) {
                    composant.setBackground(Color.decode("#93fa8f"));
                } else if (row > 1 + fenetreActuelle.nbCentresParType()[1] && row < 2 + fenetreActuelle.nbCentresParType()[1] + fenetreActuelle.nbCentresParType()[0]) {
                    composant.setBackground(Color.decode("#c9fcc7"));
                } else if (row == 2 + fenetreActuelle.nbCentresParType()[1] + fenetreActuelle.nbCentresParType()[0]) {
                    composant.setBackground(Color.decode("#8FB6FA"));
                }
                return composant;
            }
        });
    }
    
}