package umn.ac.shovia;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {RNotif.class}, version = 1, exportSchema = false)
public abstract class RNotifRoomDatabase extends RoomDatabase {
    public abstract RNotifDAO daoRNotif();
    private static RNotifRoomDatabase INSTANCE;
    static RNotifRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RNotifRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            RNotifRoomDatabase.class, "dbRNotif")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {
                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                }
            };
}
