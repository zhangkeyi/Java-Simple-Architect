package org.demo;

import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;


public class EventBus implements Bus {



    /**
     *  该注册表维护了 topic 和 subscriber 之间的关系
     */
    class Registry {
        private final ConcurrentHashMap<String, ConcurrentLinkedQueue<Subscriber>>
                subscriberContainer = new ConcurrentHashMap<>();

        public void bind (Object subscriber) {
            List<Method> subscribeMethods = getSubscribeMethods(subscriber);
            subscribeMethods.forEach(m -> tierSubscriber(subscriber, m));
        }

        public void unbind (Object subscriber) {
            subscriberContainer.forEach((key, queue) -> {
                queue.forEach(s -> {
                    if (s.getSubscribeObject() == subscriber) {
                        s.setDisable(true);
                    }
                });
            });
        }

        public ConcurrentLinkedQueue<Subscriber> scanSubscriber (final String topic) {
            return subscriberContainer.get(topic);
        }

        private List<Method> getSubscribeMethods (Object subscriber) {
            final List<Method> methods = new ArrayList<>();
            Class<?> temp = subscriber.getClass();
            // 不断获取当前类和父类所有@Subscribe方法
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

    private final Dispatcher dispatcher;

    public EventBus () {
        this (DEFAULT_BUS_NAME, null, Dispatcher.SEQ_EXECUTOR_SERVICE);
    }

    public EventBus(String busName) {
        this (busName, null, Dispatcher.SEQ_EXECUTOR_SERVICE);
    }

    public EventBus (EventExceptionHandler exceptionHandler) {
        this (DEFAULT_BUS_NAME, exceptionHandler, Dispatcher.SEQ_EXECUTOR_SERVICE);
    }

    public EventBus(String busName, EventExceptionHandler exceptionHandler, Executor seqExecutorService) {
        this.busName = busName;
        this.dispatcher = Dispatcher.newDispatcher(exceptionHandler, seqExecutorService);
    }

    @Override
    public void register(Object subscriber) {
        this.registry.bind(subscriber);
    }

    @Override
    public void unregister(Object subscriber) {
        this.registry.unbind(subscriber);
    }

    @Override
    public void post(Object event) {
        this.post(event, DEFAULT_TOPIC);
    }

    @Override
    public void post(Object event, String topic) {
        this.dispatcher.dispath(this, registry, event,topic);
    }

    @Override
    public void close() {
        this.dispatcher.close();
    }

    @Override
    public String getBusName() {
        return this.busName;
    }
}
