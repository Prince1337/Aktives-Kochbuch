package prince.aktiveskochbuch.domain.models;

public enum Type {
    VEGETARISCH, GLUTENFREI, STANDARD;

    public static Type fromString(String type) {
        return switch (type) {
            case "VEGETARISCH" -> VEGETARISCH;
            case "GLUTENFREI" -> GLUTENFREI;
            case "STANDARD" -> STANDARD;
            default -> throw new IllegalArgumentException("Ungültiger Rezept Typ: " + type);
        };
    }
}
