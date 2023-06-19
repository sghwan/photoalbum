package com.squarecross.photoalbum.ssr.service;

import com.squarecross.photoalbum.domain.User;
import com.squarecross.photoalbum.dto.EmailDto;
import com.squarecross.photoalbum.dto.LoginDto;
import com.squarecross.photoalbum.dto.RegisterDto;
import com.squarecross.photoalbum.repository.UserRepository;
import com.squarecross.photoalbum.util.Encrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(LoginDto loginDto) {
        User user = findUser(loginDto.getEmail());

        if (user == null) {
            throw new EntityNotFoundException("존재 하지 않는 회원입니다.");
        }

        if (!isMatch(loginDto.getPassword(), user.getPassword(), user.getSalt())) {
            return null;
        }

        return user;
    }

    private boolean isMatch(String plain, String password, String salt) {
        Encrypt encrypt = new Encrypt();
        String encryptedPlain = encrypt.getEncrypt(plain, salt);

        return encryptedPlain.compareToIgnoreCase(password) == 0;
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Transactional
    public User register(RegisterDto registerDto) {
        Encrypt encrypt = new Encrypt();
        String salt = encrypt.genSalt();

        User foundUser = userRepository.findByEmail(registerDto.getEmail())
                .orElse(null);

        if (foundUser != null) {
            return null;
        }

        User user = new User();
        user.setName(registerDto.getName());
        user.setEmail(registerDto.getEmail());
        user.setSalt(salt);
        user.setPassword(encrypt.getEncrypt(registerDto.getPassword(), salt));

        return userRepository.save(user);
    }
}
