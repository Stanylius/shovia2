package umn.ac.shovia;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class RNotifViewModel extends AndroidViewModel {
    private RNotifRepository rnotifRepository;

    private LiveData<List<RNotif>> daftarRNotif;
    public RNotifViewModel(@NonNull Application application) {
        super(application);
        rnotifRepository = new RNotifRepository(application);
        daftarRNotif = rnotifRepository.getDaftarRNotif();

    }
    public LiveData<List<RNotif>> getDaftarRNotif(){

        return this.daftarRNotif;
    }
    public void insert(RNotif rnotif) {
        rnotifRepository.insert(rnotif);
    }

    public void deleteAll() {
        rnotifRepository.deleteAll();
    }
    public void delete(RNotif rnotif) {
        rnotifRepository.delete(rnotif);
    }

}
