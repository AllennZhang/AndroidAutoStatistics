package com.hipac.codeless.store;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.hipac.codeless.store.table.CombineEvent;

import java.util.List;

/**
 * Created by youri on 2018/3/7.
 */

@Dao
public interface CombineEventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CombineEvent... events) ;

     @Insert(onConflict = OnConflictStrategy.REPLACE)
     void insert(List<CombineEvent> list);

     @Delete
     void delete(CombineEvent... events);

    @Delete
    void delete(List<CombineEvent> list);

     @Update(onConflict = OnConflictStrategy.REPLACE)
     void update(CombineEvent... events);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(List<CombineEvent> list);

    @Query("SELECT count(*) FROM  combineEvent ")
    long queryCount();


    @Query("SELECT * FROM  combineEvent ORDER BY id ASC LIMIT 20")
    List<CombineEvent> query();


}
