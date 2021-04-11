package queue;

public class ArrayQueue extends AbstractQueue {
    private Object[] arr = new Object[8];
    private int start = 0;

    private int end() {
        return (start + size) % arr.length;
    }

    private void allocate(int newSize) {
        if (newSize < size) {
            throw new IllegalArgumentException("new size < count of contained");
        }
        Object[] newArr = toArray(newSize);
        arr = newArr;
        start = 0;
    }

    private Object[] toArray(int arrSize) {
        Object[] newArr = new Object[arrSize];
        int rightSize = Math.min(size, arr.length - start);
        System.arraycopy(arr, start, newArr, 0, rightSize);
        System.arraycopy(arr, (start + rightSize) % arr.length,
                newArr, rightSize, size - rightSize);
        return newArr;
    }

    public Object[] toArray() {
        return toArray(size);
    }

    @Override
    public Object peek() {
        return arr[start];
    }

    @Override
    public void push(Object obj) {
        if (size >= arr.length) {
            allocate(arr.length * 2);
        }
        arr[end()] = obj;
    }

    @Override
    public Object pop() {
        Object ret = arr[start];
        arr[start] = null;
        start = (start + 1) % arr.length;
        return ret;
    }

    @Override
    public void clearImp() {
        arr = new Object[arr.length];
        start = 0;
    }
}
