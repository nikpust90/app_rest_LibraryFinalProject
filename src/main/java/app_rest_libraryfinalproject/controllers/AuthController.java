package app_rest_libraryfinalproject.controllers;

import app_rest_libraryfinalproject.validation.AuthValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import app_rest_libraryfinalproject.dto.AuthenticationDTO;
import app_rest_libraryfinalproject.dto.PersonDTO;
import app_rest_libraryfinalproject.dto.PersonDeleteDTO;
import app_rest_libraryfinalproject.dto.PersonUpdateDTO;
import app_rest_libraryfinalproject.model.Person;
import app_rest_libraryfinalproject.service.PeopleService;
import app_rest_libraryfinalproject.util.JWTUtil;
import app_rest_libraryfinalproject.validation.PersonValidator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {
    private final PeopleService peopleService;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final AuthValidator authValidator;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody AuthenticationDTO authDTO) {
        log.info("Login attempt for user: {}", authDTO.getUsername());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authDTO.getUsername(), authDTO.getPassword())
            );
            return peopleService.findByUsername(authDTO.getUsername())
                    .map(person -> {
                        String token = jwtUtil.generateToken(person.getUsername(), person.getRole());
                        log.info("User '{}' successfully logged in", authDTO.getUsername());
                        return ResponseEntity.ok(Map.of("jwt-token", token));
                    })
                    .orElseGet(() -> {
                        log.warn("Login failed: User '{}' not found", authDTO.getUsername());
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "User not found"));
                    });
        } catch (BadCredentialsException e) {
            log.warn("Login failed: Incorrect password for user '{}'", authDTO.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Incorrect login or password"));
        }
    }

    @PostMapping("/registration")
    public Map<String, String> register(@RequestBody @Valid PersonDTO personDTO, BindingResult bindingResult) {
        log.info("Registration attempt for user: {}", personDTO.getUsername());
        Person person = peopleService.convertDTOToPerson(personDTO);
        authValidator.validateRegistration(person, bindingResult);
        if (bindingResult.hasErrors()) {
            log.warn("Registration failed: Validation errors for user '{}'", personDTO.getUsername());
            return Map.of("message", "Validation failed", "errors", bindingResult.getAllErrors().toString());
        }
        peopleService.savePerson(person);
        log.info("User '{}' successfully registered", personDTO.getUsername());
        String token = jwtUtil.generateToken(person.getUsername(), person.getRole());
        return Map.of("jwt-token", token);
    }

    @PostMapping("/updateUser")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> updateUser(@RequestBody @Valid PersonUpdateDTO personDTO, BindingResult bindingResult) {
        log.info("Update attempt for user: {}", personDTO.getUsername());
        Map<String, Object> response = new HashMap<>();
        return peopleService.findByUsername(personDTO.getUsername())
                .map(person -> {
                    authValidator.validateUpdate(personDTO, bindingResult);
                    if (bindingResult.hasErrors()) {
                        log.warn("Update failed: Validation errors for user '{}'", personDTO.getUsername());
                        response.put("message", "error body");
                        return response;
                    }
                    peopleService.updatePerson(authValidator.updateFields(person, personDTO));
                    log.info("User '{}' successfully updated", personDTO.getUsername());
                    response.put("username", person.getUsername());
                    response.put("status", "updated");
                    return response;
                })
                .orElseGet(() -> {
                    log.warn("Update failed: User '{}' not found", personDTO.getUsername());
                    response.put("message", "User not found");
                    return response;
                });
    }


    @PostMapping("/deleteUser")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> deleteUser(@RequestBody @Valid PersonDeleteDTO personDTO, BindingResult bindingResult) {
        log.info("Delete attempt for user: {}", personDTO.getUsername());
        authValidator.validateDeletion(personDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            log.warn("Delete failed: Validation errors for user '{}'", personDTO.getUsername());
            return Map.of("message", "error body");
        }
        return peopleService.findByUsername(personDTO.getUsername())
                .map(person -> {
                    peopleService.deletePerson(person.getId());
                    log.info("User '{}' successfully deleted", personDTO.getUsername());
                    return Map.of("username", (Object) person.getUsername(), "status", (Object) "delete");
                })
                .orElseGet(() -> {
                    log.warn("Delete failed: User '{}' not found", personDTO.getUsername());
                    return Map.of("message", (Object) "User not found");
                });
    }
}
