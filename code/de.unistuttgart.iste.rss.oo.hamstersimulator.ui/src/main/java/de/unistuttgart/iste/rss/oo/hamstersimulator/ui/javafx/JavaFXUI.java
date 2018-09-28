package de.unistuttgart.iste.rss.oo.hamstersimulator.ui.javafx;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.GameCommandStack;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.GameLog;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.InputInterface;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.ReadOnlyTerritory;
import javafx.application.Application;
import javafx.stage.Stage;

public class JavaFXUI extends Application {

    private static final CountDownLatch initLatch = new CountDownLatch(1);

    public static void start() {
        new Thread(()->Application.launch(JavaFXUI.class)).start();
        waitForJavaFXStart();
    }

    private static void waitForJavaFXStart() {
        try {
            initLatch.await();
        } catch (final InterruptedException e) { }
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        initLatch.countDown();
    }

    public static void openSceneFor(final ReadOnlyTerritory territory, final GameCommandStack commandStack, final GameLog gameLog) {
        JavaFXUtil.blockingExecuteOnFXThread(() -> {
            Stage stage;
            try {
                stage = new HamsterGameStage(territory, commandStack, gameLog);
                stage.show();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        });
    }
    
    public static InputInterface getJavaFXInputInterface() {
        return new JavaFXInputInterface();
    }

}
