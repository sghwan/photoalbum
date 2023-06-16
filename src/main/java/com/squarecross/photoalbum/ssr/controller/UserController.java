package com.squarecross.photoalbum.ssr.controller;

import com.squarecross.photoalbum.domain.User;
import com.squarecross.photoalbum.dto.LoginDto;
import com.squarecross.photoalbum.ssr.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    public String loginForm(@ModelAttribute("login") LoginDto loginDto, BindingResult bindingResult, HttpServletRequest request) {
        Pattern pattern = Pattern.compile("/^[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}$/i");
        Matcher matcher = pattern.matcher(loginDto.getEmail());

        if (!matcher.matches()) {
            bindingResult.rejectValue("email", "login", null, "이메일 형식이 맞지 않습니다.");
        }

        User loginUser = userService.login(loginDto);

        if (loginUser == null) {
            bindingResult.rejectValue("password", "login", null, "아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        if (bindingResult.hasErrors()) {
            return "users/loginForm";
        }

        HttpSession session = request.getSession();
        session.setAttribute("loginMember", loginUser);

        //session에 login한 유저 정보를 담았고
        //자동적으로 cookie에 생성된 특정 유저의 sessionId를 담고 있다
        //그렇다면 controller를 대대적으로 손으로 봐야한다. 필터나 인터셉터를 적용하여
        //session이 없다면 로그인 화면으로 강제이동하고
        //session이 있다면 가져온 session에 저장된 유저 정보를 토대로 유저와 앨범을 조인하여 앨범을 가져와야 한다.
        //앨범 테이블에 user fk를 추가 해줘야 한다.
        //테스트 케이스 작성도 필요하다.

        return "redirect:/albums";
    }
}
