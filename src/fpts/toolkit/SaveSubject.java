package fpts.toolkit;

/**
 * The object being watched by the Save Notifiers in order to persist any changes
 * <p>
 * Implementation Observer design pattern as the Subject
 */
public interface SaveSubject {
    /**
     * @param saveNotifier add an saveNotifier to the object to be notified
     */
    void registerWatcher(SaveNotifier saveNotifier);

    /**
     * @param saveNotifier remove an saveNotifier so that it is no longer notified
     */
    void removeWatcher(SaveNotifier saveNotifier);

    /**
     * Notify observers of changes
     */
    void notifyWatchers();

}
