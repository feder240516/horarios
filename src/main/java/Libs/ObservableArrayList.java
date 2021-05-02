package Libs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Observer;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ObservableArrayList<E> implements Iterable<E>, ChangeListener{

    protected ArrayList<E> hidden_list;
    protected HashSet<ObservableArrayListListener<E>> list_listeners;

    public ObservableArrayList() {
        hidden_list = new ArrayList<>();
        list_listeners = new HashSet<>();
    }

    private void fireAddedElement(Object[] elements, int[] indices) {
        ObservableArrayListEvent evt = new ObservableArrayListEvent(
                this, ObservableArrayListEvent.Type.ADDED, elements, indices);
        for(ObservableArrayListListener<E> listener: list_listeners) {
            listener.elementAdded(evt);
        }
    }

    /**
     * Este método esta supuesto para ser usado solo en aquellos casos donde un elemento es
     * modificado, no en aquellos cuando es reemplazado.
     * @param elements
     * @param indices
     */
    private void fireModifiedElement(Object[] elements, int[] indices) {
        ObservableArrayListEvent evt = new ObservableArrayListEvent(
                this, ObservableArrayListEvent.Type.MODIFIED, elements, indices);
        for(ObservableArrayListListener<E> listener: list_listeners) {
            listener.elementModified(evt);
        }
    }

    private void fireRemovedElement(Object[] elements, int[] indices) {
        ObservableArrayListEvent evt = new ObservableArrayListEvent(
                this, ObservableArrayListEvent.Type.REMOVED, elements, indices);
        for(ObservableArrayListListener<E> listener: list_listeners) {
            listener.elementRemoved(evt);
        }
    }

    private void fireClearedList() {
        ObservableArrayListEvent evt = new ObservableArrayListEvent(
                this, ObservableArrayListEvent.Type.CLEARED, null, null);
        for(ObservableArrayListListener<E> listener: list_listeners) {
            listener.listCleared(evt);
        }
    }

    public void addObservableArrayListListener(ObservableArrayListListener<E> lis) {
        list_listeners.add(lis);
    }

    public void removeObservableArrayListListener(ObservableArrayListListener<E> lis) {
        list_listeners.remove(lis);
    }

    public int size() {
        return hidden_list.size();
    }

    public boolean isEmpty() {
        return hidden_list.isEmpty();
    }

    public boolean contains(Object o) {
        return hidden_list.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return hidden_list.iterator();
    }

    public Object[] toArray() {
        return hidden_list.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return hidden_list.toArray(a);
    }

    public boolean add(E e) {
        boolean added = hidden_list.add(e);
        if (added) {
            fireAddedElement(new Object[]{e},new int[] {hidden_list.size()-1});
        }
        return added;
    }

    public boolean remove(Object o) {
        boolean removed = hidden_list.remove(o);
        if (removed) fireRemovedElement(new Object[] {o}, new int[] {hidden_list.size()});
        return removed;
    }

    public void clear() {
        hidden_list.clear();
        fireClearedList();
    }

    public E get(int index) {
        return hidden_list.get(index);
    }

    public void add(int index, E element) {
        hidden_list.add(index,element);
        fireAddedElement(new Object[]{element},new int[] {index});
    }

    public E remove(int index) {
        E element = hidden_list.remove(index);
        fireRemovedElement(new Object[] {element}, new int[] {index});
        return element;
    }

    public int indexOf(Object o) {
        return hidden_list.indexOf(o);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        fireModifiedElement(new Object[] {e.getSource()}, new int[] {indexOf(e.getSource())});
    }

}
