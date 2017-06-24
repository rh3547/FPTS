package fpts.toolkit;

/**
 * Save Notifier
 *
 * An implementation of Observer that watches different objects so that when they
 * are modified the changes will be persisted
 *
 * Implementation Observer design pattern as the Observer
 */
public interface SaveNotifier {
    void update();
}
