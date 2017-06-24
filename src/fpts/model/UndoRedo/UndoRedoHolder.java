package fpts.model.UndoRedo;

import java.util.Stack;

/**
 * Created by alexandergarrity on 3/30/16.
 *
 * This class holds the two stack used in the undo and redo command system.
 * The undo stack holds commands that have been performed and can be undone.
 * The redo stack holds commands that have been undone that can be redone.
 */
public class UndoRedoHolder {
    // Holds commands that can be undone
    public Stack<UndoRedo> undoStack = new Stack<>();

    // Holds commands that can be redone
    public Stack<UndoRedo> redoStack = new Stack<>();

}
