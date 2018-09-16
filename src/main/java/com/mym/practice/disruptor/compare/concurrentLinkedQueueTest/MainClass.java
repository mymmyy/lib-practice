package com.mym.practice.disruptor.compare.concurrentLinkedQueueTest;

import com.mym.practice.disruptor.compare.TestEvent;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class MainClass {
    static final long testNum = 10000000;

    public static ConcurrentLinkedQueue<TestEvent>  QUEUE = new ConcurrentLinkedQueue<TestEvent>();

    public static void main(String[] args) {

        final ExecutorService producer = Executors.newSingleThreadExecutor();
        final ExecutorService consumer = Executors.newSingleThreadExecutor();
        final long startTime = System.currentTimeMillis();
        producer.execute(new TestProducer());
        consumer.execute(new TestConsumer(startTime));


    }

    static class TestProducer implements Runnable{

        @Override
        public void run() {
            int i = 0;
            while (i++ < testNum){
                TestEvent testEvent = new TestEvent();
                testEvent.setData((long)(Math.random()*100));
                QUEUE.add(testEvent);
            }

        }
    }

    static class TestConsumer implements Runnable{

        AtomicInteger count = new AtomicInteger(0);

        long startTime;

        public TestConsumer(long startTime){
            this.startTime = startTime;
        }

        @Override
        public void run() {
            int c = count.incrementAndGet();
            while(c <= testNum){
                TestEvent poll = QUEUE.poll();
                if(poll != null){
                    c = count.incrementAndGet();
                }
                System.out.println("QUEUE task : "+poll+" count is "+c);
            }

            long endTime = System.currentTimeMillis();
            System.out.println("process need time (ms):"+(endTime - startTime));
        }
    }



}
