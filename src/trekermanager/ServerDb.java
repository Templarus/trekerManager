package trekermanager;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Statement;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.sql.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import trekermanager.Device;

public class ServerDb implements Constatnts {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    protected Connection conn;
    private Statement st;
    private String sql;
    private ResultSet rs;
    public ResultSet rsClientFind;
    public String err = "";

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public ServerDb(String url, String user, String password) {
        if (connect(url, user, password) != Constatnts.READY) {
            JOptionPane.showMessageDialog(null, "Не удается устрановить соединение с базой", "Внимание", 0);
        }
        System.out.println("SERVERDb: constructor");
    }

    public int connect(String url, String user, String password) {

        try {
            conn = DriverManager.getConnection(url, user, password);//Подключаемся к баде данных
            st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);//Делаем многонаправленный ResultSet
            System.out.println("SUCCESSFULLY connected with database!");
        } catch (SQLException ex) {
            System.out.println("ServerDb:getData():Ошибка подключения или создание Statement - " + ex);
            return Constatnts.ERROR;
        }
        return Constatnts.READY;
    }

    @Override
    public int connect() {
        System.out.println("SERVERDb: connect");
        String[] args = {""};
        if (this.connect(args) == Constatnts.ERROR) {
            return Constatnts.ERROR;
        }
        return Constatnts.READY;
    }

    @Override
    public int connect(String[] args) {
        System.out.println("SERVERDb: connect");
        String url = args[0];//--Разбираем массив параметров
        String user = args[1];
        String password = args[2];
        try {
            conn = DriverManager.getConnection(url, user, password);//Подключаемся к баде данных
            st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);//Делаем многонаправленный ResultSet
            System.out.println("SUCCESSFULLY connected with database!");
        } catch (SQLException ex) {
            System.out.println("ServerDb:getData():Ошибка подключения или создание Statement - " + ex);
            return Constatnts.ERROR;
        }
        System.out.println("ok good connection with DATABASE");
        return Constatnts.READY;
    }

    public int insertDb(String sql) {
        int rezult = 0;
        try {
            st = conn.createStatement();
            rezult = st.executeUpdate(sql);
            st.close();
        } catch (SQLTimeoutException ex) {
            System.out.println("ServerDb:insertDb():Ошибка подключения или создание Statement - " + ex + " sql: " + sql);
        } catch (SQLException ex) {
            System.out.println("ServerDb:insertDb():Ошибка подключения или создание Statement - " + ex + " sql: " + sql);
        }
        return rezult;
    }

    public int updateDb(String sql) {
        int rezult = 0;
        try {
            st = conn.createStatement();
            rezult = st.executeUpdate(sql);
            st.close();
        } catch (SQLTimeoutException ex) {
            System.out.println("ServerDb:updateDb():Ошибка подключения или создание Statement - " + ex + " sql: " + sql);
        } catch (SQLException ex) {
            System.out.println("ServerDb:updateDb():Ошибка подключения или создание Statement - " + ex + " sql: " + sql);
        }
        return rezult;
    }

    public ResultSet selectDb(String sql) {
        try {
            st = conn.createStatement();
            rs = st.executeQuery(sql);
        } catch (SQLException ex) {
            System.out.println("ServerDb:selectDb():Ошибка подключения или создание Statement - " + ex + " sql: " + sql);
        }
        return rs;
    }

    public int setDevice(Device dev) {
        sql = "INSERT INTO STreker "
                + "(deviceId, port, password) "
                + "VALUES ('" + dev.getId() + "'," + dev.getPort() + ",'" + dev.getPassword() + "')";//
        if (insertDb(sql) > 0) {
            return Constatnts.OK;
        } else {
            return Constatnts.ERROR;
        }
    }

    public ResultSet getSTreker() {
        sql = "SELECT deviceId, port, password FROM STreker";
        return selectDb(sql);
    }

    public ArrayList<Device> getDeviceList() {
        ArrayList<Device> deviceList = new ArrayList<Device>();
        Device device;
        try {
            sql = "SELECT deviceId, port, password FROM STreker";
            ResultSet rs = selectDb(sql);
            while (rs.next()) {
                device = new Device(rs.getString(1), rs.getInt(2), rs.getString(3));
                deviceList.add(device);
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("ServerDb:getDeviceList():Ошибка подключения или создание Statement");
        }
        return deviceList;
    }

    public void updDeviceTimeWork() {

        for (Device dev : getDeviceList()) {
            //Получаем данные по последней работе
            Date dtLastW = new Date(-2208978000000L);
            Time timeLastW = new Time(-10800000l);
            DeviceTimeWork devLastWork = getDeviceTimeWorkLast(dev);
            Date dtLast = new Date(-2208978000000L);
            Time timeLast = new Time(-10800000l);

//            if (!devLastWork.getDeviceId().equals("")) {
//                dtLast = devLastWork.getDtWork();
//                timeLast = devLastWork.getTimeEnd();
//            };

                try {
                    sql = "SELECT    TOP(1)    deviceId, ISNULL(dtWork,''), timeBegin, ISNULL(timeEnd,ISNULL(timeBegin,'')) FROM DeviceTimeWork WHERE (deviceId = N'" + dev.getId() + "') ORDER BY dtWork DESC, timeBegin DESC, timeEnd DESC";
                    rs = selectDb(sql);
                    System.out.println("ДАТА ЗАВЕРШЕНИЯ" + sql);
                    while (rs.next()) {
                        dtLast = rs.getDate(2);
                        timeLast = rs.getTime(4);
                    }
                } catch (SQLException ex) {
                    System.out.println("ServerDb:updDeviceTimeWork()ПолучениеДатфЗавершения:Ошибка подключения или создание Statement");
                }


            while (hasNextTimeWork(dev, dtLast, timeLast) > 0) {

                //if (dtLast.equals(dtLastW) | timeLast.equals(timeLastW)) {
                try {
                    sql = "SELECT  TOP(1)  date, time FROM PackageDate WHERE (ISNULL(input2,0) = 1) AND deviceId = '" + dev.getId() + "' AND date > = '" + dtLast + "' AND time > '" + timeLast.toString() + "' ORDER BY date, time";
                    rs = selectDb(sql);
                    System.out.println("Получение данных по последней дате работы" + sql);
                    while (rs.next()) {
                        dtLast = rs.getDate(1);
                        timeLast = rs.getTime(2);
                    }
                } catch (SQLException ex) {
                    System.out.println("ServerDb:updDeviceTimeWork()ПолучениеДанныхПоПоследнейРаботе:Ошибка подключения или создание Statement");
                }

//                if (timeLast.toString().equals("00:00:00")) {
//                    sql = "INSERT INTO DeviceTimeWork "
//                            + "      (deviceId, dtWork, timeBegin) "
//                            + "VALUES('" + dev.getId() + "','" + dtLast.toString() + "',(SELECT TOP(1) time FROM PackageDate WHERE ISNULL(input2,0) = 1 AND deviceId = '" + dev.getId() + "' AND time > '00:00:00' AND date = '" + dtLast.toString() + "' ORDER BY time))";
//
//                    System.out.println("SQL3:" + sql);
//                } else {
                    sql = "INSERT INTO DeviceTimeWork "
                            + "      (deviceId, dtWork, timeBegin) "
                            + "VALUES('" + dev.getId() + "','" + dtLast.toString() + "','" + timeLast.toString() + "')";
                     //+ "VALUES('" + dev.getId() + "','" + dtLast.toString() + "',(SELECT TOP(1) time FROM PackageDate WHERE ISNULL(input2,0) = 1 AND deviceId = '" + dev.getId() + "' AND time > '" + timeLast.toString() + "' AND date = '" + dtLast.toString() + "' ORDER BY time))";
                    System.out.println("SQL43:" + sql);
                //};
                updateDb(sql);

                sql = "UPDATE DeviceTimeWork "
                        + "SET timeEnd = "
                        + "ISNULL("
                        + "(SELECT TOP(1) time FROM PackageDate WHERE deviceId = '" + dev.getId() + "' AND (date = '" + dtLast + "') AND (time >= '" + timeLast + "') and ISNULL(input2,0) = 0 ORDER BY time),"
                        + "'23:59:59') "
                        + "WHERE  deviceId = '" + dev.getId() + "' ANd dtWork = '" + dtLast + "' And timeBegin = '" + timeLast + "'";
                System.out.println("setDeviceTimeWorkEnd - SQL : " + sql);
                if (updateDb(sql) > 0) {
                    //System.out.println("Обновлено удачно!");

                };
                //devLastWork = getDeviceTimeWorkLast(dev);
                try {
                    sql = "SELECT    TOP(1)    deviceId, dtWork, timeBegin, ISNULL(timeEnd,'') FROM DeviceTimeWork WHERE (deviceId = N'" + dev.getId() + "') ORDER BY dtWork DESC, timeBegin DESC, timeEnd DESC";
                    rs = selectDb(sql);
                    System.out.println("ДАТА ЗАВЕРШЕНИЯ" + sql);
                    while (rs.next()) {
                        dtLast = rs.getDate(2);
                        timeLast = rs.getTime(4);
                    }
                } catch (SQLException ex) {
                    System.out.println("ServerDb:updDeviceTimeWork()ПолучениеДатфЗавершения:Ошибка подключения или создание Statement");
                }

                // }
//                devLastWork = getDeviceTimeWorkLast(dev);
//                if (!devLastWork.getDeviceId().equals("")) {
//                    dtLast = devLastWork.getDtWork();
//                    timeLast = devLastWork.getTimeEnd();
//                };
            }

//            if (getDeviceTimeWorkLast(dev).getDeviceId().equals("")) {
//                try {
//                    System.out.println("Проверка записи по устройству" + "\"" + dev.getId() + "\"");
//
//                    //Проверяем есть ли данные по работе устройства в таблице PackageDate
//                    sql = "SELECT COUNT(*) AS Count FROM PackageDate WHERE (deviceId = N'" + dev.getId() + "')";
//                    ResultSet rs = selectDb(sql);
//                    int count = 0;
//                    while (rs.next()) {
//                        count = rs.getInt(1);
//                    }
//                    if (count == 0) {
//                        System.out.println("Нет записи по устройсту - " + "\"" + dev.getId() + "\"");
//                        continue;
//                    } else {
//                        for(int i = 0; i < count; i++){
//                        Date dtLast = new Date(-2208988800000L - 9000000L - 1800000L);
//                        setDeviceWorkTime(dev, dtLast);
//                        }
//                    }
//                } catch (SQLException ex) {
//                    Logger.getLogger(ServerDb.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            };
            System.out.println("Закончили формирования работы...");

        }
    }

    private void setDeviceWorkTime(Device dev, Date dtLast, DeviceTimeWork devLastWork, Time timeLast) {
//        Time t = new Time(-10800000l);
//        sql = "SELECT date FROM PackageDate WHERE (ISNULL(input2, 0) = 1) AND (deviceId = N'" + dev.getId() + "') AND date > '" + dtLast + "' GROUP BY date ORDER BY date";
//        System.out.println("setDeviceWorkTime  - SQL : " + sql);
//        rs = selectDb(sql);

        //try {
//            while (rs.next()) {
//                Date dt = rs.getDate(1);
//                while (hasNextTimeWork(dev, dt) > 0) {
//                    System.out.println("Мы зашли в блок проверки времени");
//                    DeviceTimeWork devLastWork = getDeviceTimeWorkLast(dev);
//                    System.out.println("devLastWork - " + devLastWork.getDeviceId());
        if (devLastWork.getDeviceId().equals("")) {
            sql = "INSERT INTO DeviceTimeWork "
                    + "      (deviceId, dtWork, timeBegin) "
                    + "VALUES('" + dev.getId() + "','" + dtLast.toString() + "',(SELECT TOP(1) time FROM PackageDate WHERE ISNULL(input2,0) = 1 AND deviceId = '" + dev.getId() + "' AND time > '00:00:00' AND date = '" + dtLast.toString() + "' ORDER BY time))";

            System.out.println("SQL3:" + sql);
        } else {
            sql = "INSERT INTO DeviceTimeWork "
                    + "      (deviceId, dtWork, timeBegin) "
                    + "VALUES('" + dev.getId() + "','" + dtLast.toString() + "',(SELECT TOP(1) time FROM PackageDate WHERE ISNULL(input2,0) = 1 AND deviceId = '" + dev.getId() + "' AND time > '" + timeLast.toString() + "' AND date = '" + dtLast.toString() + "' ORDER BY time))";

            //System.out.println("SQL4:" + sql);
        };

        //System.out.println("SQL2:" + sql);
        updateDb(sql);
        // }
//            }
//
//        } catch (SQLException ex) {
//            System.out.println("ServerDb:setDeviceWorkTime():Ошибка подключения или создание Statement");
//        }

    }

    private int hasNextTimeWork(Device dev, Date dtLast, Time timeLast) {
//        Time timeLast = new Time(-10800000l);
//        //DeviceTimeWork devLastWork = getDeviceTimeWorkLast(dev);
//
//        sql = "SELECT TOP(1)  timeEnd FROM DeviceTimeWork WHERE (deviceId = N'" + dev.getId() + "') ORDER BY dtWork DESC, timeBegin DESC, timeEnd DESC";
//       // System.out.println("hasNextTimeWork - SQL : " + sql);
//        rs = selectDb(sql);
//        try {
//            while (rs.next()) {
//                timeLast = rs.getTime(1);
//            }
//        } catch (SQLException ex) {
//            System.out.println("ServerDb:hasNextTimeWork()ОшибкаПолученияПоследнегоВремяРаботы:Ошибка подключения или создание Statement");
//        }
//
//        System.out.println("devLastWork.getTimeEnd() : " + timeLast);
        int countRecord = 0;
        sql = "SELECT COUNT(*) AS Count FROM PackageDate WHERE (deviceId = '" + dev.getId() + "' ) AND  (date > '" + dtLast.toString() + "') AND (time > '" + timeLast + "') AND (ISNULL(input2,0) = 1)";
        System.out.println("hasNextTimeWork - SQL : " + sql);
        rs = selectDb(sql);
        try {
            while (rs.next()) {
                countRecord = rs.getInt(1);
            }

        } catch (SQLException ex) {
            System.out.println("ServerDb:hasNextTimeWork():Ошибка подключения или создание Statement");
        }

        return countRecord;
    }

    public DeviceTimeWork getDeviceTimeWorkLast(Device dev) {
        DeviceTimeWork devLastWork = new DeviceTimeWork("", new Date(-2208988800000L), new Time(-10800000l), new Time(-10800000l));
        sql = "SELECT TOP (1) deviceId, dtWork, timeBegin, ISNULL(timeEnd,'') FROM UltraFiolet.dbo.DeviceTimeWork WHERE (deviceId = '" + dev.getId() + "') ORDER BY dtWork DESC, timeEnd DESC, timeBegin DESC";
        System.out.println("getDeviceTimeWorkLast - SQL:" + sql);
        rs = selectDb(sql);
        try {

            while (rs.next()) {
                devLastWork.setDeviceId(rs.getString(1));
                devLastWork.setDtWork(rs.getDate(2));
                devLastWork.setTimeBegin(rs.getTime(3));
                devLastWork.setTimeEnd(rs.getTime(4));
            }

            if (devLastWork.getDeviceId().equals("")) {
                return devLastWork;
            }

            if (devLastWork.getTimeEnd().toString().equals("00:00:00")) {
                System.out.println("1 Девайс : " + devLastWork.getDeviceId() + "Время работы начало: " + devLastWork.getTimeBegin());
                setDeviceTimeWorkEnd(devLastWork, dev);
                getDeviceTimeWorkLast(dev);
            }

        } catch (SQLException ex) {
            System.out.println("ServerDb:getDeviceTimeWorkLast():Ошибка подключения или создание Statement");
        }
        return devLastWork;
    }
    //Вызываем если время конца работы на устройсве не заполнено

    public void setDeviceTimeWorkEnd(DeviceTimeWork devLastWork, Device dev) {
        sql = "UPDATE DeviceTimeWork "
                + "SET timeEnd = "
                + "ISNULL("
                + "(SELECT TOP(1) time FROM PackageDate WHERE deviceId = '" + devLastWork.getDeviceId() + "' AND (date = '" + devLastWork.getDtWork() + "') AND (time >= '" + devLastWork.getTimeBegin() + "') and ISNULL(input2,0) = 0 ORDER BY time),"
                + "'23:59:59') "
                + "WHERE  deviceId = '" + devLastWork.getDeviceId() + "' ANd dtWork = '" + devLastWork.getDtWork() + "' And timeBegin = '" + devLastWork.getTimeBegin() + "'";
        System.out.println("setDeviceTimeWorkEnd - SQL : " + sql);
        if (updateDb(sql) > 0) {
            System.out.println("Обновлено удачно!");
            getDeviceTimeWorkLast(dev);

        };

    }

    public synchronized int setPackageData(PackageData pd) {
        System.out.println("pd.date=" + pd.date);

        sql = "INSERT INTO PackageDate "
                + "(deviceId,date, time, lat, lon, speed, course, height, sats, hdop, digitinput, digitouput, ads, ibutton, params, input2) \n"
                + "VALUES ('" + pd.id + "','" + Methods.dateToString(pd.date) + "','" + Methods.timeToString(pd.time) + "','" + pd.lat + "','" + pd.lon + "','" + pd.speed + "','" + pd.course + "','" + pd.height + "','" + pd.sats + "','" + pd.hdop + "','" + pd.digitinput + "','" + pd.digitouput + "','" + pd.ads + "','" + pd.ibutton + "','" + pd.params + "','" + pd.input2 + "')";//
        if (insertDb(sql) > 0) {
            return Constatnts.OK;
        } else {
            return Constatnts.ERROR;
        }

    }

    @Override
    public int disconnect() {
        try {
            if (!conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServerDb.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Constatnts.DISCONNECT;
    }

}
