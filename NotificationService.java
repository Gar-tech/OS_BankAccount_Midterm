public interface NotificationService {
    Notification getNotification();
    void sendNotification();
    void pushNotification();
    void pushNotification(String msg, String title);
    void markAsRead(Notification n);
    void clearAllNotification();
}