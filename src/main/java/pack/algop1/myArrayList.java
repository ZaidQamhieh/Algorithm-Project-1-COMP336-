package pack.algop1;

import java.util.Iterator;

public class myArrayList<T extends Comparable<T>> implements Iterable<T> {
    private T[] list;
    private int capacity = 5;
    private int index;

    public myArrayList() {
        list = (T[]) new Comparable[capacity];
        index = 0;
    }

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

    public void add(T t) {
        resize();
        list[index++] = t;
    }

    public void addAll(myArrayList<T> other) {
        for (int i = 0; i < other.size(); i++) {
            add(other.get(i));
        }
    }

    public myArrayList(myArrayList<T> oList, int capacity) {
        this.capacity = capacity;
        this.list = (T[]) new Comparable[capacity];
        this.index = 0;
        for (int i = 0; i < oList.size(); i++) {
            add(oList.get(i));
        }
    }

    public void clear() {
        for (int i = 0; i < index; i++) list[i] = null;
        index = 0;
    }

    public void remove(T obj) {
        int index = search(obj);
        if (index != -1) removeByIndex(index);
    }

    private void removeByIndex(int indexToRemove) {
        if (indexToRemove < 0 || indexToRemove >= index) return;
        for (int i = indexToRemove; i < index - 1; i++) list[i] = list[i + 1];
        list[--index] = null;
    }

    public int search(T obj) {
        for (int i = 0; i < index; i++)
            if (list[i].equals(obj)) return i;
        return -1;
    }

    public T get(int index) {
        if (index < 0 || index >= this.index)
            throw new IndexOutOfBoundsException();
        return list[index];
    }

    public boolean contains(T obj) {
        return search(obj) != -1;
    }

    public boolean isEmpty() {
        return index == 0;
    }

    public int size() {
        return index;
    }


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
