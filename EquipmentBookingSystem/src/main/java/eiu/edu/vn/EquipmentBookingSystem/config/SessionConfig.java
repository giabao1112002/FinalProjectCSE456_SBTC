package eiu.edu.vn.EquipmentBookingSystem.config;

import jakarta.servlet.http.HttpSessionListener;
import jakarta.servlet.http.HttpSessionEvent;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SessionConfig implements HttpSessionListener {
    
    @Override
    public void sessionCreated(HttpSessionEvent event) {
        // Thiết lập timeout session (30 phút)
        event.getSession().setMaxInactiveInterval(30 * 60);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        // Xóa session khi logout
        event.getSession().invalidate();
    }
}
