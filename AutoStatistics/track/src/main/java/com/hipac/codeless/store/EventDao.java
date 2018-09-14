package com.hipac.codeless.store;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.hipac.codeless.store.table.StatEvent;

import java.util.List;

/**
 * Created by youri on 2018/3/7.
 */

@Deprecated
@Dao
public interface EventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(StatEvent ... statEvent) ;

     @Insert(onConflict = OnConflictStrategy.REPLACE)
     void insert(List<StatEvent> list);

     @Delete
     void delete(StatEvent ...statEvent);

    @Delete
    void delete(List<StatEvent> list);

     @Update(onConflict = OnConflictStrategy.REPLACE)
     void update(StatEvent ...statEvent);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(List<StatEvent> list);

    @Query("SELECT * FROM  event ")
     List<StatEvent> query();




}
