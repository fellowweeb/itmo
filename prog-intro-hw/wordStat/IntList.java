package wordStat;

import java.util.Arrays;
import java.util.NoSuchElementException;

public class IntList {
    private final int MIN_ARRAY_SIZE = 8;
    private int[] list = new int[MIN_ARRAY_SIZE];
    private int size = 0;


    public IntList() {
    }

    public IntList(int size) {
        resize(size);
    }

    public IntList(int size, int value) {
        if (size > list.length) {
            allocate(size);
        }
        this.size = size;
        Arrays.fill(list, 0, size, value);
    }

    public void allocate(int newSize) {
        list = Arrays.copyOf(list, newSize);
    }

    public void allocate() {
        list = Arrays.copyOf(list, list.length * 2);
    }

    public int size() {
        return size;
    }

    public void resize(int newSize) {
        if (newSize > list.length) {
            allocate(newSize);
        }
        Arrays.fill(list, size, newSize, 0);
        size = newSize;
    }

    public void fill(int begin, int end, int value) {
        if (end > list.length) {
            allocate(end);
        }
        if (begin < 0) {
            throw new IndexOutOfBoundsException();
        }
        Arrays.fill(list, begin, end, value);
    }

    public void pushBack(int value) {
        if (size == list.length) {
            allocate();
        }
        list[size++] = value;
    }

    public int back() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return list[size - 1];
    }

    public int get(int index) {
        if (index >= size) {
            throw new IndexOutOfBoundsException();
        }
        return list[index];
    }

    public int front() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return list[0];
    }

    public void trim() {
        list = Arrays.copyOf(list, size);
    }
}
