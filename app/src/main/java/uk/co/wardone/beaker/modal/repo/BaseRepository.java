package uk.co.wardone.beaker.modal.repo;

import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import uk.co.wardone.beaker.modal.data.AppDatabase;

public abstract class BaseRepository<K, T> {

    protected static final String DIVISOR = "1000000000000000000";
    protected static final long MAX_CACHE_AGE_MILLIS = TimeUnit.MINUTES.toMillis(5);
    protected AppDatabase appDatabase;
    protected ExecutorService executorService;

    public BaseRepository(AppDatabase appDatabase){

        this.appDatabase = appDatabase;
        this.executorService = Executors.newSingleThreadExecutor();

    }

    public abstract LiveData<T> get(K key);

    public abstract T getSync(K key);

    public abstract void refresh(K key);

}
