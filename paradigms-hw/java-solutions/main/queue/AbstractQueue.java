package queue;

public abstract class AbstractQueue implements Queue {
    protected int size = 0;

    protected abstract Object pop();

    protected abstract Object peek();

    protected abstract void push(Object obj);

    protected abstract void clearImp();

    @Override
    public Object element() {
        if (size <= 0) {
            throw new IllegalStateException("size <= 0");
        }
        return peek();
    }

    @Override
    public void enqueue(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("enqueue null");
        }
        push(obj);
        size++;
    }

    @Override
    public Object dequeue() {
        if (size <= 0) {
            throw new IllegalStateException("size <= 0");
        }
        Object ret = pop();
        size--;
        return ret;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        clearImp();
        size = 0;
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        for (int i = 0; i < size; i++) {
            arr[i] = dequeue();
            enqueue(arr[i]);
        }
        return toArray();
    }
}
