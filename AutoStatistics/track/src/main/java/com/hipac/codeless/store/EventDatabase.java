package com.hipac.codeless.store;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.VisibleForTesting;

import com.hipac.codeless.store.table.CombineEvent;
import com.hipac.codeless.update.TraceEvent;
import com.hipac.codeless.update.TraceEventDao;

/**
 * Created by youri on 2017/12/4.
 * Room让你可以让你写Migration类来保存用户数据。
 * 每个Migration类指定from和to版本。
 * 运行时Room运行每个Migration类的 migrate() 方法，
 * 使用正确的顺序把数据库迁移到新版本。
 */

@Database(entities = {CombineEvent.class, TraceEvent.class},version = 2)
public abstract class EventDatabase extends RoomDatabase {



    @VisibleForTesting
    public static final String DATABASE_NAME = "stat.db";
    private static volatile EventDatabase mInstance;
    public abstract CombineEventDao eventDao();
    public abstract TraceEventDao traceEventDao();

    public static EventDatabase instance(Context context){
        if (mInstance == null){
            synchronized (EventDatabase.class){
                if (mInstance == null) {
                    mInstance = buildDatabase(context.getApplicationContext());
                }
            }
        }
        return mInstance;
    }

    private static EventDatabase buildDatabase(final Context context){
        try {
            return Room.databaseBuilder(context,EventDatabase.class,DATABASE_NAME)
                    .addMigrations(MIGRATION_1_2)
                    .build();
        }catch (Exception e){

            return null;
        }
    }


// todo for test update db version
 @VisibleForTesting
  private   static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("DROP TABLE  IF EXISTS combineEvent");
        }
    };



    public void close(){
        if (mInstance != null){
            mInstance.close();
        }
    }
}
