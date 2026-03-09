package eiu.edu.vn.EquipmentBookingSystem.controller;

import eiu.edu.vn.EquipmentBookingSystem.model.Account;
import eiu.edu.vn.EquipmentBookingSystem.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private BookingService bookingService;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Account user = (Account) session.getAttribute("user");
        if (user == null || !user.getRole().equals(Account.Role.ADMIN)) {
            return "redirect:/auth/login";
        }

        // Thống kê
        long totalEquipment = equipmentService.getAllEquipment().size();
        long totalCategories = categoryService.getAllCategories().size();
        long totalAccounts = accountService.getAllAccounts().size();
        long totalBookings = bookingService.getAllBookings().size();

        model.addAttribute("totalEquipment", totalEquipment);
        model.addAttribute("totalCategories", totalCategories);
        model.addAttribute("totalAccounts", totalAccounts);
        model.addAttribute("totalBookings", totalBookings);
        model.addAttribute("recentBookings", bookingService.getAllBookings().stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .limit(10)
                .toList());

        return "admin/dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/";
    }
}
