public class LambdaIntro {

    public static void main(String[] args) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("LambdaIntro");
            }
        });
        thread.start();

        new Thread(() -> System.out.println("LambdaIntro More Concise")).start();
        new Thread(() -> {System.out.println("LambdaIntro More Concise2");}).start();
    }
}