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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
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
import org.graphstream.ui.swingViewer.DefaultView;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;
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
    private JLabel nbr_v_i;
    private JLabel attente;
    private JLabel nbr_v_a;
    private JLabel retour;
    private JLabel nbr_v_r;
    
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
    
    //pop-up Nouveau Trajet
    private JLabel centre_d;
    private JButton ce_d;
    private JLabel centre_a;
    private JButton ce_a;
    private JLabel trajet_c;
    private JButton envoyer;
    private JButton annuler;
    private DefaultListModel monModele;
    
    //popup info centre
    private JLabel titre_ic;
    private JLabel t_nom_ic;
    private JLabel id_m;
    private JLabel id_bo;
    
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
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 20, 2, 20);
        
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
            } }) ;
        
        gbc.gridx = 0; gbc.gridy = 0;
        pHaut.add(nom_m, gbc);
        gbc.gridheight = 2;
                       gbc.gridy = 1;
        pHaut.add(nouvelle_map, gbc);
        gbc.gridheight = 1;
        
        //Vehicule
        vehicule = new JLabel("Véhicule", JLabel.CENTER);
 
        gbc.gridx = 1; gbc.gridy = 0;
        pHaut.add(vehicule, gbc);
        
        //Type De Centre
        type_centre = new JLabel("Type De Centre", JLabel.CENTER);
        nutrition = new JLabel("Nutrition");
        nbr_n = new JLabel("0");
        maternite = new JLabel("Maternité");
        nbr_m = new JLabel("0");
        bloc_operatoire= new JLabel("Bloc Opératoire");
        nbr_bo = new JLabel("0");
        
        gbc.gridwidth = 2;
        gbc.gridx = 3; gbc.gridy = 0;
        pHaut.add(type_centre, gbc);  
        
        gbc.gridwidth = 1;
        gbc.gridx = 3; gbc.gridy = 1;
        pHaut.add(nutrition, gbc);
        gbc.gridx = 4; gbc.gridy = 1;
        pHaut.add(nbr_n, gbc);
        
        gbc.gridx = 3; gbc.gridy = 2;
        pHaut.add(maternite, gbc);
        gbc.gridx = 4; gbc.gridy = 2;
        pHaut.add(nbr_m, gbc);
        
        gbc.gridx = 3; gbc.gridy = 3;
        pHaut.add(bloc_operatoire, gbc);
        gbc.gridx = 4; gbc.gridy = 3;
        pHaut.add(nbr_bo, gbc);
        
        //Nouveau trajet
        nouveau_trajet = new JButton("Nouveau Trajet");
        nouveau_trajet.addActionListener(e -> popup_nouv_t()) ;
         
        gbc.gridheight = 3;
        gbc.gridx = 5; gbc.gridy = 0;
        pHaut.add(nouveau_trajet, gbc);
        
        pPrincipal.add(pHaut, BorderLayout.NORTH);
        
        //MAP
        pMap = new JPanel(new BorderLayout());
        pMap.setPreferredSize(new Dimension(500,600));
        grapheActuel.visualisationGraphe();
        Viewer viewer = new Viewer(grapheActuel, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        viewer.enableAutoLayout();// affichage automatique
       
        DefaultView view=(DefaultView) viewer.addDefaultView(false);
        
        pMap.add((Component) view, BorderLayout.CENTER);
//        pMap.revalidate();
//        pMap.repaint();
        
        pPrincipal.add(pMap, BorderLayout.PAGE_END);
        
        this.pack();
        this.setContentPane(pPrincipal);
    }
    
    private void popup_nouv_district() throws IOException{
        String[] options = {"Import", "Création aléatoire", "Annuler"};
        int choix = JOptionPane.showOptionDialog(this, "Comment voulez vous créer votre nouveau district ?", "Choix du district", 0, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
        
        switch (choix) {
            case 0:
                f = new JFileChooser();
                FileNameExtensionFilter filtre = new FileNameExtensionFilter("Fichiers texte", "txt");
                f.setFileFilter(filtre);
                
                if (f.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    PrintWriter pw = new PrintWriter(new FileWriter(f.getSelectedFile()));
                    grapheActuel.chargerGraphe("" + pw);
                }
                //else //BIEN METTRE UNE EXCEPTION
                break;

            case 1:
                String reponse = JOptionPane.showInputDialog(this, "Combien de centres voulez vous dans votre district ?", "Choix nombre de centres", JOptionPane.QUESTION_MESSAGE);
                grapheActuel.creerGrapheAleatoire(Integer.parseInt(reponse));
                break;

            case 2:
                //Je ne sais pas si c'est possible sinon on enlève le bouton annuler
                break;
                
            default:
                throw new IllegalStateException("Unexpected value: " + choix);
        }
    
    }
   
    private void popup_infos_centre(){
        JDialog popup = new JDialog(this, "Information Centre", true);
        JLabel titre_ic = new JLabel("Information Centre");
        JLabel t_nom_ic = new JLabel("Nom");
        
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
        JDialog popup = new JDialog(this, "Nouveau Trajet", true);
        JLabel centre_d = new JLabel("Centre de départ : ", JLabel.CENTER);
        JButton ce_d = new JButton("Choisir");
        
        JLabel centre_a = new JLabel("Centre d'arrivé : ", JLabel.CENTER);
        JButton ce_a = new JButton("Choisir");
        
        JLabel trajet_c = new JLabel("Trajet choisi", JLabel.CENTER);
        
        JButton envoyer = new JButton("Envoyer");
        JButton annuler = new JButton("Annuler");
        
        monModele = new DefaultListModel();
        monModele.addElement("Trajet 1");
        monModele.addElement("Trajet 2");
        monModele.addElement("Trajet 3");
        JList liste_t = new JList<>(monModele);
        
        JPanel pan = new JPanel(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        
        JPanel pnlBoutons = new JPanel(new GridLayout(1, 2, 15, 0));
        pnlBoutons.setOpaque(false); // Pour garder le même fond
        pnlBoutons.add(envoyer);
        pnlBoutons.add(annuler);
    
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; 
        pan.add(centre_d, gbc);
        gbc.gridy = 1;
        pan.add(ce_d, gbc);
        gbc.gridy = 2;
        pan.add(centre_a, gbc);
        gbc.gridy = 3;
        pan.add(ce_a, gbc);
        gbc.gridy = 4;
        pan.add(trajet_c, gbc);
        gbc.gridy = 5;
        pan.add(liste_t, gbc);
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.HORIZONTAL; 
        gbc.weightx = 1.0;                        
        pan.add(pnlBoutons, gbc);
        
        popup.setContentPane(pan);
        popup.pack(); 
        popup.setLocationRelativeTo(this); 
        popup.setVisible(true); 
    }
    
    
}
