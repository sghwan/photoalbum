package com.squarecross.photoalbum.ssr.controller;

import com.squarecross.photoalbum.Constants;
import com.squarecross.photoalbum.domain.User;
import com.squarecross.photoalbum.dto.LoginDto;
import com.squarecross.photoalbum.dto.RegisterDto;
import com.squarecross.photoalbum.ssr.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
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
    public String login(@ModelAttribute("login") LoginDto loginDto,
                        BindingResult bindingResult,
                        HttpServletRequest request) {

        User loginUser = userService.login(loginDto);

        if (loginUser == null) {
            bindingResult.reject("login", null, "아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        if (bindingResult.hasErrors()) {
            return "users/loginForm";
        }

        HttpSession session = request.getSession();
        session.setAttribute(Constants.LOGIN_USER, loginUser);

        return "redirect:/albums";
    }

    @GetMapping("/register")
    public String registerForm(@ModelAttribute("register") RegisterDto registerDto) {
        return "users/registerForm";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("register") RegisterDto registerDto, BindingResult bindingResult) {
        Pattern pattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
        Matcher matcher = pattern.matcher(registerDto.getEmail());

        if (Objects.equals(registerDto.getName(), "")) {
            bindingResult.rejectValue("name", "register", null, "이름은 필수 입력값입니다.");
        }

        if (!matcher.matches()) {
            bindingResult.rejectValue("email", "register", null, "이메일 형식이 맞지 않습니다.");
        }

        if (Objects.equals(registerDto.getPassword(), "")) {
            bindingResult.rejectValue("password", "register", null, "비밀번호는 필수 입력값입니다.");
        }

        if (registerDto.getPassword().compareTo(registerDto.getPasswordCheck()) != 0) {
            bindingResult.rejectValue("passwordCheck", "register", null, "비밀번호가 일치하지 않습니다.");
        }

        User registeredUser = userService.register(registerDto);

        if (registeredUser == null) {
            bindingResult.reject("register", null, "이미 존재하는 회원입니다.");
        }

        if (bindingResult.hasErrors()) {
            return "users/registerForm";
        }

        return "redirect:/users/login";
    }
}
