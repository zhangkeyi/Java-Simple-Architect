package org.example;

public abstract class Latch {
    // 限制事件完成数
    protected int limit;

    public Latch (int limit) {
        this.limit = limit;
    }

    // 事件完成时调用该方法，计数器减1
    protected abstract void countDown ();
    // 等待所有事件完成
    protected abstract void await () throws InterruptedException;
    // 查询剩余事件数
    protected abstract int getUnArrived ();


}
