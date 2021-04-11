package queue;

public class LinkedQueue extends AbstractQueue {
    private Node first;
    private Node last;

    @Override
    public Object peek() {
        return first.value;
    }

    @Override
    public void push(Object obj) {
        if (size > 0) {
            last = last.prev = new Node(null, obj);
        } else {
            last = first = new Node(null, obj);
        }
    }

    @Override
    public Object pop() {
        Object ret = first.value;
        first = first.prev;
        return ret;
    }

    @Override
    public void clearImp() {
        first = null;
        last = null;
    }

    private static class Node {
        public Node prev;
        public Object value;

        public Node(Node prev, Object value) {
            this.prev = prev;
            this.value = value;
        }
    }
}
