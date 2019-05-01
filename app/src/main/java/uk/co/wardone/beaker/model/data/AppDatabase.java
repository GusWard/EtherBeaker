package uk.co.wardone.beaker.model.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import uk.co.wardone.beaker.model.data.cache.AccountBalanceDao;
import uk.co.wardone.beaker.model.data.cache.ERC20TokenDao;
import uk.co.wardone.beaker.model.data.cache.TokenBalanceDao;
import uk.co.wardone.beaker.model.data.model.AccountBalance;
import uk.co.wardone.beaker.model.data.model.ERC20Token;
import uk.co.wardone.beaker.model.data.model.TokenBalance;

@Database(entities = {AccountBalance.class, TokenBalance.class, ERC20Token.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "app-database";

    private static AppDatabase instance;

    public abstract AccountBalanceDao getBalanceDao();

    public abstract ERC20TokenDao getTokenDao();

    public abstract TokenBalanceDao getTokenBalanceDao();

    public synchronized static AppDatabase getInstance(Context context) {

        if (instance == null) {

            instance = buildDatabase(context);

        }

        return instance;
    }

    private static AppDatabase buildDatabase(final Context context) {
        return Room.databaseBuilder(context,
                AppDatabase.class,
                DATABASE_NAME)
                .allowMainThreadQueries()
                .build();
    }

}
