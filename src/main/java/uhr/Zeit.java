package uhr;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * eine Uhr mit Sekundenzählung
 */
public class Zeit {

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private int stunde;
    private int minute;
    private int sekunde;
    private ScheduledExecutorService service;
    private Future<?> laufen;

    /**
     * erstellt die Uhr
     */
    public Zeit() {
        service = Executors.newSingleThreadScheduledExecutor();
        laufen = service.scheduleAtFixedRate(this::laufen, 0, 1, TimeUnit.SECONDS);
    }

    /**
     * Adds a PropertyChangeListener to the underlying PropertyChangeSupport object.
     *
     * @param listener the PropertyChangeListener to be added
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    /**
     * Removes a PropertyChangeListener from the underlying PropertyChangeSupport object.
     *
     * @param listener the PropertyChangeListener to be removed
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    /**
     * liefert die aktuelle Stunde
     *
     * @return Stunde
     */
    public int getStunde() {
        return stunde;
    }

    /**
     * liefert die aktuelle Minute
     *
     * @return Minute
     */
    public int getMinute() {
        return minute;
    }

    /**
     * liefert die aktuelle Sekunde
     *
     * @return Sekunde
     */
    public int getSekunde() {
        return sekunde;
    }

    /**
     * Updates the hour, minute, and second properties of the Zeit object.
     */
    public void laufen() {
        LocalTime jetzt = LocalTime.now();
        int oldStunde = stunde;
        int oldMinute = minute;
        int oldSekunde = sekunde;

        stunde = jetzt.getHour();
        pcs.firePropertyChange("stunde", oldStunde, stunde);

        minute = jetzt.getMinute();
        pcs.firePropertyChange("minute", oldMinute, minute);

        sekunde = jetzt.getSecond();
        pcs.firePropertyChange("sekunde", oldSekunde, sekunde);
    }

    /**
     * Closes the application.
     */
    public void starterSchliessen() {
        laufen.cancel(false);
        service.shutdown();
    }
}
