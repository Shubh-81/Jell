package customErrors;

public class InvalidArguments extends Exception {
    @Override
    public String getMessage() {
        return "invalid arguments";
    }
}
