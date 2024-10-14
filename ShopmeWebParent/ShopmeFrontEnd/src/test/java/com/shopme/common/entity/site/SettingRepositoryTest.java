package com.shopme.common.entity.site;


import com.shopme.common.entity.setting.Setting;
import com.shopme.common.enums.SettingCategory;
import com.shopme.common.entity.site.repository.SettingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class SettingRepositoryTest {

    @Autowired
    private SettingRepository repository;

    @Test
    public void findByTwoCategories(){
       List<Setting> settingList= repository.findByTwoCategories(SettingCategory.GENERAL,SettingCategory.CURRENCY);
       settingList.forEach(System.out::println);
    }
}
