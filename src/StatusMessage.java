/**
 * Created by Sheolfire on 8/23/2016.
 */
public class StatusMessage {
    private int status_code;
    private String status_message;
    private boolean passed;

    public StatusMessage(int status_code, String status_message) {
        this.status_code = status_code;
        this.status_message = status_message;
    }

    public StatusMessage(int status_code) {
        this.status_code = status_code;
        this.status_message = "Default Success";
    }

    public int getStatusCode() {
        return status_code;
    }

    public void setStatusCode(int status_code) {
        this.status_code = status_code;
    }

    public String getStatusMessage() {
        return status_message;
    }

    public void setStatusMessage(String status_message) {
        this.status_message = status_message;
    }
}
