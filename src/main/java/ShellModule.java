import com.google.inject.AbstractModule;
import history.BaseHistoryHandler;
import history.HistoryHandler;
import keyParser.BaseKeyParser;
import keyParser.KeyParser;
import utils.ExecutableProvider;
import utils.SystemExecutableProvider;

public class ShellModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(BaseCommandHandler.class).to(NewCommandHandler.class);
        bind(BaseAutoCompleteHandler.class).to(AutoCompleteHandler.class);
        bind(BaseInputHandler.class).to(InputHandler.class);
        bind(BaseHistoryHandler.class).to(HistoryHandler.class);
        bind(BaseKeyParser.class).to(KeyParser.class);
        bind(ExecutableProvider.class).to(SystemExecutableProvider.class);
    }
}
