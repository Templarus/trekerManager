package trekermanager;

import UI.Start;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DeviceListener implements Runnable {

    private ServerSocket serverSocket;
    private Device device;
    private Socket cs;

    public DeviceListener() {
        this(new Device());
    }

    public DeviceListener(Device d) {
        this.device = d;
    }

    private void makeConnection() {
        try {
            serverSocket = new ServerSocket(device.getPort());
            Socket cs = new Socket();
            cs = serverSocket.accept();
            boolean status=Start.mf.getWatcherStatus(device);
            while (status) {
                if (!cs.isClosed()) {
                    Start.mf.setWatcherStatus(device, true);
                    Start.mf.deviceConnection(device.getId(), true);
                    InputStream in = cs.getInputStream();
                    OutputStream out = cs.getOutputStream();
                    byte[] incoming = new byte[256];
                    int length = in.read(incoming);
                    System.out.println("Client query(" + length + " bytes):" + new String(incoming).trim());
                    String reply = makeAnswer(incoming);
                    out.write(reply.getBytes());
                    System.out.println("DeviceListener: getPacket executed");
                } else {
                    Start.mf.deviceConnection(device.getId(), false);
                }
            }
            Start.mf.setWatcherStatus(device, false);
        } catch (IOException ex) {
            Logger.getLogger(DeviceListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String makeAnswer(byte[] data) {
        String message[] = new String(data).trim().split("#");
        switch (message[1]) {
            case "L":
                return "#AL#1\r\n";

            case "D":
                getData(message[2]);
                return "#AD#1\r\n";

            case "P":
                return "#AP#\r\n";
        }
        return "";
    }

    private Pack getData(String message) {

        String body[] = message.split(";");
        int date = 0;
        int time = 0;
        String lat = "";
        String lon = "";
        int speed = 0;
        int course = 0;
        int height = 0;
        int sats = 0;
        float hdop = 0;
        int digitinput = 0;
        int digitouput = 0;
        String ads;
        String ibutton;
        String params;

        date = Integer.parseInt(body[0]);
        time = Integer.parseInt(body[1]);
        lat = body[2] + "," + body[3];
        lon = body[4] + "," + body[5];
        if (!body[6].equals("NA")) {
            speed = Integer.parseInt(body[6]);
        }
        if (!body[7].equals("NA")) {
            course = Integer.parseInt(body[7]);
        }
        if (!body[8].equals("NA")) {
            height = Integer.parseInt(body[8]);
        }
        if (!body[6].equals("NA")) {
            sats = Integer.parseInt(body[9]);
        }
        if (!body[6].equals("NA")) {
            hdop = Float.parseFloat(body[10]);
        }
        if (!body[11].equals("NA")) {
            digitinput = Integer.parseInt(body[11]);
        }
        if (!body[12].equals("NA")) {

            digitouput = Integer.parseInt(body[12]);
        }
        ads = body[13];
        ibutton = body[14];
        params = body[15];

        PackageData D = new PackageData(device.getId(), date, time, lat, lon, speed, course, height, sats, hdop, digitinput, digitouput, ads, ibutton, params);
        System.out.println("DeviceListener: getData executed");

        return D;
    }

    @Override
    public void run() {
        System.out.println("DeviceListener: Listener" + device.getId() + "__" + device.getPort() + " created");
        makeConnection();
    }
}
