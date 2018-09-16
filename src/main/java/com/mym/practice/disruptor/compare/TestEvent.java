package com.mym.practice.disruptor.compare;

public class TestEvent {

    private long data;

    public void setData(long data){
        this.data = data;
    }

    @Override
    public String toString() {
        return "TestEvent{" +
                "data=" + data +
                '}';
    }
}
