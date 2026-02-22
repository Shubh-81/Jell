package history;

public interface BaseHistoryHandler {
    public void recordCommand(String command);
    public void resetIndex();
    public String handleUp(String currentCommand);
    public String handleDown(String currentCommand);
}
