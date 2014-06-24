package com.hazelcast.test;

import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.Member;
import com.hazelcast.core.MembershipListener;


public class LeaderTest {
    static int x;

    public static void main(String[] args) {

        Config cfg = new Config();
        Config config = new Config();
        NetworkConfig network = cfg.getNetworkConfig();

        JoinConfig join = network.getJoin();
        join.getTcpIpConfig().setEnabled(false);
        join.getAwsConfig().setEnabled(false);
        join.getMulticastConfig().setEnabled(true);
        cfg.setProperty("hazelcast.initial.min.cluster.size","4");

        HazelcastInstance instance = Hazelcast.newHazelcastInstance(cfg);
        //HazelcastInstance instance1=Hazelcast.newHazelcastInstance();

//        Map<Integer, String> mapCustomers = instance.getMap("customers");
//        mapCustomers.put(1, "Joe");
//        mapCustomers.put(2, "Ali");
//        mapCustomers.put(3, "Avi");
//
//        System.out.println("count" + x++);
//        System.out.println("Customer with key 1: " + mapCustomers.get(1));
//        System.out.println("Map Size:" + mapCustomers.size());

        System.out.println("is leader :"+isLeader(instance));
        System.out.println("leader  " + instance.getCluster()
                .getMembers().iterator().next());
        MembershipListener listener = new MyMembershipListener();
        instance.getCluster().addMembershipListener(listener);
        System.out.println("IsActive     " + isActive(instance));
       // instance.getCluster().getMembers().iterator().remove();//instance.getCluster().getMembers().iterator().next();

    }

    public static boolean isLeader(HazelcastInstance instance) {

        Member oldestMember = instance.getCluster()
                .getMembers().iterator().next();

        return oldestMember.localMember();
    }

    public static boolean isActive(HazelcastInstance instance) {
        boolean isActive = false;
        int clusterSize = instance.getCluster().getMembers().size();
        if (clusterSize > 3) {
            isActive = true;
        } else if(clusterSize>10){
            isActive=false;
        }
        return isActive;
    }



}
