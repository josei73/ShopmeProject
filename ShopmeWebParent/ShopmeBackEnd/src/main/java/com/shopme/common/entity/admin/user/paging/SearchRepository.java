package com.shopme.common.entity.admin.user.paging;

import com.shopme.common.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

import java.util.List;

@NoRepositoryBean
public interface SearchRepository<T,ID> extends JpaRepository<T,ID> {
    public Page<T> findAll(String keyword, Pageable pageable);
}
