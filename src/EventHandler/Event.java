package EventHandler;

import UI.Start;

public class Event {

    private final int eventId;
    private int eventSeverity; // важность события
    private String eventText; // собственно текст который отображается пользователю
    private int eventType; // тип - устройство\служба\косяк в транспорте 1С и тд.
    private int incId; // если >=0 - это устройство и тут лежит deviceID, если меньше = это другой источник(и может быть простая заглушка в виде -1)

    /**
     * для устройств
     *
     * @param eventId
     * @param eventSeverity
     * @param eventType
     * @param incId
     */
    public Event(int eventId, int eventSeverity, int eventType, int incId) {
        this.eventId = eventId;
        this.eventSeverity = eventSeverity;
        this.eventType = eventType;
        this.incId = incId;
    }

    /**
     * для всех НЕ устройст
     *
     * @param eventId
     * @param eventSeverity
     * @param eventType
     */
    public Event(int eventId, int eventSeverity, int eventType) {
        this.eventId = eventId;
        this.eventSeverity = eventSeverity;
        this.eventType = eventType;
    }

    public Event() {
        this.eventId = -1;
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
