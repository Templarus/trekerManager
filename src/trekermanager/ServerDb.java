<<<<<<< HEAD
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
import java.util.Date;
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
        //Дата последней записи по устройству
        Date dtLast = null;
        //Время начала последней работы
        Time timeLastBegin = null;
        //Время окончания последней работы
        Time timeLastEnd = null;
        
        for (Device dev : getDeviceList()) {
//            dtLast = getDateDeviceLastWork(dev);
//            timeLast = getTimeDeviceLastWork(dev,dtLast);
        }
    }

    public Date getDateDeviceLastWork(Device dev) {
        Date dtLast = null;
        sql = "SELECT ISNULL(MAX(dtWork),'') AS dt FROM DeviceTimeWork WHERE (deviceId = N'" + dev.getId() + "')";
        ResultSet rs = selectDb(sql);
        try {
            while (rs.next()) {
                    dtLast = rs.getDate(1);
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("ServerDb:getDateDeviceLastWork():Ошибка подключения или создание Statement");
        }
        return dtLast;
    }
    
    public Time getTimeDeviceLastWork(Device dev, Date dtLast) {
        Time timeLast = null;
        sql = "SELECT ISNULL(MAX(timeEnd),ISNULL(MAX(timeEnd),'')) AS dt FROM DeviceTimeWork WHERE (deviceId = N'" + dev.getId() + "') AND dtWork > = '" + dtLast + "'";
        ResultSet rs = selectDb(sql);
        try {
            while (rs.next()) {
                System.out.println("Видим выборку - " + rs.getTime(1));
                    timeLast = rs.getTime(1);
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("ServerDb:getTimeDeviceLastWork():Ошибка подключения или создание Statement");
        }
        System.out.println("Видим переменную - " + timeLast);
        return timeLast;
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
=======
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
import java.util.ArrayList;
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
            rs = st.executeQuery(sql);
        } catch (SQLException ex) {
            System.out.println("ServerDb:selectDb():Ошибка подключения или создание Statement - " + ex + " sql: " + sql);
        }
        return rs;
    }

   public int setDevice(Device dev){
         sql = "INSERT INTO STreker "
                + "(deviceId, port, password) "
                + "VALUES ('" + dev.getId() + "'," + dev.getPort() + ",'" + dev.getPassword() + "')";//
        if (insertDb(sql) > 0) {
            return Constatnts.OK;
        } else {
            return Constatnts.ERROR;
        }
   } 
   
   public ResultSet getSTreker(){
       sql = "SELECT deviceId, port, password FROM STreker";
       
       return selectDb(sql);
   }
    
    
    public ArrayList<Device> getDeviceList(){
        ArrayList<Device> deviceList = new ArrayList<Device>();
        Device device;
         try {
            sql = "SELECT deviceId, port, password FROM STreker";
           ResultSet rs = selectDb(sql);
            while (rs.next()) {
                device = new Device(rs.getString(1), rs.getInt(2),rs.getString(3));
                deviceList.add(device);
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("ServerDb:getDeviceList():Ошибка подключения или создание Statement");
        }
        
        
        
        return deviceList;    
    }
    
    public synchronized int setPackageData(PackageData pd) {
        System.out.println("pd.date=" + pd.date);

        sql = "INSERT INTO PackageDate \n"
                + "(deviceId,date, time, lat, lon, speed, course, height, sats, hdop, digitinput, digitouput, ads, ibutton, params, input2) \n"
                + "VALUES ('" + pd.id + "','" + Methods.dateToString(pd.date) + "','" + Methods.timeToString(pd.time) + "','" + pd.lat + "','" + pd.lon + "','" + pd.speed + "','" + pd.course + "','" + pd.height + "','" + pd.sats + "','" + pd.hdop + "','" + pd.digitinput + "','" + pd.digitouput + "','" + pd.ads + "','" + pd.ibutton+ "','"+pd.params+ "','"+pd.input2 + "')";
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
>>>>>>> origin/master
