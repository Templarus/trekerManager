/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trekermanager;

import UI.Start;

/**
 *
 * @author RusTe
 */
public class PackageData extends Pack {

    String id;
    int date;
    int time;
    String lat = "";
    String lon = "";
    int speed;
    int course;
    int height;
    int sats;
    float hdop;
    int digitinput;
    int digitouput;
    String ads;
    String ibutton;
    String params;
    boolean input1;
    boolean input2;

    public PackageData(String id, int date, int time, String lat, String lon, int speed, int course, int height, int sats, float hdop, int digitinput, int digitoutput, String ads, String ibutton, String params) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.lat = lat;
        this.lon = lon;
        this.speed = speed;
        this.course = course;
        this.sats = sats;
        this.hdop = hdop;
        this.digitinput = digitinput;
        this.digitouput = digitoutput;
        this.ads = ads;
        this.ibutton = ibutton;
        this.params = params;
        String paramsbody[] = params.split(",");
        String In1[] = paramsbody[3].split(":");
        this.input1 = Integer.parseInt(In1[2]) == 1;
        String In2[] = paramsbody[4].split(":");
        this.input2 = Integer.parseInt(In2[2]) == 1;
        System.out.println("Integer.parseInt(In2[2]) ==" + Integer.parseInt(In2[2]));
        SaveToDB();
    }

    @Override
    public void Print() {
        System.out.println("Параметры:" + this.params);
        System.out.println("Состояние устройства:" + this.input1);
    }

    @Override
    public void SaveToDB() {
        System.out.println("PackageData: SaveToDB executed");
        if (this.input2) {
            Start.mf.deviceStatus(this.id, true);
        } else {
             Start.mf.deviceStatus(this.id, false);
        }
        Start.mf.sdb.setPackageData(this);
    }
}
