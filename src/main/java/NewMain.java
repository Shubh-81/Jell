import com.google.inject.Guice;
import com.google.inject.Injector;

import static utils.StartupShutdownHandlers.onShutDown;
import static utils.StartupShutdownHandlers.onStartup;

public class NewMain {

    public static void main(String[] args) throws Exception {
        onStartup();

        // Creates injector based on bindings in ShellModule
        Injector injector = Guice.createInjector(new ShellModule());
        BaseInputHandler inputHandler = injector.getInstance(BaseInputHandler.class);

        inputHandler.work();

        onShutDown();
    }
}
