package eiu.edu.vn.EquipmentBookingSystem.controller;

import eiu.edu.vn.EquipmentBookingSystem.model.Account;
import eiu.edu.vn.EquipmentBookingSystem.model.Booking;
import eiu.edu.vn.EquipmentBookingSystem.model.Equipment;
import eiu.edu.vn.EquipmentBookingSystem.service.BookingService;
import eiu.edu.vn.EquipmentBookingSystem.service.EquipmentService;
import eiu.edu.vn.EquipmentBookingSystem.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/my-bookings")
    public String myBookings(HttpSession session, Model model) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }

        model.addAttribute("bookings", bookingService.getBookingsByAccount(user));
        return "bookings/my-bookings";
    }

    @GetMapping("/borrow/{equipmentId}")
    public String showBorrowForm(@PathVariable Long equipmentId, HttpSession session, Model model) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }

        Equipment equipment = equipmentService.getEquipmentById(equipmentId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy thiết bị"));

        model.addAttribute("equipment", equipment);
        model.addAttribute("booking", new Booking());
        return "bookings/borrow-form";
    }

    @PostMapping("/borrow")
    public String borrowEquipment(@ModelAttribute Booking booking, HttpSession session, 
                                 RedirectAttributes redirectAttributes) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }

        try {
            booking.setAccount(user);
            bookingService.createBooking(booking);
            redirectAttributes.addFlashAttribute("success", "Mượn thiết bị thành công");
            return "redirect:/bookings/my-bookings";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/";
        }
    }

    @GetMapping("/return/{bookingId}")
    public String returnEquipment(@PathVariable Long bookingId, RedirectAttributes redirectAttributes) {
        try {
            bookingService.returnBooking(bookingId);
            redirectAttributes.addFlashAttribute("success", "Trả thiết bị thành công");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/bookings/my-bookings";
    }

    @GetMapping("/admin/all-bookings")
    public String allBookings(Model model) {
        model.addAttribute("bookings", bookingService.getAllBookings());
        return "admin/bookings/list";
    }
}
