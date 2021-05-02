package Libs;

import java.util.EventListener;

public interface ObservableArrayListListener<E> extends EventListener {
    public void elementAdded(ObservableArrayListEvent evt);
    public void elementModified(ObservableArrayListEvent evt);
    public void elementRemoved(ObservableArrayListEvent evt);
    public void listCleared(ObservableArrayListEvent evt);
}
