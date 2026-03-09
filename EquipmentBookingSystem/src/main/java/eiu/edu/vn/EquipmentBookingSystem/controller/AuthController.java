package eiu.edu.vn.EquipmentBookingSystem.controller;

import eiu.edu.vn.EquipmentBookingSystem.model.Account;
import eiu.edu.vn.EquipmentBookingSystem.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AccountService accountService;

    @GetMapping("/login")
    public String showLoginForm() {
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, 
                       HttpSession session, RedirectAttributes redirectAttributes) {
        var account = accountService.login(username, password);
        if (account.isPresent()) {
            session.setAttribute("user", account.get());
            if (account.get().getRole().equals(Account.Role.ADMIN)) {
                return "redirect:/admin/dashboard";
            }
            return "redirect:/";
        }
        redirectAttributes.addFlashAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng");
        return "redirect:/auth/login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("account", new Account());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute Account account, BindingResult result) {
        if (result.hasErrors()) {
            return "auth/register";
        }

        try {
            accountService.registerAccount(account);
            return "redirect:/auth/login?success";
        } catch (IllegalArgumentException e) {
            result.rejectValue("username", "error.account", e.getMessage());
            return "auth/register";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
