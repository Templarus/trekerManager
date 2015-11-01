package trekermanager;

/**
 *
 * @author EvgenyVartavanyan
 */
//класс с набором методов для общих операций (например переобразования)
public class Methods {

    public static String dateToString(int data) {
        if (data != 0) {
            String day = new Integer(data).toString().substring(0, 2);
            String month = new Integer(data).toString().substring(2, 4);
            String year = new Integer(data).toString().substring(4, 6);
            StringBuilder stringDate = new StringBuilder();
            stringDate.append(year).append(month).append(day);
            return stringDate.toString();
        } else {
            return "";
        }
    }
    public static String timeToString(int time) {
        if (time != 0) {
            String h = new Integer(time).toString().substring(0, 2);
            String m = new Integer(time).toString().substring(2, 4);
            String s = new Integer(time).toString().substring(4, 6);
            StringBuilder stringTime = new StringBuilder();
            stringTime.append(h).append(":").append(m).append(":").append(s);
            return stringTime.toString();
        } else {
            return "";
        }
    }
}
