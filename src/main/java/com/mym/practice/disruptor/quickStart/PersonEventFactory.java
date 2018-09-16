package com.mym.practice.disruptor.quickStart;

import com.lmax.disruptor.EventFactory;

/**
 * 事件工厂
 */
public class PersonEventFactory  implements EventFactory<PersonEvent> {
    @Override
    public PersonEvent newInstance() {
        return new PersonEvent(12,"cat", 1);
    }
}
