package com.brahmasys.bts.joinme;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by apple on 22/09/16.
 */
public class Connectivity_Checking {


    public static boolean isConnectingToInternet() {
        InetAddress inetAddress = null;
        try {
            Future<InetAddress> future = Executors.newSingleThreadExecutor().submit(new Callable<InetAddress>() {
                @Override
                public InetAddress call() {
                    try {
                        return InetAddress.getByName("google.com");
                    } catch (UnknownHostException e) {
                        return null;
                    }
                }
            });
            inetAddress = future.get(2000, TimeUnit.MILLISECONDS);
            future.cancel(true);

        } catch (Exception e) {
        }
        return inetAddress != null && !inetAddress.equals("");
    }



}
