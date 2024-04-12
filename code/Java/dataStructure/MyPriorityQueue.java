class item {
    public int value;
    public int priority;
}

public class MyPriorityQueue {
    static int N = 100010;
    static item[] pr = new item[N];
    static int size = -1;

    static void enqueue(int value, int priority) {
        size++;
        pr[size] = new item();
        pr[size].value = value;
        pr[size].priority = priority;
        .
    }
}
