package com.mym.practice.disruptor.quickStart;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PersonDisruptorStarter {

    static EventFactory<PersonEvent> eventFactory;
    static ExecutorService executor;
    static Disruptor disruptor;

    static{
        eventFactory = new PersonEventFactory();
        executor = Executors.newSingleThreadExecutor();
        int ringBufferSize = 1024 * 1024; // RingBuffer 大小，必须是 2 的 N 次方；
        disruptor = new Disruptor<PersonEvent>(eventFactory,
                ringBufferSize, executor, ProducerType.SINGLE,
                new YieldingWaitStrategy());
    }

    public static void main(String[] args) {

        disruptorStart();

        int i = 0;
        while(i++ < 100){
            disruptorProducer();
        }

        close();
        System.out.println("disruptor close . . . ");
    }

    public static void disruptorStart(){
        EventHandler<PersonEvent> eventHandler = new PersonEventHandler();
        disruptor.handleEventsWith(eventHandler);

        disruptor.start();
    }

    public static void disruptorProducer(){
        /*获得当前生产序号*/
        RingBuffer<PersonEvent> ringBuffer = disruptor.getRingBuffer();
        long sequence = ringBuffer.next();

        try {
            /*生产处理事件*/
            PersonEvent event = ringBuffer.get(sequence);//获取该序号对应的事件对象；
            int random = (int)(Math.random() * 10);
            event.modifyAge(random);
        } finally{
            /*发布事件，在finally中确保得到调用，即使出了异常*/
            ringBuffer.publish(sequence);
        }
    }

    public static void close(){
        disruptor.shutdown();//关闭 disruptor，方法会堵塞，直至所有的事件都得到处理；
        executor.shutdown();//关闭 disruptor 使用的线程池；如果需要的话，必须手动关闭， disruptor 在 shutdown 时不会自动关闭；
    }


}
