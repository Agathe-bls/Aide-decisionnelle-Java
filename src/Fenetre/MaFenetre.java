/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Fenetre;

import java.awt.BorderLayout;
import java.awt.Color;
import static java.awt.Color.BLACK;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.ui.swingViewer.DefaultView;
import org.graphstream.ui.view.Viewer;
import org.graphstream.graph.Node;
import org.graphstream.ui.swingViewer.DefaultView;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;
import saejava.classes.Chemin;
import saejava.classes.MonGraphe;
/**
 *
 * @author Auryann / Agathe
 */
public class MaFenetre extends JFrame {
    MonGraphe grapheActuel = new MonGraphe("gActuel");
    
    //Partie Haut Nouvelle-map
    private JLabel nom_m;
    private JButton btNouvelle_map;
    public static JFileChooser f;
    
    //Partie Haut Vehicules
    private JLabel vehicule;
    private JLabel intervention;
    
    //Partie Haut Centres
    private JLabel type_centre;
    private JLabel nutrition;
    private JLabel nbr_n;
    private JPanel couleur_n;
    private JLabel maternite;
    private JLabel nbr_m;
    private JPanel couleur_m;
    private JLabel bloc_operatoire;
    private JLabel nbr_bo;
    private JPanel couleur_bo;
    
    //Partie Haut Nouveau trajets
    private JButton btNouveau_trajet;
    
    //Map
    private JPanel pMap;
    Chemin chemin = new Chemin(grapheActuel);
    JLabel coutEtTrajet;
    
    //Partie Est 
    private JLabel titreComboC;
    
    //Menu
    private JMenuBar menuBar;
    private JMenu menuCentres;
    private JMenu menuRoutes;
    private JMenu menuSeuil;
    private JCheckBoxMenuItem checkType;
    private JCheckBoxMenuItem checkNom;
    private JCheckBoxMenuItem checkFia;
    private JCheckBoxMenuItem checkDuree;
    private JCheckBoxMenuItem checkDistance;
    
    //popup info centre
    private JLabel titre_ic;
    private JLabel t_nom_ic;
    
    public MaFenetre(){
        super("MedicOne");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(1500,800));
        this.pack();
        this.setLocationRelativeTo(null);
        
        créationMenu();
        initComposants();
        testGraphPresent();
       
        gererTailleTextes();
        
        this.setVisible(true);
    }
    
    private void gererTailleTextes(){
        Font police = new Font("Arial", Font.ROMAN_BASELINE, 16);
        Font policeTitre = new Font("Arial", Font.BOLD, 17);
        //Partie Haut Nouvelle-map
        nom_m.setFont(policeTitre);
        btNouvelle_map.setFont(police);
        //Partie Haut Véhicules
        vehicule.setFont(policeTitre);
        intervention.setFont(police);
        //Partie Haut Centres
        type_centre.setFont(policeTitre);
        nutrition.setFont(police);
        nbr_n.setFont(police);
        couleur_n.setFont(police);
        maternite.setFont(police);
        nbr_m.setFont(police);
        couleur_m.setFont(police);
        bloc_operatoire.setFont(police);
        nbr_bo.setFont(police);
        couleur_bo.setFont(police);
        //Partie Haut Nouveau trajets
         btNouveau_trajet.setFont(police);
         //Map
         coutEtTrajet.setFont(police);
         //Partie Est 
        titreComboC.setFont(policeTitre);
        //Menu
        menuCentres.setFont(policeTitre);
        menuRoutes.setFont(policeTitre);
        menuSeuil.setFont(policeTitre);
        checkType.setFont(police);
        checkNom.setFont(police);
        checkFia.setFont(police);
        checkDuree.setFont(police);
        checkDistance.setFont(police);
    }
    
    private void créationMenu(){
        menuBar = new JMenuBar();
        MaFenetre.this.setJMenuBar(menuBar);
        
        menuCentres = new JMenu("Informations des centres");
        menuRoutes = new JMenu("Informations des routes");
        menuSeuil = new JMenu("Seuil de dangerosité");
        //                                  menuSeuil.addSeparator(); CA NE MARCHE PAS JSP POURQUOI
                
        checkNom = new JCheckBoxMenuItem("Noms des centres");
        checkType = new JCheckBoxMenuItem("Types des centres");
        checkFia = new JCheckBoxMenuItem("Fiabilités");
        checkDuree = new JCheckBoxMenuItem("Durées");
        checkDistance = new JCheckBoxMenuItem("Distances");
        
        menuCentres.add(checkNom);
        menuCentres.add(checkType);

        menuRoutes.add(checkFia);
        menuRoutes.add(checkDuree);
        menuRoutes.add(checkDistance);
        
        menuBar.add(menuCentres);
        menuBar.add(menuRoutes);
        menuBar.add(menuSeuil);
    }
    
    private void initComposants(){
        JPanel pPrincipal = new JPanel(new BorderLayout());
        
        //Partie Haut Nouvelle-map
        JPanel pHaut = new JPanel(new GridBagLayout());
        pHaut.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        pHaut.setPreferredSize(new Dimension(1800,120));
        pHaut.setBackground(Color.lightGray);
        GridBagConstraints gbcHaut = new GridBagConstraints();
        gbcHaut.insets = new Insets(0, 100, 0, 100);
        
        nom_m = new JLabel("Chargez ou creez un district");
        btNouvelle_map = new JButton("Nouveau district");
        
        gbcHaut.gridheight = 2;
        gbcHaut.gridx = 0; gbcHaut.gridy = 0;
        pHaut.add(nom_m, gbcHaut);
                       gbcHaut.gridy = 2;
        pHaut.add(btNouvelle_map, gbcHaut);
        
        //Vehicule
        gbcHaut.insets = new Insets(0, 80, 0, 180);
        vehicule = new JLabel("Véhicule");
        intervention = new JLabel("0 véhicules en tournée", JLabel.CENTER);
        gbcHaut.gridx = 1; gbcHaut.gridy = 0;
        pHaut.add(vehicule, gbcHaut);
                       gbcHaut.gridy = 2;
        pHaut.add(intervention, gbcHaut);
        gbcHaut.gridheight = 1;
        
        //Type De Centre
        gbcHaut.insets = new Insets(5, 20, 5, 20); // Pour ne pas avoir trop d'espaces entre les infos des centres
        type_centre = new JLabel("Type De Centre", JLabel.CENTER);
        nutrition = new JLabel("Nutrition");
        nbr_n = new JLabel("0");
        couleur_n = InfosCouleursCentres(0);
        
        maternite = new JLabel("Maternité");
        nbr_m = new JLabel("0");
        couleur_m = InfosCouleursCentres(1);
        
        bloc_operatoire= new JLabel("Bloc Opératoire");
        nbr_bo = new JLabel("0");
        couleur_bo = InfosCouleursCentres(2);
        
        gbcHaut.gridwidth = 3;
        gbcHaut.gridx = 2; gbcHaut.gridy = 0;
        pHaut.add(type_centre, gbcHaut);  
        
        gbcHaut.gridwidth = 1;
        gbcHaut.anchor = GridBagConstraints.LINE_START;
        gbcHaut.gridx = 2; gbcHaut.gridy = 1;
        pHaut.add(couleur_n, gbcHaut);
        gbcHaut.anchor = GridBagConstraints.CENTER;
        gbcHaut.gridx = 3;
        pHaut.add(nutrition, gbcHaut);
        gbcHaut.gridx = 4; 
        pHaut.add(nbr_n, gbcHaut);
        
        gbcHaut.anchor = GridBagConstraints.LINE_START;
        gbcHaut.gridx = 2; gbcHaut.gridy = 2;
        pHaut.add(couleur_m, gbcHaut);
        gbcHaut.anchor = GridBagConstraints.CENTER;
        gbcHaut.gridx = 3;
        pHaut.add(maternite, gbcHaut);
        gbcHaut.gridx = 4; 
        pHaut.add(nbr_m, gbcHaut);
        
        gbcHaut.anchor = GridBagConstraints.LINE_START;
        gbcHaut.gridx = 2; gbcHaut.gridy = 3;
        pHaut.add(couleur_bo, gbcHaut);
        gbcHaut.anchor = GridBagConstraints.CENTER;
        gbcHaut.gridx = 3;
        pHaut.add(bloc_operatoire, gbcHaut);
        gbcHaut.gridx = 4; 
        pHaut.add(nbr_bo, gbcHaut);
        
        //Nouveau trajet
        gbcHaut.insets = new Insets(10, 100, 10, 100);
        btNouveau_trajet = new JButton("Nouveau Trajet");
         
        gbcHaut.gridheight = 4;
        gbcHaut.gridx = 5; gbcHaut.gridy = 0;
        pHaut.add(btNouveau_trajet, gbcHaut);
        
        pPrincipal.add(pHaut, BorderLayout.NORTH);
        
        //MAP
        pMap = new JPanel(new BorderLayout());
        pMap.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        pMap.setPreferredSize(new Dimension(700,500));
        grapheActuel.visualisationGraphe();
        Viewer viewer = new Viewer(grapheActuel, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        viewer.enableAutoLayout();// affichage automatique
        DefaultView view=(DefaultView) viewer.addDefaultView(false);
        
        pMap.add((Component) view, BorderLayout.CENTER);
        
        coutEtTrajet = new JLabel("", JLabel.LEFT);
        pMap.add(coutEtTrajet, BorderLayout.SOUTH);
        
        pPrincipal.add(pMap,BorderLayout.CENTER);
        
        //PARTIE EST 
        JPanel pEst = new JPanel(new GridBagLayout());
        pEst.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        GridBagConstraints gbcEst = new GridBagConstraints();
        gbcEst.insets = new Insets(20, 10, 20, 10);
        pEst.setPreferredSize(new Dimension(200,650));
        pEst.setBackground(Color.lightGray);
        
        titreComboC = new JLabel("Centres de santé");
        gbcEst.gridx = 0; gbcEst.gridy = 0; 
        pEst.add(titreComboC, gbcEst);
        
        pPrincipal.add(pEst, BorderLayout.EAST);
        
        this.pack();
        this.setContentPane(pPrincipal);
    }
    
    private JPanel InfosCouleursCentres(int type){
        JPanel couleur = new JPanel();
        switch(type){
            case 0 : couleur.setBackground(Color.decode("#93FA8F")); break;
            case 1 : couleur.setBackground(Color.decode("#FA8FF1")); break;
            case 2 : couleur.setBackground(Color.decode("#8FB6FA")); break;
        }
        couleur.setPreferredSize(new Dimension(15,15));
        couleur.setVisible(true);
        couleur.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        return couleur;
    }
    
    private void testGraphPresent(){
        if(grapheActuel.getEstVide() == true){
            initEcouteurs(false);
        }
        else 
            initEcouteurs(true);
        System.out.println("le graphe est vide : " + grapheActuel.getEstVide());
    }
    
    private void initEcouteurs(boolean graphPresent){
        //Partie Haut
        btNouvelle_map.addActionListener(e -> {
            try {
                popup_nouv_district();
            } catch (IOException ex) {
                System.getLogger(MaFenetre.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        });
        if (graphPresent == true) {
            btNouveau_trajet.setEnabled(true);
            checkFia.setEnabled(true);
            checkDuree.setEnabled(true);
            checkDistance.setEnabled(true);
            //Nouveau trajet 
            btNouveau_trajet.addActionListener(e -> popup_nouv_t());

            //Menu
            checkFia.addActionListener((ActionEvent e) -> {
                initLabelRoutes();
            });
            checkDuree.addActionListener((ActionEvent e) -> {
                initLabelRoutes();
            });
            checkDistance.addActionListener((ActionEvent e) -> {
                initLabelRoutes();
            });
        } else if (graphPresent == false) {
            btNouveau_trajet.setEnabled(false);
            checkFia.setEnabled(false);
            checkDuree.setEnabled(false);
            checkDistance.setEnabled(false);
        }
    }
    
    private void initLabelRoutes(){
        for (Edge route : grapheActuel.getEachEdge()) {
            String label = "";

            if (checkFia.isSelected()) {
                label += route.getAttribute("fiabilite") + " fia";
            }

            if (checkDuree.isSelected()) {
                if (!label.equals("")) {
                    label += " | ";
                }
                label += route.getAttribute("duree") + " min";
            }

            if (checkDistance.isSelected()) {
                if (!label.equals("")) {
                    label += " | ";
                }
                label += route.getAttribute("distance") + " km";
            }

            if (label.equals("")) {
                route.removeAttribute("ui.label");
            } else {
                route.addAttribute("ui.label", label);
            }

            route.addAttribute("ui.style", "text-size: 15; text-alignment : along;");
        }
    }
    
    private void recharger_infos(){
        coutEtTrajet.setText("");
        pMap.revalidate();
        pMap.repaint();
        grapheActuel.visualisationGraphe();
        int nu = 0, m = 0, o = 0;
        for(Node n : grapheActuel.getEachNode()){
            if(n.getAttribute("type").equals("N"))
                nu ++;
            else if(n.getAttribute("type").equals("M"))
                m++;
            else if(n.getAttribute("type").equals("O"))
                o++;
            else
                throw new IllegalArgumentException("Type invalide : " + n.getAttribute("type")); //REVERIFIER OU VONT TOUTES LES EXCEPTIONS PAR RAPPORT A CA
        }
        nbr_bo.setText("" + o);
        nbr_m.setText("" + m);
        nbr_n.setText("" +  nu); 
    }
    
    private void popup_nouv_district() throws IOException{
        checkFia.setSelected(false);
        checkDistance.setSelected(false);
        checkDuree.setSelected(false);
        
        String[] options = {"Import", "Création aléatoire", "Annuler"};
        int choix = JOptionPane.showOptionDialog(this, "Comment voulez vous créer votre nouveau district ?", "Choix du district", 0, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
        
        switch (choix) {
            case 0:
                f = new JFileChooser();
                FileNameExtensionFilter filtre = new FileNameExtensionFilter("Fichier exel", "csv");
                f.setFileFilter(filtre);
                
                if (f.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    String fichier = f.getSelectedFile().getName();
                    
                    grapheActuel.clear();
                    grapheActuel.chargerGraphe(fichier);
                    recharger_infos();
                    nom_m.setText("District du fichier " + fichier);
                }
                //else //BIEN METTRE UNE EXCEPTION
                break;

            case 1:
                String reponse = JOptionPane.showInputDialog(this, "Combien de centres voulez vous dans votre district ?", "Choix nombre de centres", JOptionPane.QUESTION_MESSAGE);
                grapheActuel.clear();
                grapheActuel.creerGrapheAleatoire(Integer.parseInt(reponse));
                recharger_infos();
                nom_m.setText("District aléatoire de " + grapheActuel.getNodeCount() + " centres");
                break;

            case 2:
                break;
                
            default:
                throw new IllegalStateException("Unexpected value: " + choix);
        }
        testGraphPresent();
    }
    
    private void popup_nouv_t(){
        coutEtTrajet.setText("");
        chemin.clearVisualisations();
        String[] options = {"Itinéraire entre centres", "Tournée Véhicule", "Annuler"};
        int choix = JOptionPane.showOptionDialog(this, "Quel type de trajet voulez-vous ?", "Choix du trajet", 0, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
        
        switch (choix) {
            case 0:
                String choixDepart  = JOptionPane.showInputDialog( null, "De quel centre voulez vous partir", "Choix du départ du chemin", JOptionPane.QUESTION_MESSAGE) ;
                String choixArrivee  = JOptionPane.showInputDialog( null, "A quel centre voulez vous arriver", "Choix de l'arrivée du chemin", JOptionPane.QUESTION_MESSAGE) ;
                Node depart = grapheActuel.getNode(choixDepart.toUpperCase());
                Node arrivee = grapheActuel.getNode(choixArrivee.toUpperCase());
                
                String[] optionsCh = {"Chemin le plus rapide", "Chemin rapide et fiable", "Annuler"};
                int choixCh = JOptionPane.showOptionDialog(this, "Quel type de chemin voulez-vous ?", "Choix du chemin", 0, JOptionPane.QUESTION_MESSAGE, null, optionsCh, optionsCh[2]);
                switch (choixCh) {
                    case 0:
                        chemin.cheminDureeMinDijkstra(depart, arrivee);
                        chemin.visualisationChemin("dureeMin");
                        coutEtTrajet.setForeground(Color.RED);
                        coutEtTrajet.setText("Durée : " + chemin.getDureeTotale() + " min. " + chemin.getCentresTraverses());
                        break;
                    case 1:
                        chemin.cheminDureeEstimeeDijkstra(depart, arrivee);
                        chemin.visualisationChemin("dureeEstimee");
                        coutEtTrajet.setForeground(Color.GREEN);
                        coutEtTrajet.setText("Durée estimée avec fiabilitée : " + chemin.getDureeEstimee() + " min. " + chemin.getCentresTraverses());
                        break;
                    default:
                        throw new AssertionError();
                        //FAIRE UNE EXCEPTION POUR DIRE QUE LES CHEMINS DE D2PART ET ARRIVEES N4EXISTENT PAS OU AUTRE ERREURS
                }
                break;

            case 1:
                String choixNbV  = JOptionPane.showInputDialog( null, "Combien de véhicules voulez vous utiliser ?", "Choix du nombre de véhicules", JOptionPane.QUESTION_MESSAGE) ;
                int NbV = Integer.parseInt(choixNbV);
                
                switch (NbV) {
                    case 1:
                        System.out.println("On a pas encore cette possibilité");
                        break;
                    case 2:
                        System.out.println("On a pas encore cette possibilité");
                        break;
                    default:
                        throw new AssertionError();
                        //FAIRE UNE EXCEPTION PAR RAPPORT AU NB CE V2HICULES OU AUTRE ERREURS
                } break;

            case 2:break;
                
            default:
                throw new IllegalStateException("Unexpected value: " + choix);
                //FAIRE UNE EXCEPTION PAR RAPPORT AU CHOIX INNEXISTANT
        }
    }
    
    private void popup_infos_centre(){
        JDialog popup = new JDialog(this, "Information Centre", true);
        titre_ic = new JLabel("Information Centre");
        t_nom_ic = new JLabel("Nom");
        
        JPanel pan = new JPanel(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; 
        pan.add(titre_ic, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2; 
        pan.add(t_nom_ic, gbc);
        
        popup.setContentPane(pan);
        popup.pack(); 
        popup.setLocationRelativeTo(this); 
        popup.setVisible(true); 
        
    }
    
    
}
