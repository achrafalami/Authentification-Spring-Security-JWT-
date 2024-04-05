package com.example.SpringAuth.Controller;


import com.example.SpringAuth.DTO.AuthResponseDto;
import com.example.SpringAuth.DTO.LoginDto;
import com.example.SpringAuth.DTO.RegisterDto;
import com.example.SpringAuth.Model.RoleEntity;
import com.example.SpringAuth.Model.UserEntity;
import com.example.SpringAuth.Repository.RoleRepository;
import com.example.SpringAuth.Repository.UserRepository;
import com.example.SpringAuth.Validateur.EmailValidator;
import com.example.SpringAuth.security.JWTGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JWTGenerator jwtGenerator;
    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, PasswordEncoder passwordEncoder,JWTGenerator jwtGenerator) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
    }
    @GetMapping("/All")
    public List <UserEntity> findAll(){
        return userRepository.findAll();
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginDto loginDto){
        if(!EmailValidator.isValidEmail(loginDto.getUsername())){
            return new ResponseEntity<>("Email not valid",HttpStatus.BAD_REQUEST);
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(),
                        loginDto.getPassword()));
        List<String> roles = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        if(roles.contains("EMPLOYE") || roles.contains("ADMIN") ){
            if(EmailValidator.isValidEmployeeSorecEmail(loginDto.getUsername())){
                return new ResponseEntity<>(new AuthResponseDto(token), HttpStatus.OK);
            }

        } else if (roles.contains("ADMIN_PRESTATAIRE") || roles.contains("PRESTATAIRE")) {
            if(EmailValidator.isValidEmployePrestataireEmail(loginDto.getUsername())){
                return new ResponseEntity<>(new AuthResponseDto(token), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("UNAUTHORIZED USER", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
        List<String> roles = registerDto.getRoles().stream().map(RoleEntity::getName).toList();
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            return new ResponseEntity<>("Username is taken!", HttpStatus.BAD_REQUEST);
        }
        if(!EmailValidator.isValidEmail(registerDto.getUsername())){
            return new ResponseEntity<>("Email not valid ??",HttpStatus.BAD_REQUEST);
        }
        if(roles.contains("EMPLOYE") || roles.contains("ADMIN")){
            if(EmailValidator.isValidEmployeeSorecEmail(registerDto.getUsername())){
                UserEntity user = new UserEntity();
                user.setUsername(registerDto.getUsername());
                user.setPassword(passwordEncoder.encode((registerDto.getPassword())));
                user.setRoles(registerDto.getRoles());

                userRepository.save(user);

                return new ResponseEntity<>("User registered success!", HttpStatus.OK);
            }

        } else if (roles.contains("ADMIN_PRESTATAIRE") || roles.contains("PRESTATAIRE")) {
            if(EmailValidator.isValidEmployePrestataireEmail(registerDto.getUsername())){
                UserEntity user = new UserEntity();
                user.setUsername(registerDto.getUsername());
                user.setPassword(passwordEncoder.encode((registerDto.getPassword())));
                user.setRoles(registerDto.getRoles());

                userRepository.save(user);

                return new ResponseEntity<>("User registered success!", HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Email or password not valid ",HttpStatus.UNAUTHORIZED);
    }


}
