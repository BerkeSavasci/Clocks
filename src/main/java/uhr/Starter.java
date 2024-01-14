package uhr;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Startet eine Uhrenoberfläche
 *
 * @author Doro
 */
public class Starter extends Application implements PropertyChangeListener {
    @FXML
    private Button btnAnalog;
    @FXML
    private Button btnDigital;
    @FXML
    private Button btnEntfernen;
    private Stage primaryStage;

    private List<DigitalUhr> dUhren = new LinkedList<>();
    private List<KreisUhr> kUhren = new LinkedList<>();
    private ScheduledExecutorService service;
    private Future<?> laufen;
    private Zeit zeit;


    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("starter.fxml"));
        loader.setController(this);
        Parent lc = loader.load();
        Scene scene = new Scene(lc, 300, 100);
        primaryStage.setTitle("Viele Uhren");
        primaryStage.setScene(scene);
        primaryStage.show();
        initialize();

        service = Executors.newSingleThreadScheduledExecutor();
        zeit = new Zeit();
        laufen = service.scheduleAtFixedRate(zeit::laufen, 0, 1, TimeUnit.SECONDS);
        zeit.addPropertyChangeListener(this);

    }

    @FXML
    private void initialize() {
        btnAnalog.setOnAction(e -> neueKreisUhr());
        btnDigital.setOnAction(e -> neueDigitalUhr());
        btnEntfernen.setOnAction(e -> alleEntfernen());
        primaryStage.setOnCloseRequest(e -> starterSchliessen());

    }

    /**
     * wird beim Klick auf Digital-Button aufgerufen
     */
    private void neueDigitalUhr() {
        dUhren.add(new DigitalUhr(zeit));
    }

    /**
     * wird beim Klick auf Analog-Button aufgerufen
     */
    private void neueKreisUhr() {
        kUhren.add(new KreisUhr(zeit));
    }

    /**
     * Removes all the clocks from the application.
     */
    private void alleEntfernen() {
        for (KreisUhr k : kUhren) k.dispose();
        for (DigitalUhr d : dUhren) d.beenden();
        kUhren.clear();
        dUhren.clear();
    }

    /**
     * Closes the application.
     */
    private void starterSchliessen() {
        laufen.cancel(false);
        service.shutdown();
    }

    /**
     * This method is called when a property changes. It updates the clocks if the changed property is "minute" or "sekunde".
     *
     * @param evt The property change event
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        if (propertyName.equals("sekunde")) {
            for (KreisUhr k : kUhren) SwingUtilities.invokeLater(k::tick);
            for (DigitalUhr d : dUhren) Platform.runLater(d::tick);
        }
    }
}
