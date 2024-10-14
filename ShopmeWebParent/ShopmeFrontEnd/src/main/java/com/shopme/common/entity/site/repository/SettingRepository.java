package com.shopme.common.entity.site.repository;

import com.shopme.common.entity.setting.Setting;
import com.shopme.common.enums.SettingCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SettingRepository extends JpaRepository<Setting,String> {
    public List<Setting> findByCategory(SettingCategory category);

    @Query("Select s From Setting s WHERE s.category= ?1 OR s.category=?2 ")
    public List<Setting> findByTwoCategories(SettingCategory cate1,SettingCategory cate2);

    public Setting findByKey(String key);
}
