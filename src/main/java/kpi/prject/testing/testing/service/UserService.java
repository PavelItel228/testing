package kpi.prject.testing.testing.service;

import kpi.prject.testing.testing.dto.UserDTO;
import kpi.prject.testing.testing.entity.User;
import kpi.prject.testing.testing.entity.enums.Role;
import kpi.prject.testing.testing.entity.enums.Status;
import kpi.prject.testing.testing.exceptions.InvalidUserException;
import kpi.prject.testing.testing.exceptions.UserExistsException;
import kpi.prject.testing.testing.repository.UserRepository;
import kpi.prject.testing.testing.validators.NewUserValidator;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService implements UserDetailsService {

    public final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, @Lazy BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepository.findByEmail(s).orElseThrow(() -> new UsernameNotFoundException(String.format("User with email %s not found", s)));
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public void registration (User user, BindingResult result) throws InvalidUserException, UserExistsException {
        new NewUserValidator().validate(user, result);
        if(result.hasErrors()) {
            log.warn("POST registration: not valid data");
            throw new InvalidUserException("dont validate");
        }
        user.setStatus(Status.Active);
        user.setRole(Role.ROLE_USER);
        user.setCreated(LocalDate.now());
        user.setUpdated(LocalDate.now());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            userRepository.save(user);
        }
        catch (DataIntegrityViolationException e){
            log.warn("POST registration: uniq fields duplicate");
            result.rejectValue("username", "3", "login or email is already taken");
            result.rejectValue("email", "3", "login or email is already taken");
            throw new UserExistsException(user.getUsername());
        }

    }

    public User getFromDto(UserDTO user) {
        return User.builder().email(user.getEmail()).username(user.getUsername()).confirmPassword(user.getConfirmPassword())
                .password(user.getPassword()).build();
    }

    public Optional<User> getByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
