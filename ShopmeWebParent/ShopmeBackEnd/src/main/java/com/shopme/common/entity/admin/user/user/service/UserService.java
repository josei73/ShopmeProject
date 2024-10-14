package com.shopme.common.entity.admin.user.user.service;


import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import com.shopme.common.entity.admin.user.paging.PagingAndSortingHelper;
import com.shopme.common.entity.admin.user.paging.PagingAndSortingParam;
import com.shopme.common.entity.admin.user.user.repository.RoleRepository;
import com.shopme.common.entity.admin.user.user.exception.UserNotFoundException;
import com.shopme.common.entity.admin.user.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;


/*
    CSV wurde durch SuperCSV generiert


    Transactional habe ich gebraucht um queries in der Tabelle zu Updaten
 */
@Service
@Transactional
public class UserService {
    public static final int USERS_PER_PAGE = 4;
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public User getByEmail(String email) {
        return userRepo.getUserByEmail(email);
    }

    public List<User> listAll() {
        return (List<User>) userRepo.findAll(Sort.by("firstName"));
    }

    public void listByPage(int pageNumber, PagingAndSortingHelper helper) {
        helper.listEntities(pageNumber,USERS_PER_PAGE,userRepo);
    }


    public List<Role> listRoles() {
        return (List<Role>) roleRepository.findAll();

    }

    public User save(User user) {
        // Wenn ungleich ist, dann ist man im bearbeitungsmodus ansonsten wird ein neuer User erzeugt
        boolean isUpdatingUser = (user.getId() != null);
        if (isUpdatingUser) {
            User existingUser = userRepo.findById(user.getId()).get();
            if (user.getPassword().isEmpty()) {
                // Hier rein dann will man das Passwort nicht bearbeiten und behält das alte
                user.setPassword(existingUser.getPassword());
            } else {
                encodePassword(user);
            }
        } else {
            encodePassword(user);
        }
        return userRepo.save(user);

    }

    public User updateAccount(User userInForm) {
        User userDB = userRepo.findById(userInForm.getId()).get();
        if (!userInForm.getPassword().isEmpty()) {
            userDB.setPassword(userInForm.getPassword());
            encodePassword(userDB);
        }
        if (userInForm.getPhotos() != null)
            userDB.setPhotos(userInForm.getPhotos());

        userDB.setFirstName(userInForm.getFirstName());
        userDB.setLastName(userInForm.getLastName());
        return userRepo.save(userDB);
    }

    private void encodePassword(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    public boolean isEmailUnique(Integer id, String email) {

        User userByEmail = userRepo.getUserByEmail(email);
        if (userByEmail == null)
            return true;
        // Wenn ID Gerade Null ist, heißt es dass, ich einen neuen User erstelle und nicht bearbeite die Anfrage
        boolean isCreatingNew = (id == null);
        if (isCreatingNew) {
            // Wenn User nicht null ist existiert er und die E-Mail ist besetzt deswegen False zurückgeben
            if (userByEmail != null) return false;
        } else {
            // Hier rein wenn, ich gerade einen User bearbeite.
            // Wenn die ID nicht gleich existiert schon ein User mit der E-Mail deswegen auch hier False
            if (!Objects.equals(userByEmail.getId(), id)) return false;
        }

        return true;
    }

    public User get(Integer id) throws UserNotFoundException {
        try {
            return userRepo.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new UserNotFoundException("Could not find any user with ID" + id);
        }
    }

    public void delete(Integer id) throws UserNotFoundException {
        Long countById = userRepo.countById(id);
        if (countById == null | countById == 0) throw new UserNotFoundException("Could not find any user with ID" + id);
        userRepo.deleteById(id);
    }

    public void updateUserEnableStatus(Integer id, boolean enable) {
        userRepo.updateEnableStatus(id, enable);
    }
}
