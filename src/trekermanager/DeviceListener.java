package trekermanager;

import UI.Start;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

//Класс предназначенный для создания и обработки соединения с одним устройством
public class DeviceListener implements Runnable {

    private ServerSocket serverSocket;
    private Device device;
    private Socket cs;
    private InputStream in;
    private OutputStream out;
    private int length = 0;
    private Socket clientSocket;
    byte[] incoming;

    private boolean status = true;  // статус соединения\открытого ClientSocker
    private long time1;
    private long time2;
    boolean switcher = true;

    public DeviceListener() {
        this(new Device()); // пустой конструктор Device вернёт элемент с стандартными данными
    }

    public DeviceListener(Device d) {
        this.device = d;
    }

    private void makeConnection() {
        try {
            serverSocket = new ServerSocket(device.getPort());
            clientSocket = new Socket();
            clientSocket = serverSocket.accept();
            status = Start.mf.getWatcherStatus(device); // в локальную переменную записываем значение из MainForm - изначально оно = true

            while (Start.mf.getWatcherStatus(device)) { //т.к. сокет был закрыт - возвращаем зачение false (которое должно быть уже задано в нужной коллекции
                if (!clientSocket.isClosed()) { // подлежит доработке для адекватной отработки кода
                    Start.mf.setWatcherStatus(device, true); // логическое дублирование, если сокет запущен вторично. вероятно бесполезно
                    Start.mf.deviceConnection(device.getId(), true); // если .accept прошёл - TCP соединение установлено
                 
                    in = clientSocket.getInputStream();
                    out = clientSocket.getOutputStream();
                 
                    incoming = new byte[256];
                    length = in.read(incoming);
                  
                    System.out.println("Client query(" + length + " bytes):" + new String(incoming).trim());
                    String reply = makeAnswer(incoming); // выполняется метод, который возвращает устройству требуемый ответ в зависимости от пришедшего пакета
                
                    if (reply.equals("empty")) { //если вернулось empty - устройство разорвало соединение со своей стороны.
                        System.err.println("DeviceListener: empty answer");
                        Start.mf.setWatcherStatus(device, false); // соединение разорвано и требуется запустить новый поток с новым Listener для этого порта и устройства
                    } else {
                        out.write(reply.getBytes());
                        System.out.println("DeviceListener: getPacket executed");
                    }
                }
            }
        } catch (IOException ex) {
            closeListener();
            Start.mf.deviceConnection(device.getId(), false);
            System.err.println("IOException in listener " + device.getId() + " : " + ex.getMessage());

        }
    }

    private String makeAnswer(byte[] data) {
        String message[] = new String(data).trim().split("#");
//        System.out.println("Message. mess[0]=" + message[0] + "mess1="+message[1]+"  mess length=" + message.length);
        if (message.length > 0) {
            switch (message[1]) {
                case "L":
                    // System.err.println("1");
                    return "#AL#1\r\n";

                case "D": // пакет с данными - требуется его разборка, вызывается метод getData, в котором происходит создание элемента класса PackageData( абстр Pack)
                    //System.err.println("2");
                    getData(message[2]);
                    return "#AD#1\r\n";

                case "P":
                    // System.err.println("3");
                    return "#AP#\r\n";
            }
        }
        System.err.println("4");
        return "empty";

    }
// разборка пакета с данными на собственно данные

    private Pack getData(String message) {
//D#020100;030350;NA;NA;NA;NA;NA;NA;NA;NA;NA;NA;NA;;000000000000;IDX:1:108,MCC:1:250,MNC:1:1,LAC:1:407,CID:1:56625,Vext:1:5977,IN1:1:0,IN2:1:0
        String body[] = message.split(";");
        String date = "";
        String time = "";
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

        date = body[0];
        time = body[1];
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

    private void closeListener() {//метод закрывающий текущий листенер

        try {
            serverSocket.close();
            cs.close();
            Start.mf.deviceConnection(device.getId(), false);
            Start.mf.deviceStatus(device.getId(), false);
        } catch (IOException ex1) {
            Logger.getLogger(DeviceListener.class.getName()).log(Level.SEVERE, null, ex1);
        }
    }
}
