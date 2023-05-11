package ru.rest_demo.exercise_2315.services;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.rest_demo.exercise_2315.exception_handlers.UserNotCreatedException;
import ru.rest_demo.exercise_2315.exception_handlers.UserNotFoundException;
import ru.rest_demo.exercise_2315.exception_handlers.UserNotUpdatedException;
import ru.rest_demo.exercise_2315.models.User;
import ru.rest_demo.exercise_2315.repositories.UserRepository;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService{
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImp(UserRepository userRepository, @Lazy BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("No user found with such id"));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEMail(email)
                .orElseThrow(() -> new UserNotFoundException("No user found with such e-mail"));
    }

    @Override
    public List<User> allUsers() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public void saveUser(User user) {
        Optional<User> userFromDB = userRepository.findByEMail(user.getEmail());
        if (userFromDB.isPresent()) {
            throw new UserNotCreatedException("User with such e-mail already exists");
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void updateUser(User user) {
        Optional<User> userFromDB = userRepository.findByEMail(user.getEmail());
        if (userFromDB.isPresent() && userFromDB.get().getId() != user.getId()) {
            throw new UserNotUpdatedException("E-mail already in use");
        }
        if (user.getPassword().isEmpty()) {
            userFromDB = userRepository.findById(user.getId());
            userFromDB.ifPresent(value -> user.setPassword(value.getPassword()));
        } else {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        userRepository.save(user);
    }


    @Override
    @Transactional
    public void deleteUserById(Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
        } else {
            throw new UserNotFoundException("No user found with such id");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEMail(email);
        if (user.isEmpty()) {
            throw new UserNotFoundException("No user found with such e-mail");
        }
        return user.get();
    }
}