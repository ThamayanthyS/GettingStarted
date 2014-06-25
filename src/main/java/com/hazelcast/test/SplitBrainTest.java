package com.hazelcast.test;

import com.hazelcast.config.Config;
import com.hazelcast.core.*;
import com.hazelcast.instance.GroupProperties;
import com.hazelcast.instance.Node;
import com.hazelcast.instance.TestUtil;

import java.util.Iterator;
import java.util.concurrent.CountDownLatch;

/**
 * Created with IntelliJ IDEA.
 * User: thamayanthy
 * Date: 6/26/14
 * Time: 12:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class SplitBrainTest {
    public static void main(String args []){
        try {
//            splitBrain(true);
            testSplitBrain();
        } catch (Exception e) {

            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }



    }

    public static void testSplitBrain() throws InterruptedException {
        System.out.println("STARTED%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        Config config = new Config();
        config.getGroupConfig().setName("split");
        config.setProperty("hazelcast.initial.min.cluster.size", "1");
        config.setProperty(GroupProperties.PROP_MERGE_FIRST_RUN_DELAY_SECONDS, "5");
        config.setProperty(GroupProperties.PROP_MERGE_NEXT_RUN_DELAY_SECONDS, "5");
        HazelcastInstance h1 = Hazelcast.newHazelcastInstance(config);
        HazelcastInstance h2 = Hazelcast.newHazelcastInstance(config);
        HazelcastInstance h3 = Hazelcast.newHazelcastInstance(config);
        final CountDownLatch latch = new CountDownLatch(1);
        h3.getLifecycleService().addLifecycleListener(new LifecycleListener() {
            public void stateChanged(LifecycleEvent event) {
                if (event.getState() == LifecycleEvent.LifecycleState.MERGED) {
                    latch.countDown();
                }
            }
        });

        System.out.println("h1   "+h1.getCluster().getMembers().size());
        Iterator iterator=h1.getCluster().getMembers().iterator();
        System.out.println("getLeader(h1) "+getLeader(h1));
        while (iterator.hasNext())
            System.out.println(iterator.next().toString());

        System.out.println("h2   "+h2.getCluster().getMembers().size());
        iterator=h2.getCluster().getMembers().iterator();
        System.out.println("getLeader(h2) "+getLeader(h2));
        while (iterator.hasNext())
            System.out.println(iterator.next().toString());

        System.out.println("h3   "+h3.getCluster().getMembers().size());
        iterator=h3.getCluster().getMembers().iterator();
        System.out.println("getLeader(h3) "+getLeader(h3));
        while (iterator.hasNext())
            System.out.println(iterator.next().toString());

        closeConnectionBetween(h1, h3);
        closeConnectionBetween(h2, h3);
        Thread.sleep(1000);

        System.out.println("h1   "+h1.getCluster().getMembers().size());
        iterator=h1.getCluster().getMembers().iterator();
        System.out.println("getLeader(h1) "+getLeader(h1));
        while (iterator.hasNext())
            System.out.println(iterator.next().toString());

        System.out.println("h2   "+h2.getCluster().getMembers().size());
        iterator=h2.getCluster().getMembers().iterator();
        System.out.println("getLeader(h2) "+getLeader(h2));
        while (iterator.hasNext())
            System.out.println(iterator.next().toString());

        System.out.println("h3   "+h3.getCluster().getMembers().size());
        iterator=h3.getCluster().getMembers().iterator();
        System.out.println("getLeader(h3) "+getLeader(h3));
        while (iterator.hasNext())
            System.out.println(iterator.next().toString());
//        System.out.println(latch.await(30, TimeUnit.SECONDS));
//        System.out.println(h1.getCluster().getMembers().size());
//        System.out.println(h2.getCluster().getMembers().size());
//        System.out.println(h3.getCluster().getMembers().size());

        System.out.println("ENDED%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
    }

    private static void closeConnectionBetween(HazelcastInstance h1, HazelcastInstance h2) {
        if (h1 == null || h2 == null) return;
        final Node n1 = TestUtil.getNode(h1);
        final Node n2 = TestUtil.getNode(h2);
        n1.clusterService.removeAddress(n2.address);
        n2.clusterService.removeAddress(n1.address);
    }

    public static boolean isLeader(HazelcastInstance instance) {

        Member oldestMember = instance.getCluster()
                .getMembers().iterator().next();

        return oldestMember.localMember();
    }

    public static Member getLeader(HazelcastInstance instance){
        return instance.getCluster()
                .getMembers().iterator().next();
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
