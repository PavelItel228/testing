package kpi.prject.testing.testing.service;

import kpi.prject.testing.testing.entity.User;
import kpi.prject.testing.testing.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    public final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User loadUserByUsername(String s) throws UsernameNotFoundException {
        System.out.println(userRepository.findByUsername(s).orElse(null));
        return userRepository.findByUsername(s).orElse(null);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }
}
