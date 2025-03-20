package app_rest_libraryfinalproject.validation;

import app_rest_libraryfinalproject.dto.PersonDeleteDTO;
import app_rest_libraryfinalproject.dto.PersonUpdateDTO;
import app_rest_libraryfinalproject.model.Person;
import app_rest_libraryfinalproject.service.PeopleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Component
@RequiredArgsConstructor
public class AuthValidator {
    private final PeopleService peopleService;

    public void validateRegistration(Person person, BindingResult bindingResult) {
        if (peopleService.findByUsername(person.getUsername()).isPresent()) {
            bindingResult.rejectValue("username", "error.username", "User with this username already exists");
        }
        person.setRole(formatRole(person.getRole()));
    }

    public void validateUpdate(PersonUpdateDTO personDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("Invalid data");
        }
    }

    public void validateDeletion(PersonDeleteDTO personDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("Invalid data");
        }
    }

    public Person updateFields(Person person, PersonUpdateDTO personDTO) {
        if (personDTO.getYearOfBirth() != null) {
            person.setYearOfBirth(personDTO.getYearOfBirth());
        }
        if (personDTO.getEmail() != null) {
            person.setEmail(personDTO.getEmail());
        }
        if (personDTO.getRole() != null) {
            person.setRole(formatRole(personDTO.getRole()));
        }
        return person;
    }

    private String formatRole(String role) {
        if (role == null || role.isBlank()) {
            return "ROLE_USER";
        }
        return role.startsWith("ROLE_") ? role.toUpperCase() : "ROLE_" + role.toUpperCase();
    }
}

