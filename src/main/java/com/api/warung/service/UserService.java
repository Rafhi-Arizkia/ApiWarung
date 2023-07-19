package com.api.warung.service;

import com.api.warung.auth.JwtService;
import com.api.warung.dto.AuthToken;
import com.api.warung.dto.LoginDto;
import com.api.warung.dto.RegisterDto;
import com.api.warung.model.entitiy.RolesEntities;
import com.api.warung.model.entitiy.UserEntities;
import com.api.warung.model.repo.RolesRepo;
import com.api.warung.model.repo.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
public class UserService implements UserDetailsService {
    private final UserRepo userRepo;
    private final RolesRepo rolesRepo;
    @Autowired
    private JwtService jwtService;
    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;
    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;
    @Autowired
    public UserService(UserRepo userRepo, RolesRepo rolesRepo) {
        this.userRepo = userRepo;
        this.rolesRepo = rolesRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String UserEmail) throws UsernameNotFoundException {
        Optional<UserEntities> userOptional = userRepo.findByUserEmail(UserEmail);
        UserEntities user = userOptional.orElseThrow(() -> new UsernameNotFoundException("Invalid Email or password"));

        List<GrantedAuthority> authorities = getAuthorities(user.getRoles());
        return new User(user.getUserEmail(), user.getPassword(), authorities);
    }

    private List<GrantedAuthority> getAuthorities(List<RolesEntities> roles){
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (RolesEntities role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getRolesName()));
        }
        return authorities;
    }

    private void checkUserRoleExist(){
        RolesEntities roles = new RolesEntities();
        roles.setRolesName("ROLE_USER");
        rolesRepo.save(roles);
    }

    public AuthToken saveUser(RegisterDto registerDto){
        RolesEntities roles = rolesRepo.findByRolesName("ROLE_USER");
        if (roles == null){
            checkUserRoleExist();
        }
        var userEntities = UserEntities.builder()
                .userName(registerDto.getUserName())
                .userEmail(registerDto.getUserEmail())
                .userAddress(registerDto.getUserAddress())
                .numberPhone(registerDto.getNumberPhone())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .roles(Collections.singletonList(roles))
                .build();
        userRepo.save(userEntities);
        var userOptional = userRepo.findByUserEmail(registerDto.getUserEmail());
        if (userOptional.isPresent()){
            var user= userOptional.get();
            var jwtToken = jwtService.generateToken(new HashMap<>(),user);
            return  AuthToken.builder()
                    .token(jwtToken)
                    .build();
        }else {
            throw new NoSuchElementException("User with email " + registerDto.getUserEmail() + "not found");
        }
    }

    public AuthToken loginValidate(LoginDto loginDto){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getEmail(),
                loginDto.getPassword()
        ));
        var userOptional = userRepo.findByUserEmail(loginDto.getEmail());
        if (userOptional.isPresent()){
            var user = userOptional.get();
            var jwtToken = jwtService.generateToken(new HashMap<>(),user);
            return AuthToken.builder()
                    .token(jwtToken)
                    .build();
        }else {
            throw new NoSuchElementException("User with email " + loginDto.getEmail() + "not found");
        }
    }
}
