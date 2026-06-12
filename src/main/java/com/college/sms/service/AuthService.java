package com.college.sms.service;

import com.college.sms.dto.request.LoginRequest;
import com.college.sms.dto.request.RegisterRequest;
import com.college.sms.dto.response.JwtResponse;
import com.college.sms.entity.Role;
import com.college.sms.entity.Role.RoleName;
import com.college.sms.entity.User;
import com.college.sms.exception.BadRequestException;
import com.college.sms.repository.RoleRepository;
import com.college.sms.repository.UserRepository;
import com.college.sms.security.JwtUtils;
import com.college.sms.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final ActivityLogService activityLogService;

    public JwtResponse login(LoginRequest request, String ip) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);

        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        String token = jwtUtils.generateToken(userDetails);

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
        activityLogService.log(user, "LOGIN", "user", user.getId(), "User logged in", ip);

        return JwtResponse.builder()
                .token(token)
                .type("Bearer")
                .id(userDetails.getId())
                .email(userDetails.getEmail())
                .roles(userDetails.getAuthorities().stream()
                        .map(a -> a.getAuthority()).collect(Collectors.toList()))
                .build();
    }

    @Transactional
    public User register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already registered");
        }
        RoleName roleName = parseRole(request.getRole());
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new BadRequestException("Invalid role"));

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .roles(Set.of(role))
                .enabled(true)
                .build();
        return userRepository.save(user);
    }

    private RoleName parseRole(String role) {
        return switch (role.toUpperCase()) {
            case "ADMIN" -> RoleName.ROLE_ADMIN;
            case "TEACHER" -> RoleName.ROLE_TEACHER;
            case "STUDENT" -> RoleName.ROLE_STUDENT;
            default -> throw new BadRequestException("Role must be ADMIN, TEACHER, or STUDENT");
        };
    }
}
