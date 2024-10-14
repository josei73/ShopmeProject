package com.shopme.common.entity.admin.user.user.repository;

import com.shopme.common.entity.User;
import com.shopme.common.entity.admin.user.paging.SearchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/*
    Hier werden die Entity Klassen und den Anfragen an der Datenbank
 */
@Repository
public interface UserRepository extends SearchRepository<User,Integer> {
    @Query("SELECT u FROM User u WHERE u.email= :email")
    public User getUserByEmail(@Param("email") String email);

    public Long countById(Integer id);

    @Query("SELECT u FROM User u WHERE CONCAT(u.id,' ',u.email,' ',u.firstName,' ', u.lastName) LIKE %?1% ")
    public Page<User> findAll(String keyword, Pageable pageable);

    @Query("Update User u set u.enabled = ?2 Where u.id = ?1")
    @Modifying
    public void updateEnableStatus(Integer id, boolean enabled);
}
