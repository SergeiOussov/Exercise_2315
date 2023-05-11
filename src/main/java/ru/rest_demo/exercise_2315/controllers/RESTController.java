package ru.rest_demo.exercise_2315.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rest_demo.exercise_2315.dto.UserDTO;
import ru.rest_demo.exercise_2315.models.Role;
import ru.rest_demo.exercise_2315.models.User;
import ru.rest_demo.exercise_2315.services.RoleService;
import ru.rest_demo.exercise_2315.services.UserService;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;



@RestController
@RequestMapping("/api")
public class RESTController {

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final RoleService roleService;

    public RESTController(UserService userService,
                          ModelMapper modelMapper,
                          RoleService roleService) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.roleService = roleService;
    }

    private UserDTO convertUserToDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    private User convertUserDTOToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    @GetMapping(value = {"/users/current"})
    public ResponseEntity<UserDTO> getCurrentUser(Principal principal) {
        return new ResponseEntity<>(convertUserToDTO(userService.findByEmail(principal.getName())), HttpStatus.OK);
    }

    @GetMapping(value = {"/users"})
    public ResponseEntity<List<UserDTO>> getUserList() {
        return new ResponseEntity<>(userService.allUsers().stream().map(this::convertUserToDTO)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping(value = {"/users/{id}"})
    public ResponseEntity<UserDTO> getUser(@PathVariable("id") Long id) {
        return new ResponseEntity<>(convertUserToDTO(userService.findUserById(id)), HttpStatus.OK);
    }

    @GetMapping(value = {"/roles"})
    public ResponseEntity<List<Role>> getRoleList() {
        return new ResponseEntity<>(roleService.allRoles(), HttpStatus.OK);
    }

    @PostMapping(value = {"/users"})
    public ResponseEntity<HttpStatus> createUser(@RequestBody UserDTO userDTO) {
        userService.saveUser(convertUserDTOToUser(userDTO));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = {"/users/{id}"})
    public ResponseEntity<HttpStatus> updateUser(@PathVariable("id") Long id, @RequestBody UserDTO userDTO) {
        User user = convertUserDTOToUser(userDTO);
        user.setId(id);
        userService.updateUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<HttpStatus>  deleteUser(@PathVariable("id") Long id) {
        userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
