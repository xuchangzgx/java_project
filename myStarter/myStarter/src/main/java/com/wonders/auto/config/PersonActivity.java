package com.wonders.auto.config;

public class PersonActivity {

    private Person person;

    public PersonActivity(Person person) {
        this.person = person;
    }

    public void eat(){
        System.out.println( person.getName()+"-正在吃饭。。。" );
    }

}
