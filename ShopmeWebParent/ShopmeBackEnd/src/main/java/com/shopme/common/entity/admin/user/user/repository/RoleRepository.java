package com.shopme.common.entity.admin.user.user.repository;


import com.shopme.common.entity.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface RoleRepository extends CrudRepository <Role,Integer> {
}
