import com.google.inject.AbstractModule;

public class ShellModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(BaseCommandHandler.class).to(NewCommandHandler.class);
        bind(BaseAutoCompleteHandler.class).to(AutoCompleteHandler.class);
        bind(BaseInputHandler.class).to(InputHandler.class);
    }
}
