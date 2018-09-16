package com.mym.practice.disruptor.quickStart;

/**
 *  传递的消息事件
 */
public class PersonEvent {

    private int age;

    private String name;

    private int gender;

    public PersonEvent(int age, String name, int gender) {
        this.age = age;
        this.name = name;
        this.gender = gender;
    }

    public boolean modifyAge(int age) {
        this.age = age;
        return true;
    }

    @Override
    public String toString() {
        return "PersonEvent{" +
                "age=" + age +
                ", name='" + name + '\'' +
                ", gender=" + gender +
                '}';
    }
}
