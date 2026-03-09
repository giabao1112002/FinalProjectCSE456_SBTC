package eiu.edu.vn.EquipmentBookingSystem.config;

import eiu.edu.vn.EquipmentBookingSystem.model.Account;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) 
            throws Exception {
        
        String requestURI = request.getRequestURI();
        
        // Chỉ kiểm tra đường dẫn /admin/*
        if (requestURI.startsWith("/admin") && !requestURI.equals("/admin/logout")) {
            HttpSession session = request.getSession(false);
            
            if (session == null) {
                response.sendRedirect("/auth/login");
                return false;
            }
            
            Account user = (Account) session.getAttribute("user");
            
            if (user == null || !user.getRole().equals(Account.Role.ADMIN)) {
                response.sendRedirect("/");
                return false;
            }
        }
        
        return true;
    }
}
