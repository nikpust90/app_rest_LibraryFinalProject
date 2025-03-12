package app_rest_libraryfinalproject.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import app_rest_libraryfinalproject.dto.PersonDTO;
import app_rest_libraryfinalproject.model.Person;
import app_rest_libraryfinalproject.repositories.PeopleRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PeopleService {

    private final PeopleRepository peopleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public void savePerson(Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        peopleRepository.save(person);
    }

    public void updatePerson(Person person) {
        peopleRepository.save(person);
    }

    public Optional<Person> findByUsername(String username) {
        return peopleRepository.findByUsername(username);
    }

    public void deletePerson(Long id) {
        peopleRepository.deleteById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void doAdminSomething() {
        System.out.println("Admin is doing something");
    }

    public Person convertDTOToPerson(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }

    public PersonDTO convertPersonToDTO(Person byId) {
        return modelMapper.map(byId, PersonDTO.class);
    }

}