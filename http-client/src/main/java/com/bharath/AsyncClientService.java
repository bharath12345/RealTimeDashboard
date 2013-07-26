package com.bharath;

import javax.management.MXBean;

/**
 * User: bharadwaj
 * Date: 26/07/13
 * Time: 5:55 PM
 */
@MXBean
public interface AsyncClientService {

    public void firePostRequests(long clientCount, long requestCounter);
}
