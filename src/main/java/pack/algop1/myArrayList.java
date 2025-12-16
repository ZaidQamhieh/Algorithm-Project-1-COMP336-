package pack.algop1;

import java.util.Iterator;

public class myArrayList<T extends Comparable<T>> implements Iterable<T> {
    private T[] list;
    private int capacity;
    private int index;

    public myArrayList(int capacity) {
        list = (T[]) new Comparable[capacity];
        this.capacity = capacity;
        index = 0;
    }
    // Resizing Array When it Becomes Full
    private void resize() {
        if (index == capacity) {
            capacity *= 2;
            T[] newList = (T[]) new Comparable[capacity];
            for (int i = 0; i < index; i++) {
                newList[i] = list[i];
            }
            list = newList;
        }
    }

    //Adds to The List
    public void add(T t) {
        resize();
        list[index++] = t;
    }

    // Helper Method To Remove an Element
    public void remove(T obj) {
        int index = search(obj);
        if (index != -1) removeByIndex(index);
    }

    // Removes an Element from The List
    private void removeByIndex(int indexToRemove) {
        if (indexToRemove < 0 || indexToRemove >= index) return;
        for (int i = indexToRemove; i < index - 1; i++) list[i] = list[i + 1];
        list[--index] = null;
    }
    // Searches For a Certain Element
    public int search(T obj) {
        for (int i = 0; i < index; i++)
            if (list[i].equals(obj)) return i;
        return -1;
    }
    // Get an Element By Index
    public T get(int index) {
        if (index < 0 || index >= this.index)
            throw new IndexOutOfBoundsException();
        return list[index];
    }

    // In Case The User Wanted to Forcibly Change The List Length
    public void updateCapacity(int newCap) {
        if (newCap <= list.length) return;

        T[] temp = (T[]) new Comparable[newCap];
        for (int i = 0; i < size(); i++)
            temp[i] = list[i];
        list = temp;
    }
    // Return if The List Has The Element
    public boolean contains(T obj) {
        return search(obj) != -1;
    }
    // Return if The List is Empty
    public boolean isEmpty() {
        return index == 0;
    }
    // Return The Size of The List
    public int size() {
        return index;
    }

    // To Be Able To Iterate Throughout The List via foreach
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            int current = 0;

            @Override
            public boolean hasNext() {
                return current < index;
            }

            @Override
            public T next() {
                return list[current++];
            }
        };
    }
}
