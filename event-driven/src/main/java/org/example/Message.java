package org.example;

/**
 *  Message 是对 Event 的更高抽象
 */
public interface Message {

    Class<? extends Message> getType ();

}
