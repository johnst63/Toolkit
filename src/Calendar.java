import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Sheolfire on 8/22/2016.
 */
public class Calendar {
    private ArrayList<CalendarEvent> events;

    public Calendar() {
        events = new ArrayList<>();
    }

    public void addEvent(String name, String desc, Date date) {
        CalendarEvent event = new CalendarEvent(name, desc, date);

        int i;
        boolean placed = false;
        for(i = 0; i < events.size(); i++) {
            if(date.before(events.get(i).getDate())) {
                events.add(i, event);
                placed = true;
                break;
            }
        }
        if(!placed) {
            events.add(event);
        }

    }

    /**
     * Returns the first index of the date
     * @param date
     * @return
     */
    /*
    public int getIndexByDate(Date date) {
        int low = 0;
        int high = events.size() - 1;
        while(low <= high) {
            int mid = low + (high - low) / 2;
            Date temp = events.get(mid).getDate();
            if(temp.before(date)) {
                low = mid + 1;
            } else if(temp.after(date)) {
                high = mid - 1;
            } else {
                return mid;
            }

        }
        return -1;
    }
    */

    /**
     * Removes an event from the events arraylist based on the its name and date
     * @param name the name of the CalendarEvent to remove
     * @param date the date of the CalendarEvent to remove
     * @return 1 if the item is successfully removed, -1 if not
     */
    public int removeEvent(String name, Date date) {
        int i = 0;
        while(i < events.size()) {
            if(date.equals(events.get(i).getDate()) && name.equals(events.get(i).getName())) {
                events.remove(i);
                return 1;
            }
            i+= 1;
        }
        return -1;
    }

    /**
     * Determines if an event from the events arraylist is removable based on the its name and date
     * @param name the name of the CalendarEvent to remove
     * @param date the date of the CalendarEvent to remove
     * @return 1 if the item is removable/exists in the list, -1 if not
     */
    public int isRemovable(String name, Date date) {
        int i = 0;
        while(i < events.size()) {
            if(date.equals(events.get(i).getDate()) && name.equals(events.get(i).getName())) {
                return 1;
            }
            i+= 1;
        }
        return -1;
    }

//    public void removeEvent(String name) {
//
//    }

    /**
     * Remove an event from events based on the index (starting from 1 not 0)
     * @param index the index of the event in the events arraylist
     * @return 1 if the item is removed, -1 if it fails
     */
    public int removeEvent(int index) {
        index-= 1;
        if(index < events.size() && index >= 0) {
            events.remove(index);
            return 1;
        }
        return -1;
    }

    /**
     * Determines if an event from events is removable based on the index (starting from 1 not 0)
     * @param index the index of the event in the events arraylist
     * @return 1 if the item can be removed, -1 if it cannot be removed/does not exist
     */
    public int isRemovable(int index) {
        index-= 1;
        if(index < events.size() && index >= 0) {
            return 1;
        }
        return -1;
    }

    public void clear() {
        events = new ArrayList<>();
    }

    /**
     * Prints out the calendar
     */
    public void print() {
        System.out.println("[-Calendar of Events-]");
//        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        int index = 1;
        for(CalendarEvent event: events) {
            System.out.println(index + ": " + event.getName() + " - " + event.getDesc() + " - " + Toolkit.date_format.format(event.getDate()));
            //System.out.println(index + ": " + event.getName() + " - " + event.getDesc() + " - " + df.format(event.getDate()));
            index += 1;
        }
        System.out.println("----------------------");
    }
}
