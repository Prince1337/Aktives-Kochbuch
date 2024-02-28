package prince.aktiveskochbuch.application.utils;

public class EmailUtils {

    private EmailUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String getConfirmationMessage(String name, String host, String token) {
        return "Hello " + name + ",\n\nYour new account has been created. Please click the link below to verify your account. \n\n" +
                getVerificationUrl(host, token) + "\n\nThe support Team";
    }

    public static String getVerificationUrl(String host, String token) {
        return host + "/api/users?token=" + token;
    }
}
