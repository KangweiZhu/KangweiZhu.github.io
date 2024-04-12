import java.util.Scanner;

public class Main {

    static int N = 100010;
    static int[] e = new int[N];
    static int[] ne = new int[N];
    static int idx = 0;
    static int head = -1;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int m = sc.nextInt();
        while (m-- > 0) {
            String cmd = sc.next();
            if (cmd.equals("D")) {
                int k = sc.nextInt();
                if (k == 0) {
                    head = ne[head];
                } else {
                    delete(k - 1);
                }
            } else if (cmd.equals("I")) {
                int k = sc.nextInt();
                int n = sc.nextInt();
                insert(k - 1, n);
            } else if (cmd.equals("H")) {
                int n = sc.nextInt();
                addHead(n);
            }
        }
        for (int i = head; i != -1; i = ne[i]) {
            System.out.print(e[i] + " ");
        }
    }

    private static void addHead(int n) {
        e[idx] = n;
        ne[idx] = head;
        head = idx;
        idx++;
    }

    private static void insert(int k, int n) {
        e[idx] = n;
        ne[idx] = ne[k];
        ne[k] = idx;
        idx++;
    }

    private static void delete(int k) {
        ne[k] = ne[ne[k]];
    }
}
