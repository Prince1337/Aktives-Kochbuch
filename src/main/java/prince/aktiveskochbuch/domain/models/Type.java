package prince.aktiveskochbuch.domain.models;

import java.util.Locale;

public enum Type {
    VEGETARISCH, GLUTENFREI, STANDARD;

    public static Type fromString(String type) {
        return switch (type.toUpperCase(new Locale("de", "DE"))) {
            case "VEGETARISCH" -> VEGETARISCH;
            case "GLUTENFREI" -> GLUTENFREI;
            case "STANDARD" -> STANDARD;
            default -> throw new IllegalArgumentException("Ungültiger Rezept Typ: " + type);
        };
    }
}
