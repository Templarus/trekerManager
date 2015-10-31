package trekermanager;

import UI.Start;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Watcher implements Runnable {

    private Map DeviceList = new HashMap<String, Device>();
    private Set<String> keys;

    public Watcher() {

    }

    private void Check() {

        while (true) {
            keys = Start.mf.getDevicesKeySet();
            DeviceList = Start.mf.getDeviceList();
            for (String key : keys) {
                Device device = (Device) DeviceList.get(key);
                if(Start.mf.getWatcherStatus(device)==false)
                {
                    Start.mf.createListener(device);
                }
                
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Watcher.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void run() {
        Check();
    }
}
