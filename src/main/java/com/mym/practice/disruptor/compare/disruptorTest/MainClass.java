package com.mym.practice.disruptor.compare.disruptorTest;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.mym.practice.disruptor.compare.TestEvent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class MainClass {

    static final long testNum = 10000000;

    public static void main(String[] args) {
        EventFactory<TestEvent> eventFactory = new TestEventFactory();
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        int ringBufferSize = 1024 * 1024; // RingBuffer 大小，必须是 2 的 N 次方；
        final Disruptor disruptor = new Disruptor<TestEvent>(eventFactory,
                ringBufferSize, executor, ProducerType.SINGLE,
                new YieldingWaitStrategy());

        long startTime = System.currentTimeMillis();
        EventHandler<TestEvent> eventHandler = new TestEventHandler(startTime);
        disruptor.handleEventsWith(eventHandler);
        disruptor.start();


        /*生产数据*/
        new Thread(new Runnable() {
            @Override
            public void run() {
                long i = 0;
                while(i++ < testNum){
            /*获得当前生产序号*/
                    RingBuffer<TestEvent> ringBuffer = disruptor.getRingBuffer();
                    long sequence = ringBuffer.next();

                    try {
            /*生产处理事件*/
                        TestEvent event = ringBuffer.get(sequence);//获取该序号对应的事件对象；
                        long random = (long)(Math.random() * 100);
                        event.setData(random);
                    } finally{
            /*发布事件，在finally中确保得到调用，即使出了异常*/
                        ringBuffer.publish(sequence);
                    }
                }

            }
        }).start();



    }

    /**处理器*/
    static class TestEventHandler implements EventHandler<TestEvent>{

        AtomicInteger count = new AtomicInteger(0);

        long startTime;

        public TestEventHandler(long startTime){
            this.startTime = startTime;
        }

        @Override
        public void onEvent(TestEvent testEvent, long l, boolean b) throws Exception {
            int c = count.incrementAndGet();
            System.out.println("process testEvent and data is:"+testEvent+" -- and process num is :"+ c);
            if(c >= testNum){
                long endTime = System.currentTimeMillis();
                System.out.println("process need time (ms):"+(endTime - startTime));
            }
        }
    }

    /**事件工厂*/
    static class TestEventFactory implements EventFactory<TestEvent>{

        @Override
        public TestEvent newInstance() {
            return new TestEvent();
        }
    }
}
