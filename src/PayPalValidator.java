public class PayPalValidator {
    // Simple validation for PayPal email
    public static boolean isValidPayPalEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }

        // Basic validation for email format
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        return email.matches(emailRegex);
    }
}
