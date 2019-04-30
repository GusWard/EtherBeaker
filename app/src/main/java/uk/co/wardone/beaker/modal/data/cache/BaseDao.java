package uk.co.wardone.beaker.modal.data.cache;

import androidx.room.Dao;
import androidx.room.Insert;

import static androidx.room.OnConflictStrategy.REPLACE;

public interface BaseDao<T> {

    @Insert(onConflict = REPLACE)
    void save(T data);

}
