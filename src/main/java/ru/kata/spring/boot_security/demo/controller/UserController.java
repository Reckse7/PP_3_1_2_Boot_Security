package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Controller
public class UserController {

    private UserService service;

    @Autowired
    public void setUserService(UserService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String helloPage() {
        return "index";
    }

    @GetMapping("/user")
    public String userPage(ModelMap model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("user", authentication.getPrincipal());
        return "user";
    }

    @GetMapping("/admin")
    public String printUsers(ModelMap model) {
        List<?> list = service.getAllUsers();
        model.addAttribute("users", list);
        return "admin";
    }

    @GetMapping("/admin/edit")
    public String editPage(@RequestParam int id, ModelMap model) {
        User user = service.getById(id);
        model.addAttribute("user", user);
        return "edit";
    }

    @GetMapping("/admin/add")
    public String addPage(ModelMap model) {
        model.addAttribute("user", new User());
        return "add";
    }

    @PostMapping("/admin/add")
    public String addUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
                return "add";
        }
        service.add(user);
        return "redirect:/admin";
    }

    @PostMapping("/admin/edit")
    public String editUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
                return "edit";
        }
        service.edit(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/delete")
    public String deleteUser(@RequestParam int id) {
        service.delete(id);
        return "redirect:/admin";
    }
}
