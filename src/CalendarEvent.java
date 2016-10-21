import java.util.Date;

/**
 * Contains the date, name, and description of a calendar event
 * Created by Sheolfire on 8/22/2016.
 */
public class CalendarEvent {
    private String name;
    private String desc;
    private Date date;

    public CalendarEvent(String name, String desc, Date date) {
        this.name = name;
        this.desc = desc;
        this.date = date;
    }

    public CalendarEvent(String name, Date date) {
        this.name = name;
        this.desc = "None";
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public Date getDate() {
        return date;
    }

    public void replaceName(String name) {
        this.name = name;
    }

    public void replaceDesc(String desc) {
        this.desc = desc;
    }

    public void replaceDate(Date date) {
        this.date = date;
    }
}