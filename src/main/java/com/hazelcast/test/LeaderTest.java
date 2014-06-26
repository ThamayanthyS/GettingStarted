package com.hazelcast.test;

import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.Member;
import com.hazelcast.core.MembershipListener;

import java.util.Iterator;


public class LeaderTest {

    public static void main(String[] args) {

        Config cfg = new Config();

        NetworkConfig network = cfg.getNetworkConfig();
        cfg.getGroupConfig().setName("split");


        JoinConfig join = network.getJoin();
        join.getTcpIpConfig().setEnabled(false);
        join.getAwsConfig().setEnabled(false);
        join.getMulticastConfig().setEnabled(true);
        cfg.setProperty("hazelcast.initial.min.cluster.size","4");

        HazelcastInstance instance = Hazelcast.newHazelcastInstance(cfg);

        MembershipListener listener = new MyMembershipListener();
        instance.getCluster().addMembershipListener(listener);

        System.out.println("instance   " + instance.getCluster().getMembers().size());
        Iterator  iterator=instance.getCluster().getMembers().iterator();
        System.out.println("getLeader(instance) "+getLeader(instance));
        System.out.println("Is active  "+isActive(instance));
        while (iterator.hasNext())
            System.out.println(iterator.next().toString());


    }

    public static boolean isLeader(HazelcastInstance instance) {

        Member oldestMember = instance.getCluster()
                .getMembers().iterator().next();

        return oldestMember.localMember();
    }

    public static boolean isActive(HazelcastInstance instance) {
        boolean isActive = false;
        int clusterSize = instance.getCluster().getMembers().size();
        isActive=instance.getLifecycleService().isRunning();
//        if (clusterSize > 3) {
//            isActive = true;
//        } else if(clusterSize>10){
//            isActive=false;
//        }
        return isActive;
    }
    public static Member getLeader(HazelcastInstance instance){
        return instance.getCluster()
                .getMembers().iterator().next();
    }
}
