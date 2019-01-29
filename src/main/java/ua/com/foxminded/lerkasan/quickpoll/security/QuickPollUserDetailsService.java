package ua.com.foxminded.lerkasan.quickpoll.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.com.foxminded.lerkasan.quickpoll.domain.User;
import ua.com.foxminded.lerkasan.quickpoll.repository.UserRepository;

@Service
public class QuickPollUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    private static final String USERNAME_NOT_FOUND = "User with the username %s doesn't exist";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format(USERNAME_NOT_FOUND, username));
        }
        return user;
    }
}
