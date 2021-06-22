package org.example;

/**
 *  接收来自 Event Loop 分配的消息，每个 Channel 负责处理一种类型的消息
 */
public interface Channel<E extends Message> {

    /**
     *  用于负责 Message 的调度
     * @param message
     */
    void dispatch (E message);

}
