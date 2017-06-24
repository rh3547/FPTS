package fpts.model.UndoRedo;

/**
 * Created by alexandergarrity on 3/30/16.
 */
public interface UndoRedo {
    /**
     * Execute the command.
     */
    void execute();

    /**
     * Un-execute the command.
     */
    void deexecuteify();
}
