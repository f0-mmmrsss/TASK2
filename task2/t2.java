package task2;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Scanner;
    public class t2 {
        private static long ans;
        private int i = 0;
        static class Worker implements Runnable {
            private CountDownLatch startLatch;
            private CountDownLatch latch;
            private int num;
            private int x;

            Worker(CountDownLatch startLatch, CountDownLatch latch, int num, int x) {
                this.startLatch = startLatch;
                this.latch = latch;
                this.num = num;
                this.x = x;
            }

            public void run() {
                try {
                    startLatch.await();
                    doWork(this.x);
                    latch.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            private void doWork(int x) {
                for (long begin = this.num*5000000,end = (this.num + 1) * 5000000; begin < end; begin++)
                {
                    if (contain(begin, x)) ans += begin;
                }
            }
        }
        private static boolean contain(long num, int x) {
            return String.valueOf(num).contains(String.valueOf(x));
        }
        public static void main(String[] args) throws InterruptedException {
            CountDownLatch startLatch = new CountDownLatch(1);
            CountDownLatch latch = new CountDownLatch(20);
            ExecutorService executor = Executors.newFixedThreadPool(20);
            Scanner scanner = new Scanner(System.in);
            int x = scanner.nextInt();
            for (int i = 0; i < 20; i++) {
                executor.execute(new Worker(startLatch, latch, i, x));
            }
            startLatch.countDown();
            latch.await();
            System.out.println(ans);
            executor.shutdown();
        }
    }