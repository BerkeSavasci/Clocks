package uhr;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

/**
 * Stellt eine Digitale Uhr dar, die man anhalten und weiterlaufen lassen kann
 */
public class DigitalUhr implements PropertyChangeListener {
    @FXML
    private Label anzeige;
    @FXML
    private Button btnEin;
    @FXML
    private Button btnAus;
    private Stage stage;
    private Zeit zeit;
    private boolean uhrAn;

    /**
     * erstellt das Fenster für die digitale Uhr und bringt es auf den
     * Bildschirm; zu Beginn läuft die Uhr im 1-Sekunden-Takt
     */
    public DigitalUhr(Zeit zeit) {
        this.zeit = zeit;

        stage = new Stage();
        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("digitaluhr.fxml"));
        loader.setController(this);
        Parent lc = null;
        try {
            lc = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(lc, 400, 100);
        stage.setTitle("Digitaluhr");
        stage.setScene(scene);
        stage.show();
        initialize();
        einschalten();
        zeit.addPropertyChangeListener(this);
    }

    @FXML
    void initialize() {
        btnEin.setOnAction(e -> einschalten());
        btnAus.setOnAction(e -> ausschalten());
    }

    /**
     * wird beim Klick auf den Ein-Button aufgerufen
     */
    private void einschalten() {
        uhrAn = true;
        btnEin.setDisable(true);
        btnAus.setDisable(false);
    }

    /**
     * wird beim Klick auf den Aus-Button aufgerufen
     */
    private void ausschalten() {
        uhrAn = false;
        btnEin.setDisable(false);
        btnAus.setDisable(true);
    }


    /**
     * Holen der aktuellen Uhrzeit und Anzeige, wenn die Uhr angestellt ist
     */
    public void tick() {
        if (uhrAn) {
            Platform.runLater(() -> {
                anzeige.setText(String.format("%02d:%02d:%02d",
                        zeit.getStunde(), zeit.getMinute(), zeit.getSekunde()));
            });
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (uhrAn && evt.getPropertyName().equals("sekunde")) {
            this.tick();
        }
    }

    /**
     * schließt das Fenster
     */
    public void beenden() {
        stage.close();
    }


}
