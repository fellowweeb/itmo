package queue;

public interface Queue {
    //post: R == count of contained
    int size();

    //post: (R == true && size == 0) || (size > 0 && R == false)
    boolean isEmpty();

    //post: (R[0] = first && ... && R[R.length - 1] = last) || (R.length = 0 && size = 0)
    Object[] toArray();

    //pre: size > 0
    //post: R == first
    Object element();

    //pre: obj != null && last = obj
    //post: size' = size + 1
    void enqueue(Object obj);

    //pre: size > 0
    //post: size-- && R == first
    Object dequeue();

    //post: size == 0
    void clear();
}
