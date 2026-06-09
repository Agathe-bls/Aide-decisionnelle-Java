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
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
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
 * @author zynga
 */
public class MaFenetre extends JFrame {
    MonGraphe grapheActuel = new MonGraphe("gActuel");


    //Partie Haut 1
    private JLabel nom_m;
    private JButton nouvelle_map;
    public static JFileChooser f;
    
    //Partie Haut Vehicules
    private JLabel vehicule;
    private JLabel intervention;
    
    //Partie Haut Centres
    private JLabel type_centre;
    private JLabel nutrition;
    private JLabel nbr_n;
    private JLabel maternite;
    private JLabel nbr_m;
    private JLabel bloc_operatoire;
    private JLabel nbr_bo;
    
    //Partie Haut Nouveau trajets
    private JButton nouveau_trajet;
    
    //Map
    private JPanel pMap;
    Chemin chemin = new Chemin(grapheActuel);
    JLabel coutEtTrajet;
    
    //pop-up Nouveau Trajet
    private JLabel centre_d;
    private JButton ce_d;
    private JLabel centre_a;
    private JButton ce_a;
    private JLabel trajet_c;
    private JButton envoyer;
    private JButton annuler;
   
    
    //popup info centre
    private JLabel titre_ic;
    private JLabel t_nom_ic;
    private JLabel id_m;
    private JLabel id_bo;
    
    //Est 
    
    private JLabel titre_un;
    private DefaultListModel modeleV;
    
    private JLabel titre_deux;
    private DefaultListModel modeleC;
    
    public MaFenetre(){
        super("MedicOne");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        initComposantsPrincipaux();
        
        this.pack();
        this.setVisible(true);
    }
    
    private void initComposantsPrincipaux(){
        JPanel pPrincipal = new JPanel(new BorderLayout());
        //pPrincipal.setPreferredSize(new Dimension(720,512));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 35, 4, 35);
        
        //Partie Haut
        JPanel pHaut = new JPanel(new GridBagLayout());
        pHaut.setBackground(Color.lightGray);
        nom_m = new JLabel("Nom du district actuel");
        nouvelle_map = new JButton("Nouveau district");
        nouvelle_map.addActionListener(e -> {
            try {
                popup_nouv_district();
            } catch (IOException ex) {
                System.getLogger(MaFenetre.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            } 
        }) ;
        
        gbc.gridx = 0; gbc.gridy = 0;
        pHaut.add(nom_m, gbc);
        gbc.gridheight = 3;
                       gbc.gridy = 1;
        pHaut.add(nouvelle_map, gbc);
        gbc.gridheight = 1;
        
        //Vehicule
        vehicule = new JLabel("Véhicule");
        intervention = new JLabel("0 véhicules en tournée", JLabel.CENTER);
        gbc.gridx = 1; gbc.gridy = 0;
        pHaut.add(vehicule, gbc);
                       gbc.gridy = 1;
        pHaut.add(intervention, gbc);
        
        
        //Type De Centre
        type_centre = new JLabel("Type De Centre", JLabel.CENTER);
        nutrition = new JLabel("Nutrition");
        nbr_n = new JLabel("0");
        maternite = new JLabel("Maternité");
        nbr_m = new JLabel("0");
        bloc_operatoire= new JLabel("Bloc Opératoire");
        nbr_bo = new JLabel("0");
        
        gbc.gridwidth = 2;
        gbc.gridx = 2; gbc.gridy = 0;
        pHaut.add(type_centre, gbc);  
        
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridwidth = 1;
        gbc.gridx = 2; gbc.gridy = 1;
        pHaut.add(nutrition, gbc);
        gbc.gridx = 3; gbc.gridy = 1;
        pHaut.add(nbr_n, gbc);
        
        gbc.gridx = 2; gbc.gridy = 2;
        pHaut.add(maternite, gbc);
        gbc.gridx = 3; gbc.gridy = 2;
        pHaut.add(nbr_m, gbc);
        
        gbc.gridx = 2; gbc.gridy = 3;
        pHaut.add(bloc_operatoire, gbc);
        gbc.gridx = 3; gbc.gridy = 3;
        pHaut.add(nbr_bo, gbc);
        gbc.anchor = GridBagConstraints.CENTER;
        
        //Nouveau trajet
        nouveau_trajet = new JButton("Nouveau Trajet");
        nouveau_trajet.addActionListener(e -> popup_nouv_t()) ;
         
        gbc.gridheight = 4;
        gbc.gridx = 4; gbc.gridy = 0;
        pHaut.add(nouveau_trajet, gbc);
        
        pPrincipal.add(pHaut, BorderLayout.NORTH);
        
        //MAP
        pMap = new JPanel(new BorderLayout());
        pMap.setPreferredSize(new Dimension(700,500));
        grapheActuel.visualisationGraphe();
        Viewer viewer = new Viewer(grapheActuel, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        viewer.enableAutoLayout();// affichage automatique
       
        DefaultView view=(DefaultView) viewer.addDefaultView(false);
        
        pMap.add((Component) view, BorderLayout.CENTER);
        
        coutEtTrajet = new JLabel("", JLabel.LEFT);
        coutEtTrajet.setBackground(BLACK);        //LE BACKGROUND MARCHE PAS
        coutEtTrajet.setFont(new Font("Serif", Font.BOLD, 15));
        pMap.add(coutEtTrajet, BorderLayout.SOUTH);
        
        pPrincipal.add(pMap,BorderLayout.CENTER);
        
        //PARTIE EST 
        
        JPanel pEst = new JPanel(new GridBagLayout());
        
        titre_un = new JLabel("Véhicules");
         titre_deux = new JLabel("Centres de santé");
        
        modeleV = new DefaultListModel();
        modeleV.addElement("Véhicule 1");
        modeleV.addElement("Véhicule 2");
        JList liste = new JList<>(modeleV);
        
        gbc.gridx = 0; gbc.gridy = 0;
        pEst.add(titre_un, gbc);
                       gbc.gridy = 1;
                       
                      
        pEst.add(liste, gbc);
        pEst.add(titre_deux, gbc);
        
        modeleC = new DefaultListModel();
        for(Node n : grapheActuel.getEachNode()){
            modeleC.addElement("Centre" + n);
        }
        
        JList listeC = new JList<>(modeleC);
        
        pEst.add(listeC, gbc);
        pEst.setLayout(new GridLayout(10, 10, 10, 10));
        pPrincipal.add(pEst, BorderLayout.EAST);
        
        this.pack();
        this.setContentPane(pPrincipal);
    }
    
    private void popup_nouv_district() throws IOException{
        String[] options = {"Import", "Création aléatoire", "Annuler"};
        int choix = JOptionPane.showOptionDialog(this, "Comment voulez vous créer votre nouveau district ?", "Choix du district", 0, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
        
        switch (choix) {
            case 0:
                f = new JFileChooser();
                FileNameExtensionFilter filtre = new FileNameExtensionFilter("Fichiers texte", "txt", "Fichier exel", "csv");
                f.setFileFilter(filtre);
                
                
                if (f.showOpenDialog(MaFenetre.this) == JFileChooser.APPROVE_OPTION) {
                    PrintWriter pw = new PrintWriter(new FileWriter(f.getSelectedFile()));
                    
                    //FileReader rd = new FileReader(args[0]);
                    
                    grapheActuel.clear();
                    System.out.println(pw);
                    grapheActuel.chargerGraphe("" + pw);
                    recharger_infos();
                }
                //else //BIEN METTRE UNE EXCEPTION
                break;

            case 1:
                String reponse = JOptionPane.showInputDialog(this, "Combien de centres voulez vous dans votre district ?", "Choix nombre de centres", JOptionPane.QUESTION_MESSAGE);
                grapheActuel.clear();
                grapheActuel.creerGrapheAleatoire(Integer.parseInt(reponse));
                recharger_infos();
                break;

            case 2:
                break;
                
            default:
                throw new IllegalStateException("Unexpected value: " + choix);
        }
    }
    
    private void recharger_infos(){
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
    
    private void popup_nouv_t(){
        
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
                        coutEtTrajet.setText("Durée estimée avec fiabilitée : " + chemin.getDureeEstimee() + " min." + chemin.getCentresTraverses());
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
    
    
}
