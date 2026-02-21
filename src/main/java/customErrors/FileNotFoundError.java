package customErrors;

public class FileNotFoundError extends Exception {
    private final String filePath;

    public FileNotFoundError(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String getMessage() {
        return filePath + ": No such file or directory";
    }
}
