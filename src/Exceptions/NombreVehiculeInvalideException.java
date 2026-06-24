package Exceptions;

public class NombreVehiculeInvalideException extends Exception {
    public NombreVehiculeInvalideException() {
        super("Le nombre de vehicule est invalide. Il doit etre soit 1 ou 2.");
    }
}
