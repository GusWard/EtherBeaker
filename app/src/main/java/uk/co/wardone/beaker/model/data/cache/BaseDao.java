package uk.co.wardone.beaker.model.data.cache;

import androidx.room.Insert;

import static androidx.room.OnConflictStrategy.REPLACE;

public interface BaseDao<T> {

    @Insert(onConflict = REPLACE)
    void save(T data);

}
