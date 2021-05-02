package Libs;

import java.util.EventObject;

public class ObservableArrayListEvent extends EventObject {

    private static final long serialVersionUID = -2074958494961778723L;

    public enum Type{
        ADDED,
        REMOVED,
        MODIFIED,
        CLEARED,
    }

    protected ObservableArrayListEvent.Type type;
    protected Object[] elements;
    protected int[] indices;

    public ObservableArrayListEvent(Object source, ObservableArrayListEvent.Type type, Object[] elements, int[] indices) {
        super(source);
        if (type == null) throw new NullPointerException("Event type declared null");
        this.type = type;
        this.elements = elements;
        this.indices = indices;
    }

    public ObservableArrayListEvent.Type getType(){
        return type;
    }

    public Object[] getElements() {
        return elements;
    }

    public int[] getIndices() {
        return indices;
    }
}

