package org.demo;

import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;


public class EventBus implements Bus {

    /**
     *  该注册表维护了 topic 和 subscriber 之间的关系
     */
    class Registry {
        private final ConcurrentHashMap<String, ConcurrentLinkedQueue<Subscriber>>
                subscriberContainer = new ConcurrentHashMap<>();

        public void bind (Object subscriber) {

        }

        public void unbind (Object subscriber) {

        }

        private List<Method> getSubscribeMethods (Object subscriber) {
            final List<Method> methods = new ArrayList<>();
            Class<?> temp = subscriber.getClass();
            // 不断获取当前类和父类所有@Subscriber方法
            while (temp != null) {
                Method[] declaredMethods = temp.getDeclaredMethods();
                // 只有 public 方法 && 有一个入参 && 最重要的是被 @Subscribe 标记的方法才符合回调方法
                Arrays.stream(declaredMethods)
                        .filter(m -> m.isAnnotationPresent(Subscribe.class)
                                && m.getParameterCount() == 1
                                && m.getModifiers() == Member.PUBLIC)
                        .forEach(methods::add);
                temp = temp.getSuperclass();
            }
            return methods;
        }

        private void tierSubscriber (Object subscriber, Method method) {
            final Subscribe subscribe = method.getDeclaredAnnotation(Subscribe.class);
            String topic = subscribe.topic();
            subscriberContainer.computeIfAbsent(topic, key -> new ConcurrentLinkedQueue<>());
            subscriberContainer.get(topic).add(new Subscriber(subscriber, method));
        }
    }


    // 用于维护 Subscriber 的注册表
    private final Registry registry = new Registry();

    private String busName;

    private final static String DEFAULT_BUS_NAME = "default";

    private final static String DEFAULT_TOPIC = "default-topic";

//    private final Dispatcher dispatcher;

    @Override
    public void register(Object subscriber) {

    }

    @Override
    public void unregister(Object subscriber) {

    }

    @Override
    public void post(Object event) {

    }

    @Override
    public void post(Object event, String topic) {

    }

    @Override
    public void close() {

    }

    @Override
    public String getBusName() {
        return null;
    }
}
