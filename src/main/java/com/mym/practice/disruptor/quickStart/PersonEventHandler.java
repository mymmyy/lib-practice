package com.mym.practice.disruptor.quickStart;

import com.lmax.disruptor.EventHandler;

/**事件处理的具体实现*/
public class PersonEventHandler implements EventHandler<PersonEvent> {
    @Override
    public void onEvent(PersonEvent personEvent, long l, boolean b) throws Exception {
        System.out.println("Event: " + personEvent);
    }
}
