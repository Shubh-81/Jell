package commands;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public interface BaseCommand {
    void execute(InputStream in, OutputStream out) throws Exception;

    public String getName();
}
