package com.hipac.codeless.update;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by youri on 2018/3/7.
 *
 * Notice : when handle with CURD in sqlite database
 * Do not do this in mainThread
 * Otherwise throw java.lang.IllegalStateException:
 * Cannot access database on the main thread
 * since it may potentially lock the UI for a long period of time.
 */

@Dao
public interface TraceEventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TraceEvent statEvent) ;

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<TraceEvent> statEvent) ;

     @Delete
     void delete(TraceEvent statEvent);

     @Delete
     void delete(List<TraceEvent> list);


     @Update(onConflict = OnConflictStrategy.REPLACE)
     void update(TraceEvent statEvent);

    @Query("SELECT * FROM  traceEvent ORDER BY requestTime ASC LIMIT 30")
    List<TraceEvent> query();

    @Query("SELECT count(*) FROM  traceEvent ")
    long queryCount();


}
