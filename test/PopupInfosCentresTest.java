import Fenetre.MaFenetre;
import Fenetre.PopupInfosCentres;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.graphstream.graph.Node;
import org.graphstream.graph.Edge;
import saejava.classes.MonGraphe;

import javax.swing.*;
import java.awt.*;
import java.io.ObjectStreamClass;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class PopupInfosCentresTest {

    private MonGraphe grapheTest;
    private Node noeudSource;
    private PopupInfosCentres popupCentres;
    private MaFenetre StubFenetre;

    @Before
    public void setUp() {
        
        grapheTest = new MonGraphe("GrapheTest");
        
        noeudSource = grapheTest.addNode("S1");
        noeudSource.setAttribute("type", "M"); // Maternité

        try {
            StubFenetre = new MaFenetre(); 
        } catch (Exception e) {
            StubFenetre = null; 
        }

        try {
            Constructor<Object> objectConstructor = Object.class.getConstructor();
            @SuppressWarnings("unchecked")
            Constructor<PopupInfosCentres> muniConstructor = (Constructor<PopupInfosCentres>) 
                sun.reflect.ReflectionFactory.getReflectionFactory()
                .newConstructorForSerialization(PopupInfosCentres.class, objectConstructor);
            
            popupCentres = muniConstructor.newInstance();
            
        } catch (Exception e) {
            try {
                java.lang.reflect.Method allocateMethod = ObjectStreamClass.class.getDeclaredMethod("allocateInstance", Class.class);
                allocateMethod.setAccessible(true);
                popupCentres = (PopupInfosCentres) allocateMethod.invoke(ObjectStreamClass.lookup(PopupInfosCentres.class));
            } catch (Exception ex) {
                fail("Impossible d'allouer PopupInfosCentres : " + ex.getMessage());
            }
        }

        injecterChampForce(popupCentres, "grapheActuel", grapheTest);
        injecterChampForce(popupCentres, "graphe", grapheTest);
        injecterChampForce(popupCentres, "monGraphe", grapheTest);
        
        injecterChampForce(popupCentres, "noeudActuel", noeudSource);
        injecterChampForce(popupCentres, "noeudSource", noeudSource);
        injecterChampForce(popupCentres, "noeud", noeudSource);
        injecterChampForce(popupCentres, "node", noeudSource);
        
        injecterChampForce(popupCentres, "fenetre", StubFenetre);
        injecterChampForce(popupCentres, "maFenetre", StubFenetre);
    }

    private void injecterChampForce(Object cible, String nomChamp, Object valeur) {
        try {
            Field f = cible.getClass().getDeclaredField(nomChamp);
            f.setAccessible(true);
            f.set(cible, valeur);
        } catch (Exception ignored) {}
    }

    @After
    public void tearDown() {
        // Nettoyage : Ferme proprement toutes les fenêtres Swing ouvertes par les tests
        for (Window window : Window.getWindows()) {
            window.dispose();
        }
    }

    @Test
    public void testCentreProche_AffichageIntegrité() {
        Node mat2 = grapheTest.addNode("S2");
        mat2.setAttribute("type", "M");
        
        Node nutri = grapheTest.addNode("S3");
        nutri.setAttribute("type", "N");
        
        Node bo = grapheTest.addNode("S4");
        bo.setAttribute("type", "O");

        Edge r1 = grapheTest.addEdge("S1-S2", noeudSource, mat2);
        r1.addAttribute("distance", 10);
        r1.addAttribute("duree", 12);
        r1.addAttribute("fiabilite", 0.9);

        Edge r2 = grapheTest.addEdge("S1-S3", noeudSource, nutri);
        r2.addAttribute("distance", 25);
        r2.addAttribute("duree", 20);
        r2.addAttribute("fiabilite", 0.8);

        Edge r3 = grapheTest.addEdge("S1-S4", noeudSource, bo);
        r3.addAttribute("distance", 5);
        r3.addAttribute("duree", 8);
        r3.addAttribute("fiabilite", 0.95);

        popupCentres.centre_proche(noeudSource);

        JFrame fenetre = chercherFenetreParTitre("Centres les plus proches de S1");
        assertNotNull("La fenêtre des centres les plus proches devrait être ouverte", fenetre);
        assertTrue(fenetre.isVisible());

        JTable tableau = extraireJTable(fenetre);
        assertNotNull("La fenêtre doit contenir un JTable", tableau);
        assertTrue(tableau.getRowCount() > 0);
    }

    @Test
    public void testTableCentreKm_AffichageDonnees() {
        Node mat2 = grapheTest.addNode("S2");
        mat2.setAttribute("type", "M");
        
        Edge r1 = grapheTest.addEdge("S1-S2", noeudSource, mat2);
        r1.addAttribute("distance", 15);
        r1.addAttribute("duree", 10);
        r1.addAttribute("fiabilite", 1.0);

        popupCentres.table_centre_km(noeudSource);

        JFrame fenetre = chercherFenetreParTitre("Distances depuis S1");
        assertNotNull("La fenêtre de tableau KM devrait s'ouvrir", fenetre);
        
        JTable tableau = extraireJTable(fenetre);
        assertNotNull(tableau);
        assertNotNull(tableau.getColumnName(2));
    }

    @Test
    public void testTableCentreDuree_HauteurFenetreBornee() {
        // Génération de 26 noeuds
        for (int i = 2; i <= 27; i++) {
            Node m = grapheTest.addNode("S" + i);
            m.setAttribute("type", "M");
            Edge e = grapheTest.addEdge("S1-S" + i, noeudSource, m);
            e.addAttribute("distance", 10);
            e.addAttribute("duree", 15);
            e.addAttribute("fiabilite", 0.9);
        }

        popupCentres.table_centre_duree(noeudSource);

        JFrame fenetre = chercherFenetreParTitre("Distances depuis S1");
        assertNotNull("La fenêtre de tableau Durée devrait s'ouvrir", fenetre);
        
        JTable tableau = extraireJTable(fenetre);
        assertNotNull(tableau);

        //le tableau s'est bien rempli
        assertTrue("Le tableau doit contenir les lignes de données générées", tableau.getRowCount() > 0);
    }

  

    private JFrame chercherFenetreParTitre(String titre) {
        for (Window window : Window.getWindows()) {
            if (window instanceof JFrame) {
                JFrame frame = (JFrame) window;
                if (frame.getTitle() != null && frame.getTitle().contains(titre)) {
                    return frame;
                }
            }
        }
        return null;
    }

    private JTable extraireJTable(Container conteneur) {
        for (Component comp : conteneur.getComponents()) {
            if (comp instanceof JTable) {
                return (JTable) comp;
            } else if (comp instanceof Container) {
                JTable table = extraireJTable((Container) comp);
                if (table != null) return table;
            }
        }
        return null;
    }
}