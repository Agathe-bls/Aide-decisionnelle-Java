package saejava.classes;

public class Centre  {
    private String id;
    private TypeCentre type;

    public Centre(String id, TypeCentre type) {
        this.id = id;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public TypeCentre getType() {
        return type;
    }

    @Override
    public String toString() {
        return id + " (" + type.getNomComplet() + ")";
    }
}