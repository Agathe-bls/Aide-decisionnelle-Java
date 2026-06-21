/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Fenetre;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.ui.graphicGraph.GraphicElement;
import org.graphstream.ui.swingViewer.DefaultView;
import org.graphstream.ui.view.Viewer;

import Exceptions.NombreVehiculeInvalideException;
import saejava.classes.MonGraphe;
import saejava.classes.Chemin;
import saejava.classes.Tournee;

public class MaFenetre extends JFrame {

//PARTIE MAP
    private DefaultView view;
    private final MonGraphe grapheActuel = new MonGraphe("gActuel"); //final car même si on fait des actions dessus pour le modifier il ne "change" jamais
    private JPanel pMap;
    
//PARTIE HAUT
    //Nouvelle-map
    private JLabel nom_m;
    private JButton btNouvelle_map;
    public static JFileChooser f;
    
    //Vehicules
    private JLabel vehicule;
    private JLabel intervention;
    
    //Centres
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
    
    //Nouveau trajets
    private JButton btNouveau_trajet;
    private final Chemin chemin = new Chemin(grapheActuel); //final car même si on fait des actions dessus pour le modifier il ne "change" jamais
    
//PARTIE BAS
    private JLabel coutEtTrajet;
    private JLabel coutEtTrajetV2;
    private JScrollPane scrollPane;
    private JScrollPane scrollPaneV2;
    
    private Tournee tournee1V;
    private Tournee tournee2V;
    
//PARTIE EST 
    private JLabel titreComboC;
    
//MENU
    private JMenuBar menuBar;
    private JMenu menuCentres;
    private JMenu menuRoutes;
    private JMenu menuSeuil;
    private JCheckBoxMenuItem checkType;
    private JCheckBoxMenuItem checkNom;
    private JCheckBoxMenuItem checkFia;
    private JCheckBoxMenuItem checkDuree;
    private JCheckBoxMenuItem checkDistance;
    private JSpinner seuil_test;
    
//Infos au clic
    private PopupInfosCentres popupInfoC;

    /**
     * Construit la fênetre principalede l'application.
     * Initialise l'interface graphique, son menu, ses composants, ses écouteurs, ses paramètres d'affichage ainsi que la visualisation initiale du graphe.
     * La fenêtre est ensuite étendue sur tout l'écrant et rendue visible.
     *
     * @author Balsan Agathe
     */
    public MaFenetre(){
        super("MedicOne");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        
        créationMenu();
        initComposants();
        initEcouteurs();
        testGraphPresent();
        visuSeuil();
        gererTailleTextes();
        
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);
    }
    
    private void créationMenu(){
        menuBar = new JMenuBar();
        MaFenetre.this.setJMenuBar(menuBar);
        
        menuCentres = new JMenu("Informations des centres");
        menuRoutes = new JMenu("Informations des routes");
        menuSeuil = new JMenu("Seuil de dangerosité");
        
        checkNom = new JCheckBoxMenuItem("Noms des centres");
        checkNom.setSelected(true);
        checkType = new JCheckBoxMenuItem("Types des centres");
        checkFia = new JCheckBoxMenuItem("Fiabilités");
        checkDuree = new JCheckBoxMenuItem("Durées");
        checkDistance = new JCheckBoxMenuItem("Distances");
        seuil_test = new JSpinner(new SpinnerNumberModel(0.1,0.1,1,0.1));
        seuil_test.setPreferredSize(new Dimension(173,20));
        
        menuCentres.add(checkNom);
        menuCentres.add(checkType);

        menuRoutes.add(checkFia);
        menuRoutes.add(checkDuree);
        menuRoutes.add(checkDistance);
        
        menuSeuil.add(seuil_test);
        
        menuBar.add(menuCentres);
        menuBar.add(menuRoutes);
        menuBar.add(menuSeuil);
    }
    
    private void initComposants(){ 
        JPanel pPrincipal = new JPanel(new BorderLayout());
        
        //PARTIE HAUT 
        //Nouvelle-map
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
        
        //Centres
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
        Viewer viewer = new Viewer(grapheActuel, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        viewer.enableAutoLayout();// affichage automatique
        this.view=(DefaultView) viewer.addDefaultView(false);
        
        pMap.add((Component) this.view, BorderLayout.CENTER);
        
        pPrincipal.add(pMap,BorderLayout.CENTER);
        
        //PARTIE BAS
        JPanel pBas = new JPanel(new BorderLayout());
        
        coutEtTrajet = new JLabel("", JLabel.CENTER);
        coutEtTrajetV2 = new JLabel("", JLabel.CENTER);
        
        scrollPane = new JScrollPane(coutEtTrajet);
        scrollPane.setPreferredSize(new Dimension(768, 50));
        pBas.add(scrollPane, BorderLayout.WEST);
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        scrollPaneV2 = new JScrollPane(coutEtTrajetV2);
        scrollPaneV2.setPreferredSize(new Dimension(768, 50));
        pBas.add(scrollPaneV2, BorderLayout.EAST);
        scrollPaneV2.getViewport().setBackground(Color.WHITE);
        
        pPrincipal.add(pBas, BorderLayout.SOUTH);
        
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
    
    private void initEcouteurs() {
        //PARTIE HAUT
        btNouvelle_map.addActionListener((ActionEvent e) -> {
            try {
                popup_nouv_district();
            } catch (IOException ex) {
                System.getLogger(MaFenetre.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        });

        
        view.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (javax.swing.SwingUtilities.isRightMouseButton(e)) {
                    GraphicsConfiguration gc = view.getGraphicsConfiguration();
                    AffineTransform tx = gc.getDefaultTransform();

                    float abscisse = (float) (e.getX() * tx.getScaleX());
                    float ordonnee = (float) (e.getY() * tx.getScaleY());

                    GraphicElement element = view.findNodeOrSpriteAt(abscisse, ordonnee);
                    System.out.println(element);
                    if (element != null) {
                        Node n = grapheActuel.getNode(element.getId());
                        if (n != null) {
                            //popup_infos_centre(n);
                            popupInfoC = new PopupInfosCentres(MaFenetre.this, grapheActuel, n);
                        }
                    }
                }
            }
        });
   
        
        //Nouveau trajet 
        btNouveau_trajet.addActionListener(e -> {
            try {
                popup_nouv_t();
            } catch (NombreVehiculeInvalideException ex) {
                System.out.println(ex.getMessage());
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        //MENU
        checkFia.addActionListener((ActionEvent e) -> {
            labelCheckRoutes();
        });
        checkDuree.addActionListener((ActionEvent e) -> {
            labelCheckRoutes();
        });
        checkDistance.addActionListener((ActionEvent e) -> {
            labelCheckRoutes();
        });
        
        checkNom.addActionListener((ActionEvent e) -> {
            labelCheckCentres();
        });
        
        checkType.addActionListener((ActionEvent e) -> {
            labelCheckCentres();
        });
    }
    
    private void popup_nouv_district() throws IOException{
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
                    nom_m.setText("District du fichier " + fichier);
                }
                //else //BIEN METTRE UNE EXCEPTION
                break;

            case 1:
                String reponse = JOptionPane.showInputDialog(this, "Combien de centres voulez vous dans votre district ?", "Choix nombre de centres", JOptionPane.QUESTION_MESSAGE);
                try {
                grapheActuel.clear();
                grapheActuel.creerGrapheAlea(Integer.parseInt(reponse));
                nom_m.setText("District aléatoire avec " + grapheActuel.getNodeCount() + " centres");
                } catch (java.lang.NumberFormatException ex) {
                    if (!"".equals(reponse)) {
                        System.out.println("Valeur trop grande : "+ reponse);
                        JOptionPane.showMessageDialog(this, "Valeur trop grande : "+reponse, "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                    else {
                        System.out.println("Pas de valeur rentrée");
                        JOptionPane.showMessageDialog(this, "Pas de valeur rentrée", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
                break;
            default:
                break;
        }
        recharger_infos();
    }
    
    private void popup_nouv_t() throws NombreVehiculeInvalideException {
        coutEtTrajet.setText("");
        coutEtTrajetV2.setText("");
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
                        coutEtTrajet.setForeground(Color.decode("#07b817"));
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
                        String choixTypeCentres[] = {"Nutrition", "Maternité", "Bloc opératoire", "Tous"};
                        int TypeCentres  = JOptionPane.showOptionDialog(this, "Quel type de centre voulez vous dans votre tournée?", "Choix du type des centres de la tournée", 0, JOptionPane.QUESTION_MESSAGE, null, choixTypeCentres, choixTypeCentres[3]) ;
                        
                        String type;
                        switch(TypeCentres){
                            case 0 : type = "N"; coutEtTrajet.setForeground(Color.decode("#6ac666")); break;
                            case 1 : type = "M"; coutEtTrajet.setForeground(Color.decode("#ee0bdc")); break;
                            case 2 : type = "O"; coutEtTrajet.setForeground(Color.decode("#5f97f7")); break;
                            default : type = null; coutEtTrajet.setForeground(Color.black); break;
                        }
                        tournee1V = new Tournee(grapheActuel, grapheActuel.getNode("S1"), 1);
                        tournee1V.calculerTourneeUnVehicule(type); //null / O / N / M
                        tournee1V.colorierTourneeUnVehicule(0);
                        coutEtTrajet.setText("Durée de la tournée : " + tournee1V.getDureeTotale(0) + " min. " + tournee1V.getLabelCentresLivres(0));
                        tournee1V.afficherTournee(0);
                        
                        
                        break;
                    case 2:
                        tournee2V = new Tournee(grapheActuel, grapheActuel.getNode("S1"), 2);
                        tournee2V.calculerTourneeDeuxVehicules();
                        
                        tournee2V.colorierTourneeUnVehicule(0);
                        tournee2V.colorierTourneeUnVehicule(1);
                        
                        coutEtTrajet.setForeground(Color.black);
                        coutEtTrajet.setText("Durée de la tournée de V1 : " + tournee2V.getDureeTotale(0) + " min. " + tournee2V.getLabelCentresLivres(0));
                        coutEtTrajetV2.setForeground(Color.decode("#b4083e"));
                        coutEtTrajetV2.setText(" Durée de la tournée de V2 : " + tournee2V.getDureeTotale(1) + " min. " + tournee2V.getLabelCentresLivres(1));
                        
                        tournee2V.afficherTournee(0);
                        tournee2V.afficherTournee(1);
                        break;
                    default:
                        throw new NombreVehiculeInvalideException();
                } break;

            case 2:break;
                
            default:
                break;
//                throw new IllegalStateException("Unexpected value: " + choix);
                //FAIRE UNE EXCEPTION PAR RAPPORT AU CHOIX INNEXISTANT IL FAUT PEUT ETRE METTRE UN DEFAULT EXIT OU JS QUOI
        }
    }
    
    private void recharger_infos(){
        testGraphPresent();
        coutEtTrajet.setText("");
        coutEtTrajetV2.setText("");
        grapheActuel.visualisationGraphe();
        labelCentresParType();
        
        seuil_test.setValue(0.2);
    }
    
    /**
     * Compte le nombre de centres de chaque type présents dans le graphe actuel.
     *
     * @return un tableau d'entiers contenant respectivement le nombre de centres de type
     * Nutrition (indice 0), Maternité (indice 1) et Bloc opératoire (indice 2)
     *
     * @author Balsan Agathe
     */
    public int[] nbCentresParType() {
        int[] tabNbParTypes = new int[3];
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
        
        tabNbParTypes[0] = nu;
        tabNbParTypes[1] = m;
        tabNbParTypes[2] = o;
        return tabNbParTypes;
    }
    
    private void labelCentresParType(){
        nbr_bo.setText("" + nbCentresParType()[2]);
        nbr_m.setText("" + nbCentresParType()[1]);
        nbr_n.setText("" +  nbCentresParType()[0]); 
    }
    
    private void testGraphPresent() {
        if (grapheActuel.getEstVide() == true) {
            btNouveau_trajet.setEnabled(false);
            checkFia.setEnabled(false);
            checkDuree.setEnabled(false);
            checkDistance.setEnabled(false);
            checkNom.setEnabled(false);
            checkType.setEnabled(false);
            seuil_test.setEnabled(false);
        } else {
            btNouveau_trajet.setEnabled(true);
            checkFia.setEnabled(true);
            checkDuree.setEnabled(true);
            checkDistance.setEnabled(true);
            checkNom.setEnabled(true);
            checkType.setEnabled(true);
            seuil_test.setEnabled(true);
        }
    }
    
    private void labelCheckRoutes(){
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
            route.addAttribute("ui.style", "text-size: 15;");       //LALIGNEMENT NE MARCHE PAS 
        }
    }
    
    private void labelCheckUneRoute(Edge route){
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
            
            route.addAttribute("ui.label", label);
            route.addAttribute("ui.style", "text-size: 15;");       //LALIGNEMENT NE MARCHE PAS
    }
    
    private void labelCheckCentres(){
        for(Node centre : grapheActuel.getEachNode()){
            String label = "";
            
            if(checkNom.isSelected()){
                label = centre.getId() + label;
            }
            if(checkType.isSelected()){
                if(!label.equals("")){
                    label += " | ";
                }
                label += centre.getAttribute("type");
            }
            centre.addAttribute("ui.label", label);
        }
    }
    
    private void visuSeuil() {
        seuil_test.addChangeListener((ChangeEvent e) -> {
            for (Edge route : grapheActuel.getEachEdge()) {
                double fiaRoute = (route.getAttribute("fiabilite"));
                double seuil = (double) seuil_test.getValue();
                if (fiaRoute < seuil) {
                    route.addAttribute("ui.label", " ⚠ ");
                    route.addAttribute("ui.style", "text-size: 25;");
                }
                else {
                    labelCheckUneRoute(route);
                }
            }
        });
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
    
    private void gererTailleTextes(){
        Font police = new Font("Arial", Font.ROMAN_BASELINE, 16);
        Font policeTitre = new Font("Arial", Font.BOLD, 17);
        //PARTIE HAUT
        //Nouvelle-map
        nom_m.setFont(policeTitre);
        btNouvelle_map.setFont(police);
        //Véhicules
        vehicule.setFont(policeTitre);
        intervention.setFont(police);
        //Centres
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
        //Nouveau trajets
         btNouveau_trajet.setFont(police);
         //MAP
         coutEtTrajet.setFont(police);
         coutEtTrajetV2.setFont(police);
         //PARTIE EST 
        titreComboC.setFont(policeTitre);
        //MENU
        menuCentres.setFont(policeTitre);
        menuRoutes.setFont(policeTitre);
        menuSeuil.setFont(policeTitre);
        checkType.setFont(police);
        checkNom.setFont(police);
        checkFia.setFont(police);
        checkDuree.setFont(police);
        checkDistance.setFont(police);
        seuil_test.setFont(police);
    }
}