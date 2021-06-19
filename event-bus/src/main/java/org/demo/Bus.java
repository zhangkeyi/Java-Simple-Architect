package org.demo;

public interface Bus {

    //  将某个对象注册到bus 上从此之后该类就成为Subscriber
    void register (Object subscriber);
    // 取消注册
    void unregister (Object subscriber);
    // 提交 event 到默认的topic
    void post (Object event);
    // 提交 event 到指定的topic
    void post (Object event, String topic);
    // 关闭该bus
    void close ();
    // 返回bus 的名称标识
    String getBusName ();


}
