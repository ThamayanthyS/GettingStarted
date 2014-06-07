package com.hazelcast.test;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.Member;
import com.hazelcast.core.MembershipListener;

import java.util.Map;
import java.util.Queue;


public class LeaderTest {
    static int x;
    public static void main(String[] args) {

        Config cfg = new Config();
        HazelcastInstance instance = Hazelcast.newHazelcastInstance(cfg);
        Map<Integer, String> mapCustomers = instance.getMap("customers");
        mapCustomers.put(1, "Joe");
        mapCustomers.put(2, "Ali");
        mapCustomers.put(3, "Avi");

        System.out.println("count" + x++);
        System.out.println("Customer with key 1: "+ mapCustomers.get(1));
        System.out.println("Map Size:" + mapCustomers.size());

        System.out.println(isLeader(instance));
        System.out.println("leader  "+instance.getCluster()
                .getMembers().iterator().next());
        MembershipListener listener = new MyMembershipListener();
        instance.getCluster().addMembershipListener(listener);

    }

        public static boolean isLeader(HazelcastInstance instance) {

            Member oldestMember = instance.getCluster()
                    .getMembers().iterator().next();

            return oldestMember.localMember();
        }

}
