package com.shopme.common.entity.admin.user.user.repository;

import com.shopme.common.entity.setting.Setting;
import com.shopme.common.enums.SettingCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SettingRepository extends JpaRepository<Setting,String> {
    public List<Setting> findByCategory(SettingCategory category);
}
