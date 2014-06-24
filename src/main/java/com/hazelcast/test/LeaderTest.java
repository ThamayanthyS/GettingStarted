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
    public static int count=0;
    public static void main(String[] args) {
        count++;
        Config cfg = new Config();
        Config config = new Config();
        NetworkConfig network = cfg.getNetworkConfig();
        cfg.setInstanceName(""+count);

        JoinConfig join = network.getJoin();
        join.getTcpIpConfig().setEnabled(false);
        join.getAwsConfig().setEnabled(false);
        join.getMulticastConfig().setEnabled(true);
        cfg.setProperty("hazelcast.initial.min.cluster.size","4");

        HazelcastInstance instance = Hazelcast.newHazelcastInstance(cfg);
//        if(count>3)
//            Hazelcast.getHazelcastInstanceByName("3").shutdown();

        System.out.println("is leader :"+isLeader(instance));
        System.out.println("leader  " + instance.getCluster()
                .getMembers().iterator().next());
        MembershipListener listener = new MyMembershipListener();
        instance.getCluster().addMembershipListener(listener);
        System.out.println("IsActive     " + isActive(instance));
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
