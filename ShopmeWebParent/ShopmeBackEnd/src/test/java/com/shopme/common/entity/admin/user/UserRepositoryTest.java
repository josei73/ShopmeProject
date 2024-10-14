package com.shopme.common.entity.admin.user;


import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import com.shopme.common.entity.admin.user.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.data.domain.Pageable;


//import java.awt.print.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// Erstellt ein Test auf der richtigen tabelle
@Rollback(false) // Füt Hibernate damit die änderung commitet werden
public class UserRepositoryTest {

    @Autowired
    private UserRepository repo;

    @Autowired
    private TestEntityManager entityManager;


    @Test
    public void testCreateNewUserWithOneRole() {
        //Sucht in der Table Role nach der id 1
        Role roleAdmin = entityManager.find(Role.class, 1);
        User user = new User("name@codejava.net", "name2022", "Test", "Test");
        user.addRole(roleAdmin);

        User savedUser = repo.save(user);
        assertThat(savedUser.getId()).isGreaterThan(0);

    }

    @Test
    public void testCreateNewUserWithTwoRole() {
        User userKwame = new User("kwame@gmail.com", "kwame2022", "Kwame", "Osei");
        Role roleAssistant = new Role(5);
        Role roleEditor = new Role(3);
        userKwame.addRole(roleAssistant);
        userKwame.addRole(roleEditor);
        User savedUser = repo.save(userKwame);
        assertThat(savedUser.getId()).isGreaterThan(0);


    }

    @Test
    public void testListAllUser() {
        Iterable<User> listUsers = repo.findAll();
        listUsers.forEach(user -> System.out.println(user));

    }

    @Test

    public void testGetUserByID() {
        User userOsei = repo.findById(1).get();
        System.out.println(userOsei);
        assertThat(userOsei).isNotNull();
    }

    @Test
    public void testUpdateUserDetails() {
        User userOsei = repo.findById(1).get();
        userOsei.setEnabled(true);
        userOsei.setEmail("oseijavaprogrammer@gmail.com");
        repo.save(userOsei);

    }

    @Test
    public void testUpdateUserRoles() {
        User userKwame = repo.findById(2).get();
        Role roleEditor = new Role(3);
        Role roleSalesperson = new Role(2);

        userKwame.getRoles().remove(roleEditor);
        userKwame.addRole(roleSalesperson);
        repo.save(userKwame);
    }

    @Test
    public void testDeleteUser() {
        Integer userId = 2;
        repo.deleteById(userId);
    }

    @Test
    public void testGetUserByEmail() {
        String email = "oseijavaprogrammer@gmail.com";
        User user = repo.getUserByEmail(email);
        assertThat(user).isNotNull();
    }

    @Test
    public void testCountById() {
        Integer id = 1;
        Long countById = repo.countById(id);
        assertThat(countById).isNotNull().isGreaterThan(0);

    }

    @Test
    public void testDisableUser() {
        Integer id = 1;
        repo.updateEnableStatus(id, false);
    }

    @Test
    public void testEnableUser() {
        Integer id = 3;
        repo.updateEnableStatus(id, true);
    }

    @Test
    public void testListFirstPage() {
        int pageNumber = 2;
        int pageSize = 4;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = repo.findAll(pageable);
        List<User> list = page.getContent();
        list.forEach(user -> System.out.println(user));
        assertThat(list.size()).isEqualTo(pageSize);
    }

    @Test
    public void testSearchUser(){
        String keyword = "bruce";
        int pageNumber = 0;
        int pageSize = 4;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = repo.findAll(keyword,pageable);
        List<User> list = page.getContent();
        list.forEach(user -> System.out.println(user));
        assertThat(list.size()).isGreaterThan(0);
    }
}
