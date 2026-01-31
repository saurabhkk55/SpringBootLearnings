package DesignPattern;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

interface Channel {
    void addNewSubscriber(Subscriber subscriber);

    void removeSubscriber(Subscriber subscriber);

    void addNewContentAndNotifyAllSubscribers(String newContent);

    String getChannelName();

    String getChannelInfo();
}

class ChannelImpl implements Channel {
    String name;
    Set<Subscriber> subscriberList;
    Set<String> contents;

    public ChannelImpl(String name) {
        this.name = name;
        this.subscriberList = new HashSet<>();
        this.contents = new HashSet<>();
    }

    @Override
    public void addNewSubscriber(Subscriber subscriber) {
        subscriberList.add(subscriber);
    }

    @Override
    public void removeSubscriber(Subscriber subscriber) {
        subscriberList.remove(subscriber);
    }

    @Override
    public void addNewContentAndNotifyAllSubscribers(String newContent) {
        contents.add(newContent);
        System.out.println("New content: " + newContent + " added to channel " + this.name);

        if(subscriberList.isEmpty()) {
            System.out.println("No subscribers to notify about this new content.");
            return;
        }

        for(Subscriber sub : subscriberList) {
            sub.update(newContent);
        }
    }

    @Override
    public String getChannelName() {
        return this.name;
    }

    @Override
    public String getChannelInfo() {
        return this.toString();
    }

    @Override
    public String toString() {
        return "ChannelImpl{" +
                "name='" + name + '\'' +
                ", subscriberList=" + subscriberList +
                ", contents=" + contents +
                '}';
    }
}

interface Subscriber {
    void update(String content);

    void subscribeChannel(Channel channel);

    void unSubscribeChannel(Channel channel);
}

class SubscriberImpl implements Subscriber {
    String name;

    SubscriberImpl(String subscriberName) {
        this.name = subscriberName;
    }

    @Override
    public void update(String content) {
        System.out.println("Hey " + name + ", new content: " + content + " is available.");
    }

    @Override
    public void subscribeChannel(Channel channel) {
        channel.addNewSubscriber(this);
    }

    @Override
    public void unSubscribeChannel(Channel channel) {
        channel.removeSubscriber(this);
    }
}

public class ObserverDesignPattern {
    static void main() {
        Subscriber jackSub = new SubscriberImpl("Jack");

        Channel javaRocket = new ChannelImpl("JavaRocket");

        jackSub.subscribeChannel(javaRocket);

        javaRocket.addNewContentAndNotifyAllSubscribers("Java OOPS Course");

        String channelInfo = javaRocket.getChannelInfo();

        System.out.println(channelInfo);

        Subscriber saurabhSub = new SubscriberImpl("Saurabh");

        saurabhSub.subscribeChannel(javaRocket);

        javaRocket.addNewContentAndNotifyAllSubscribers("Java Collection Course");

        System.out.println(channelInfo);

        String channelName = javaRocket.getChannelName();

        System.out.println("Channel Name: " + channelName);

        System.out.println("-------------------------------");
        System.out.println("-------------------------------");

        Channel bollywoodMelodies = new ChannelImpl("Bollywood Melodies");

        saurabhSub.subscribeChannel(bollywoodMelodies);

        bollywoodMelodies.addNewContentAndNotifyAllSubscribers("A.R. Rahman PlayList");

        System.out.println("-------------------------------");
        System.out.println("-------------------------------");

        jackSub.unSubscribeChannel(javaRocket);

        javaRocket.addNewContentAndNotifyAllSubscribers("Java Streams Course");

        System.out.println("-------------------------------");
        System.out.println("-------------------------------");

        Channel storyTeller = new ChannelImpl("Story Teller");

        storyTeller.addNewContentAndNotifyAllSubscribers("Horror story");
    }
}
