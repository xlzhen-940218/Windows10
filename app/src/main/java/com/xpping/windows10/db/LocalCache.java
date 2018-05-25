package com.xpping.windows10.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 本地缓存 on 2015/7/17.
 */

@DatabaseTable(tableName = "local_cache")
public class LocalCache extends DBEntity {

    @DatabaseField(generatedId = true)
    public Integer id;
    @DatabaseField
    public String key;
    @DatabaseField
    public String result;

    public LocalCache() {
    }

    public LocalCache(String key, String result) {
        super();
        this.key = key;
        this.result = result;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
