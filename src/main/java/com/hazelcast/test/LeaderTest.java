package com.hazelcast.test;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.Member;

import java.util.Map;
import java.util.Queue;

/**
 * Created with IntelliJ IDEA.
 * User: thamayanthy
 * Date: 6/3/14
 * Time: 9:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class LeaderTest {
    static LeaderTest.HazelcastLeaderElection hazelcastLeaderElection;//=  new HazelcastLeaderElection();
    static int x;
    public static void main(String[] args) {
        hazelcastLeaderElection=  new HazelcastLeaderElection();
        Config cfg = new Config();
        HazelcastInstance instance = Hazelcast.newHazelcastInstance(cfg);
        Map<Integer, String> mapCustomers = instance.getMap("customers");
        mapCustomers.put(1, "Joe");
        mapCustomers.put(2, "Ali");
        mapCustomers.put(3, "Avi");

        System.out.println("count" + x++);
        System.out.println("Customer with key 1: "+ mapCustomers.get(1));
        System.out.println("Map Size:" + mapCustomers.size());

        Queue<String> queueCustomers = instance.getQueue("customers");
        queueCustomers.offer("Tom");
        queueCustomers.offer("Mary");
        queueCustomers.offer("Jane");
        System.out.println("Queue size: " + queueCustomers.size());
        System.out.println("First customer: " + queueCustomers.poll());
        System.out.println("Second customer: "+ queueCustomers.peek());
        System.out.println("Queue size: " + queueCustomers.size());
        System.out.println(hazelcastLeaderElection.isLeader(instance));
        System.out.println("leader  "+instance.getCluster()
                .getMembers().iterator().next());

    }


    public static class HazelcastLeaderElection {

        public boolean isLeader(HazelcastInstance instance) {

            Member oldestMember = instance.getCluster()
                    .getMembers().iterator().next();

            return oldestMember.localMember();
        }
    }
}
