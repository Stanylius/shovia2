package umn.ac.shovia;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class RNotifRepository {
    private RNotifDAO daoRNotif;
    private LiveData<List<RNotif>> daftarRNotif;

     RNotifRepository(Application app){
        RNotifRoomDatabase db = RNotifRoomDatabase.getDatabase(app);
        daoRNotif = db.daoRNotif();
         daftarRNotif = daoRNotif.getAllRNotif();

    }
    public  LiveData<List<RNotif>> getDaftarRNotif(){

        return this.daftarRNotif;
    }
    public void insert(RNotif rnotif){
        new insertAsyncTask(daoRNotif).execute(rnotif);
    }

    public void deleteAll(){
        new deleteAllAsyncTask(daoRNotif).execute();
    }
    public void delete(RNotif rnotif) {
        new deleteAsyncTask(daoRNotif).execute(rnotif);
    }

    private static class insertAsyncTask extends
            AsyncTask<RNotif, Void, Void> {
        private RNotifDAO asyncDaoRNotif;
        insertAsyncTask(RNotifDAO dao){
            this.asyncDaoRNotif = dao;
        }
        @Override
        protected Void doInBackground(final RNotif... rnotifs) {
            asyncDaoRNotif.insert(rnotifs[0]);
            return null;
        }
    }
    private static class deleteAllAsyncTask extends
            AsyncTask<Void, Void, Void> {
        private RNotifDAO asyncDaoRNotif;
        deleteAllAsyncTask(RNotifDAO dao){
            this.asyncDaoRNotif = dao;
        }
        @Override
        protected Void doInBackground(final Void... voids) {
            asyncDaoRNotif.deleteAll();
            return null;
        }
        private static class deleteAsyncTask extends
                AsyncTask<RNotif, Void, Void>{
            private RNotifDAO asyncDaoRNotif;
            deleteAsyncTask(RNotifDAO dao){
                this.asyncDaoRNotif = dao;
            }
            @Override
            protected Void doInBackground(final RNotif... rnotifs) {
                asyncDaoRNotif.delete(rnotifs[0]);
                return null;
            }
        }
    }
    private static class deleteAsyncTask extends
            AsyncTask<RNotif, Void, Void>{
        private RNotifDAO asyncDaoRNotif;
        deleteAsyncTask(RNotifDAO dao){
            this.asyncDaoRNotif = dao;
        }
        @Override
        protected Void doInBackground(final RNotif... rnotifs) {
            asyncDaoRNotif.delete(rnotifs[0]);
            return null;
        }
    }
}

