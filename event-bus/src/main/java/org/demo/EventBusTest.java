package org.demo;

public class EventBusTest {
    static class SimpleSubscriber1 {
        @Subscribe
        public void method1 (String message) {
            System.out.println("=======SimpleSubscriber1==method1====" + message);
        }

        @Subscribe (topic = "test")
        public void method2 (String message) {
            System.out.println("=======SimpleSubscriber1==method2====" + message);
        }
    }

    static class SimpleSubscriber2 {
        @Subscribe
        public void method1 (String message) {
            System.out.println("=======SimpleSubscriber2==method1====" + message);
        }

        @Subscribe (topic = "test")
        public void method2 (String message) {
            System.out.println("=======SimpleSubscriber2==method2====" + message);
        }
    }


    public static void main (String[] args) {

        Bus bus = new EventBus("TestBus");
        bus.register(new SimpleSubscriber1());
        bus.register(new SimpleSubscriber2());
        bus.post("hello");
        System.out.println("---------------------");
        bus.post("hello", "test");
    }
}
