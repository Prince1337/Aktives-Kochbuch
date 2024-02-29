package prince.aktiveskochbuch.application.utils;

import prince.aktiveskochbuch.domain.models.Rezept;

import java.util.List;

public class EmailUtils {

    private EmailUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String getConfirmationMessage(String name, String host, String token) {
        return "Hello " + name + ",\n\nYour new account has been created. Please click the link below to verify your account. \n\n" +
                getVerificationUrl(host, token) + "\n\nThe support Team";
    }

    public static StringBuilder buildEmailText(String name, List<Rezept> vorschlaege) {
        StringBuilder emailText = new StringBuilder();
        emailText.append("Hallo ").append(name).append(",\n\n");
        emailText.append("Hier sind Ihre automatischen Vorschläge für heute:\n");
        // Fügen Sie jeden Vorschlag aus der Liste ein
        for (Rezept vorschlag : vorschlaege) {
            emailText.append(vorschlag.toString()).append("\n");
        }
        // Fügen Sie zusätzliche Informationen oder Grußformel hinzu, falls erforderlich
        emailText.append("\nViele Grüße,\nIhr Kochbuch-Team");
        return emailText;
    }

    public static String getVerificationUrl(String host, String token) {
        return host + "/api/users?token=" + token;
    }
}
