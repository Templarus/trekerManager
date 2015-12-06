package trekermanager;

import UI.Start;

public class Event {

    private final int eventId;
    private int eventSeverity;
    private String eventText;

    public Event() {
        this(-1, -1, "Nothing to display");
    }

    public Event(int eventId, int eventSeverity) {
        this(eventId, eventSeverity, "Nothing to display");
        this.eventText = Start.mf.getEventText(eventId); //добавить
    }

    public Event(int eventId, int eventSeverity, String eventText) {
        this.eventId = eventId;
        this.eventSeverity = eventSeverity;
        this.eventText = eventText;
    }

    public String init(String val1) {

        return eventText;
    }

    public String init(String val1, String val2) {
        return eventText;

    }

    public String init(String val1, String val2, String val3) {
        return eventText;

    }
}
