package com.squarecross.photoalbum.ssr.controller;

import com.squarecross.photoalbum.domain.User;
import com.squarecross.photoalbum.dto.LoginDto;
import com.squarecross.photoalbum.dto.RegisterDto;
import com.squarecross.photoalbum.ssr.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("login") LoginDto loginDto) {
        return "users/loginForm";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("login") LoginDto loginDto, BindingResult bindingResult, HttpServletRequest request) {
        Pattern pattern = Pattern.compile("/^[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}$/i");
        Matcher matcher = pattern.matcher(loginDto.getEmail());

        if (!matcher.matches()) {
            bindingResult.rejectValue("email", "login", null, "이메일 형식이 맞지 않습니다.");
        }

        User loginUser = userService.login(loginDto);

        if (loginUser == null) {
            bindingResult.reject("login", null, "아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        if (bindingResult.hasErrors()) {
            return "users/loginForm";
        }

        HttpSession session = request.getSession();
        session.setAttribute("loginMember", loginUser);

        return "redirect:/albums";
    }

    @GetMapping("/register")
    public String registerForm(@ModelAttribute("register") RegisterDto registerDto) {
        return "users/registerForm";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("register") RegisterDto registerDto, BindingResult bindingResult) {
        Pattern pattern = Pattern.compile("/^[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}$/i");
        Matcher matcher = pattern.matcher(registerDto.getEmail());

        if (!matcher.matches()) {
            bindingResult.rejectValue("email", "register", null, "이메일 형식이 맞지 않습니다.");
        }

        if (registerDto.getPassword().compareTo(registerDto.getPasswordCheck()) != 0) {
            bindingResult.rejectValue("passwordCheck", "register", null, "비밀번호가 일치하지 않습니다.");
        }

        if (bindingResult.hasErrors()) {
            return "users/registerForm";
        }

        userService.register(registerDto);

        return "redirect:/users/login";
    }
}
