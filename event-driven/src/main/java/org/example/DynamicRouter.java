package org.example;

public interface DynamicRouter <E extends Message>{
    /**
     *  针对每种类型注册相关的 Channel
     * @param messageType
     * @param channel
     */
    void registerChannel (Class<? extends Message> messageType, Channel<? extends E> channel);

    /**
     *  为相应的 message 分配 Message
     * @param message
     */
    void dispatch (E message);
}
