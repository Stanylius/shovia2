package umn.ac.shovia;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RNotifDAO {
    @Query("SELECT * FROM tblRNotif ")
    LiveData<List<RNotif>> getAllRNotif();
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(RNotif rnotif);
    @Delete
    void delete(RNotif rnotif);
    @Query("DELETE FROM tblRNotif")
    void deleteAll();
}
