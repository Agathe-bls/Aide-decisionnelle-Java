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
 *
 * @author Agathe
 */
public class PopupInfosCentres {
    private MaFenetre fenetreActuelle;
    private MonGraphe grapheActuel;
    private JLabel titre_ic;
    private JLabel t_nom_ic;
    
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
                //throw new IllegalStateException("Unexpected value: " + choix);
                //FAIRE UNE EXCEPTION PAR RAPPORT AU CHOIX INNEXISTANT
                //JE PENSE QUE CA SUFFIT DE JUSTE SORTIR PARCE QUE ON PEUT QUE CLIQUER DANS LA CROIX SINON ET CROIX = -1
        }
    }
    
    private void centre_proche(Node n) {
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
        System.out.println("AAAAAAAAAAAAAAAA : " + tableau.getRowHeight());
        System.out.println("AAAAAAAAAAAAAAAA : " + listeProches.size());
        System.out.println("AAAAAAAAAAAAAAAA : " + listeProches.size() * tableau.getRowHeight() * 10);
        fenetreTableau.setPreferredSize(new Dimension(700, listeProches.size() * tableau.getRowHeight()));
        fenetreTableau.pack();
        
        fenetreTableau.setLocationRelativeTo(null);
        fenetreTableau.setVisible(true);
        
    }
    
    private void table_centre_km(Node n) {
        
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
        fenetreTableau.pack();
        fenetreTableau.setLocationRelativeTo(null);
        fenetreTableau.setVisible(true);
        
    }

    private void table_centre_duree(Node n) {
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
        fenetreTableau.pack();
        fenetreTableau.setLocationRelativeTo(null);
        fenetreTableau.setVisible(true);
    }
    
    private void AfficheCouleursPlusieursLignes(JTable tableau) {
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
