package com.modernfamily.ukids.domain.user.model.service;

import com.modernfamily.ukids.domain.user.dto.*;
import com.modernfamily.ukids.domain.user.entity.Role;
import com.modernfamily.ukids.domain.user.entity.User;
import com.modernfamily.ukids.domain.user.mapper.UserMapper;
import com.modernfamily.ukids.domain.user.model.repository.UserRepository;
import com.modernfamily.ukids.global.exception.CustomException;
import com.modernfamily.ukids.global.exception.ExceptionResponse;
import com.modernfamily.ukids.global.jwt.JWTUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServIceImpl implements UserService{

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    @PersistenceContext
    private EntityManager em;

    @Override
    public String login(UserLoginDto userLoginDto) {
        User user = userRepository.findById(userLoginDto.getId())
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));
        if(user.isDelete())
            throw new ExceptionResponse(CustomException.DELETE_USER_EXCEPTION);
        if(!bCryptPasswordEncoder.matches(userLoginDto.getPassword(), user.getPassword())) {
            throw new ExceptionResponse(CustomException.PASSWORD_INPUT_EXCEPTION);
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userLoginDto.getId(), userLoginDto.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String token = jwtUtil.CreateJwt(customUserDetails, 60 * 60 * 1000L);

        return "Bearer " + token;
    }

    @Transactional
    @Override
    public String updateUser(UserUpdateDto userUpdateDto) {
        userRepository.findById(userUpdateDto.getId())
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));
        String id = CustomUserDetails.contextGetUserId();
        if(!id.equals(userUpdateDto.getId())) {
            throw new ExceptionResponse(CustomException.NOT_SAME_USER_EXCEPTION);
        }
        String password = userUpdateDto.getPassword();
        userUpdateDto.setPassword(bCryptPasswordEncoder.encode(userUpdateDto.getPassword()));
        userRepository.userUpdate(userMapper.toUserUpdateEntity(userUpdateDto));

        em.flush();
        em.clear();

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userUpdateDto.getId(), password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String token = jwtUtil.CreateJwt(customUserDetails, 60 * 60 * 1000L);

        return "Bearer " + token;
    }

    @Override
    public void signUp(SignUpDto signUpDto) {
        String id = signUpDto.getId();
        if(signUpDto.getEmail().isEmpty()) signUpDto.setEmail(null);
        if(signUpDto.getPhone().isEmpty()) signUpDto.setPhone(null);

        if(userRepository.existsById(id)){
            throw new ExceptionResponse(CustomException.DUPLICATED_ID_EXCEPTION);
        }
        if(signUpDto.getEmail() != null && userRepository.existsByEmail(signUpDto.getEmail())){
            throw new ExceptionResponse(CustomException.DUPLICATED_EMAIL_EXCEPTION);
        }

        if(signUpDto.getPhone() != null && userRepository.existsByPhone(signUpDto.getPhone())){
            throw new ExceptionResponse(CustomException.DUPLICATED_NUMBER_EXCEPTION);
        }


        signUpDto.setPassword(bCryptPasswordEncoder.encode(signUpDto.getPassword()));
        signUpDto.setRole(Role.ROLE_USER);
        User user = userMapper.toSignUpEntity(signUpDto);

        userRepository.save(user);

    }

    @Override
    public boolean idExist(String id) {
        return userRepository.existsById(id);
    }

    @Override
    public boolean emailExist(String email) {
        if(email.isEmpty()) return false;

        String id = CustomUserDetails.contextGetUserId();
        if(id != null && !id.equals("anonymousUser")){
            User user = userRepository.findById(id).orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));
            if(email.equals(user.getEmail())) return false;
        }

        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean phoneExist(String phone){
        if(phone.isEmpty()) return false;

        String id = CustomUserDetails.contextGetUserId();
        if(id != null && !id.equals("anonymousUser")){
            User user = userRepository.findById(id).orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));
            if(phone.equals(user.getPhone())) return false;
        }

        return userRepository.existsByPhone(phone);
    }

    @Override
    public UserDto getUser(Long userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));

        if(user == null){
            throw new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION);
        }

        return userMapper.toUserDto(user);
    }

    @Override
    public boolean pwCheck(PasswordCheckDto userDto) {
        String id = userDto.getId();

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));

        return bCryptPasswordEncoder.matches(userDto.getPassword(), user.getPassword());
    }

    // 다른 domain에서 사용
    @Override
    public UserOtherDto findByIdOther(String id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));

        return userMapper.toUserOtherDto(user);
    }
}
