package EventHandler;

import UI.Start;

public class Event {

    private final int eventId=0;
    private int eventSeverity;
    private String eventText;
    private int eventType;
    private int deviceId;

    public Event(int eventSeverity, int eventType, int deviceId) {
        this.eventSeverity = eventSeverity;
        this.eventType = eventType;
        this.deviceId = deviceId;
    }

    public Event(int eventSeverity, int eventType) {
        this.eventSeverity = eventSeverity;
        this.eventType = eventType;
    }

    public Event() {
    }
    

  
//    public String init(String val1) {
//
//        return eventText;
//    }
//
//    public String init(String val1, String val2) {
//        return eventText;
//
//    }
//
//    public String init(String val1, String val2, String val3) {
//        return eventText;
//
//    }

    public int getEventId() {
        return eventId;
    }

    public int getEventSeverity() {
        return eventSeverity;
    }

    public String getEventText() {
        return eventText;
    }
    
}
