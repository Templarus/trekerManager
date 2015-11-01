package trekermanager;
import UI.Start;

// класс расширяющий абстрактный Pack до пакета с данными. Сделано на всякий случай - если будут ещё другие пакеты и их тоже нужно будет обрабатывать аналогичными способами
// так же, при инициализации через конструктор, происходит запись пакета в базу данных при помощи переопределённого метода
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
// input2 - boolean отображение дискретного сигнала(0,1) с устройства.
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
