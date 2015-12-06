package EventHandler;

import java.util.LinkedList;

public class EventLogger {

    public LinkedList<Event> eventLog;

    public EventLogger() {

    }

    /**
     *
     * @param event
     * @return
     */
    private boolean addEvent(Event event) {
        if (!eventLog.contains(event)) {
            return eventLog.add(event);
        }
        return false;
    }

    /**
     *
     * @param eventID
     * @return
     */
    private boolean removeEvent(int eventID) {
        for (Event ev : eventLog) {
            if (ev.getEventId() == eventID) {
                return eventLog.remove(ev);
            }
        }
        return false;
    }

    public LinkedList<Event> getEventLog() {
        return eventLog;
    }

    public Event createEvent(int severity,int type,int incId)
    {
        Event event;
        int num=eventLog.size();
        if(incId>=0) //для устройства - нужно записать в deviceId евента = его id
        {event=new Event(num,type,incId);}
        if(incId<0) // для всех остальных источников событий = -1 и конструктор другой
        {event=new Event(num,type);}
        
        
        return event=new Event();
    }
}
