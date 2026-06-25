import Exceptions.NombreVehiculeInvalideException;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import saejava.classes.MonGraphe;
import saejava.classes.Tournee;

/**
 *
 * @author Mattis
 */
public class TourneeTest {

    private MonGraphe graphe;

    @Before
    public void setUp() {
        graphe = new MonGraphe("grapheTest");

        Node s1 = graphe.addNode("S1");
        s1.setAttribute("type", "M");

        Node s2 = graphe.addNode("S2");
        s2.setAttribute("type", "N");

        Node s3 = graphe.addNode("S3");
        s3.setAttribute("type", "O");

        Node s4 = graphe.addNode("S4");
        s4.setAttribute("type", "M");

        ajouterArete("S1_S2", "S1", "S2", 10, 5, 0.9);
        ajouterArete("S2_S3", "S2", "S3", 20, 8, 0.8);
        ajouterArete("S3_S4", "S3", "S4", 30, 10, 0.7);
        ajouterArete("S1_S4", "S1", "S4", 100, 50, 0.6);
    }

    private void ajouterArete(String id, String depart, String arrivee, int duree, int distance, double fiabilite) {
        Edge e = graphe.addEdge(id, depart, arrivee);
        e.setAttribute("duree", duree);
        e.setAttribute("distance", distance);
        e.setAttribute("fiabilite", fiabilite);
    }

    @Test
    public void constructeurTourneeUnVehiculeTest() throws NombreVehiculeInvalideException {
        Tournee tournee = new Tournee(graphe, graphe.getNode("S1"), 1);

        assertNotNull(tournee);
        assertEquals(0, tournee.getDureeTotale(0));
    }

    @Test
    public void constructeurTourneeDeuxVehiculesTest() throws NombreVehiculeInvalideException {
        Tournee tournee = new Tournee(graphe, graphe.getNode("S1"), 2);

        assertNotNull(tournee);
        assertEquals(0, tournee.getDureeTotale(0));
        assertEquals(0, tournee.getDureeTotale(1));
    }

    @Test
    public void constructeurTourneeNombreVehiculeInvalideTest() {
        try {
            new Tournee(graphe, graphe.getNode("S1"), 3);
            fail("Une exception aurait dû être levée pour un nombre de véhicules invalide.");
        } catch (NombreVehiculeInvalideException e) {
            // Exception attendue.
        }
    }

    @Test
    public void getCentresALivrerTousTypesTest() throws NombreVehiculeInvalideException {
        Tournee tournee = new Tournee(graphe, graphe.getNode("S1"), 1);

        assertEquals(4, tournee.getCentresALivrer(null).size());
    }

    @Test
    public void getCentresALivrerMaternitesTest() throws NombreVehiculeInvalideException {
        Tournee tournee = new Tournee(graphe, graphe.getNode("S1"), 1);

        assertEquals(2, tournee.getCentresALivrer("M").size());
        assertTrue(tournee.getCentresALivrer("M").contains(graphe.getNode("S1")));
        assertTrue(tournee.getCentresALivrer("M").contains(graphe.getNode("S4")));
    }

    @Test
    public void getCentresALivrerNutritionTest() throws NombreVehiculeInvalideException {
        Tournee tournee = new Tournee(graphe, graphe.getNode("S1"), 1);

        assertEquals(1, tournee.getCentresALivrer("N").size());
        assertTrue(tournee.getCentresALivrer("N").contains(graphe.getNode("S2")));
    }

    @Test
    public void getCentresALivrerBlocOperatoireTest() throws NombreVehiculeInvalideException {
        Tournee tournee = new Tournee(graphe, graphe.getNode("S1"), 1);

        assertEquals(1, tournee.getCentresALivrer("O").size());
        assertTrue(tournee.getCentresALivrer("O").contains(graphe.getNode("S3")));
    }

    @Test
    public void calculerTourneeUnVehiculeTousCentresTest() throws NombreVehiculeInvalideException {
        Tournee tournee = new Tournee(graphe, graphe.getNode("S1"), 1);

        tournee.calculerTourneeUnVehicule(null);

        assertTrue(tournee.getDureeTotale(0) > 0);
        assertTrue(tournee.getLabelCentresLivres(0).contains("S1"));
        assertTrue(tournee.getLabelCentresLivres(0).contains("S2"));
        assertTrue(tournee.getLabelCentresLivres(0).contains("S3"));
        assertTrue(tournee.getLabelCentresLivres(0).contains("S4"));
    }

    @Test
    public void calculerTourneeUnVehiculeTypeMTest() throws NombreVehiculeInvalideException {
        Tournee tournee = new Tournee(graphe, graphe.getNode("S1"), 1);

        tournee.calculerTourneeUnVehicule("M");

        assertTrue(tournee.getLabelCentresLivres(0).contains("S1"));
        assertTrue(tournee.getLabelCentresLivres(0).contains("S4"));
    }

    @Test
    public void calculerTourneeUnVehiculeTypeInvalideTest() throws NombreVehiculeInvalideException {
        Tournee tournee = new Tournee(graphe, graphe.getNode("S1"), 1);

        try {
            tournee.calculerTourneeUnVehicule("X");
            fail("Une exception aurait dû être levée pour un type de centre invalide.");
        } catch (IllegalArgumentException e) {
            // Exception attendue.
        }
    }

    @Test
    public void calculerTourneeDeuxVehiculesTest() throws NombreVehiculeInvalideException {
        Tournee tournee = new Tournee(graphe, graphe.getNode("S1"), 2);

        tournee.calculerTourneeDeuxVehicules();

        assertTrue(tournee.getDureeTotale(0) >= 0);
        assertTrue(tournee.getDureeTotale(1) >= 0);
        assertTrue(tournee.getLabelCentresLivres(0).contains("S1"));
        assertTrue(tournee.getLabelCentresLivres(1).contains("S1"));
    }

    @Test
    public void getLabelCentresLivresSansCalculTest() throws NombreVehiculeInvalideException {
        Tournee tournee = new Tournee(graphe, graphe.getNode("S1"), 1);

        assertTrue(tournee.getLabelCentresLivres(0).contains("aucun trajet calculé"));
    }

    @Test
    public void colorierTourneeUnVehiculeTest() throws NombreVehiculeInvalideException {
        Tournee tournee = new Tournee(graphe, graphe.getNode("S1"), 1);

        tournee.calculerTourneeUnVehicule(null);
        tournee.colorierTourneeUnVehicule(0);

        boolean auMoinsUneAreteColoriee = false;

        for (Edge e : graphe.getEachEdge()) {
            if (e.hasAttribute("ui.style")) {
                auMoinsUneAreteColoriee = true;
            }
        }
        assertTrue(auMoinsUneAreteColoriee);
    }
}