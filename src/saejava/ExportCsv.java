package saejava;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import saejava.classes.Tournee;

public class ExportCsv {

    public static void exporterTournes(Tournee tournee2V) {
        String nomFichier = "tournees.csv";

        // Utilisation du try-with-resources pour fermer automatiquement le fichier à la fin
        try (FileWriter fw = new FileWriter(nomFichier, false);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            // Écriture de l'en-tête du fichier CSV
            // Utilisation du point-virgule (;) pour une ouverture directe et propre dans Excel (version française)
            out.println("Vehicule;Duree (min);Depart;Etapes...");

            // Traitement pour chaque véhicule (0 et 1)
            for (int i = 0; i < 2; i++) {
                StringBuilder ligneCsv = new StringBuilder();
                
                // 1. Ajouter l'identifiant du véhicule
                ligneCsv.append("V").append(i + 1).append(";");
                
                // 2. Ajouter la durée totale de la tournée
                ligneCsv.append(tournee2V.getDureeTotale(i)).append(";");

                // 3. Récupérer la chaîne du trajet (ex: "S1 > Centre A > Centre B > S1")
                String trajetRaw = tournee2V.getLabelCentresLivres(i);

                if (trajetRaw != null && !trajetRaw.isEmpty()) {
                    // Remplacement de chaque " > " par un ";" pour changer de case/colonne automatiquement
                    String trajetFormate = trajetRaw.replace(" > ", ";").replace(">", ";");
                    ligneCsv.append(trajetFormate);
                }

                // Écriture de la ligne complétée dans le fichier CSV
                out.println(ligneCsv.toString());
            }

            System.out.println("Le fichier CSV a été généré avec succès sous le nom : " + nomFichier);

        } catch (IOException e) {
            System.err.println("Erreur lors de la création du fichier CSV : " + e.getMessage());
            e.printStackTrace();
        }
    }
}