package com.squarecross.photoalbum.ssr.service;

import com.squarecross.photoalbum.domain.User;
import com.squarecross.photoalbum.dto.LoginDto;
import com.squarecross.photoalbum.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Test
    void login() {
        //given
        User user = new User();
        user.setName("seo");
        user.setEmail("anan123456@naver.com");
        user.setPassword("CE370EFA9D66BD305E9DC65A472F6CC77D2266DD7DE93782BD2D63D307C82DBB");
        user.setSalt("abcde");
        userRepository.save(user);

        //when
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("anan123456@naver.com");
        loginDto.setPassword("1234");
        User loginUser = userService.login(loginDto);

        //then
        assertThat(loginUser).isEqualTo(user);
    }
}
