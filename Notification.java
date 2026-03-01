import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Notification{
    private final String title;
    private final String message;
    private final String time;
    private boolean isRead;

    public Notification(String title, String message){
        this.title = title;
        this.message = message;
        this.isRead = false;
        String pattern = "dd/MM/yyyy HH:mm:ss";
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        this.time = now.format(formatter);
    }

    public String getTitle(){
        return this.title;
    }

    public String getMessage(){
        return this.message;
    }

    public String getTime(){
        return this.time;
    }

    public boolean getIsRead(){
        return this.isRead;
    }

    public void setIsRead(boolean b){
        this.isRead = b;
    }
}