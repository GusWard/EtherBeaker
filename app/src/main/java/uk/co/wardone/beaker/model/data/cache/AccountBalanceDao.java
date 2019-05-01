package uk.co.wardone.beaker.model.data.cache;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import uk.co.wardone.beaker.model.data.model.AccountBalance;

@Dao
public interface AccountBalanceDao extends BaseDao<AccountBalance> {

    @Query("SELECT * FROM AccountBalance WHERE address LIKE :address LIMIT 1")
    LiveData<AccountBalance> getBalance(String address);

    @Query("SELECT * FROM AccountBalance WHERE address LIKE :address LIMIT 1")
    AccountBalance getBalanceSync(String address);

}
