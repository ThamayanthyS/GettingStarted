package com.hazelcast.test;

import com.hazelcast.core.MemberAttributeEvent;
import com.hazelcast.core.MembershipEvent;
import com.hazelcast.core.MembershipListener;

/**
 * Created with IntelliJ IDEA.
 * User: thamayanthy
 * Date: 6/6/14
 * Time: 1:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class MyMembershipListener implements MembershipListener {
    @Override
    public void memberAdded(MembershipEvent membershipEvent) {
        System.out.println("MemberAdded");
    }

    @Override
    public void memberRemoved(MembershipEvent membershipEvent) {
        //To chang e body of implemented methods use File | Settings | File Templates.
        System.out.println("Member  Removed");
    }

    @Override
    public void memberAttributeChanged(MemberAttributeEvent memberAttributeEvent) {
        System.out.println("Member Attribute Changed");
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
