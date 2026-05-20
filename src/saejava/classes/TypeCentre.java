package saejava.classes;

public class TypeCentre {
    private char type;

    public TypeCentre(char type) {
        if (type != 'M' && type != 'O' && type != 'N') {
            throw new IllegalArgumentException("Type invalide : " + type);
        }
        this.type = type;
    }

    public char getType() {
        return type;
    }

    public String getNomComplet() {
        if (type == 'M') {
            return "Maternite";
        } 
        else if (type == 'O') {
            return "Bloc operatoire";
        } 
        else {
            return "Nutrition";
        }
    }

    @Override
    public String toString() {
        return "" + type;
    }
}
