package com.shopme.common.entity.setting;

import java.util.List;

public class SettingBag {

    private List<Setting> settings;

    public SettingBag(List<Setting> settings) {
        this.settings = settings;
    }

    public Setting get(String key){

        int index = settings.indexOf(new Setting(key));
        if(index >= 0){
            return settings.get(index);
        }
        System.out.println(key);
        System.out.println(index);
        return null;
    }

    public String getValue(String key){


        Setting setting = get(key);

        if(setting != null)
            return setting.getValue();
        return null;
    }

    public void update(String key, String value){
        Setting setting = get(key);
        if(setting != null && value != null) {
            setting.setValue(value);
        }
    }

    public List<Setting> list(){
        return settings;
    }
}
