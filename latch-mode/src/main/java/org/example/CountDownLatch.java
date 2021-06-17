package org.example;

public class CountDownLatch extends Latch {


    public CountDownLatch(int limit) {
        super(limit);
    }

    @Override
    protected void countDown() {
        synchronized (this) {
            if (limit <= 0) {
                throw new IllegalStateException("all of task already arrived");
            } else {
                limit --;
                // 唤醒当前对象的排队队列中的线程
                this.notifyAll();
            }
        }
    }

    @Override
    protected void await() throws InterruptedException {
        synchronized (this) {
            while (limit > 0) {
                this.wait();
            }
        }
    }

    @Override
    protected int getUnArrived() {
        return 0;
    }
}
