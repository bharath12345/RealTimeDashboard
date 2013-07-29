package com.bharath;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * User: bharadwaj
 * Date: 29/07/13
 * Time: 2:16 PM
 */
public class Queue {

    private static ConcurrentLinkedQueue queue = new ConcurrentLinkedQueue();

    public static ConcurrentLinkedQueue getQueue() {
        return queue;
    }

    public static void setQueue(ConcurrentLinkedQueue queue) {
        Queue.queue = queue;
    }
}
