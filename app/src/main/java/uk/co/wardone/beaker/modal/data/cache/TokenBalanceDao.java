package uk.co.wardone.beaker.modal.data.cache;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import uk.co.wardone.beaker.modal.data.model.TokenBalance;

@Dao
public interface TokenBalanceDao extends BaseDao<TokenBalance> {

    @Query("SELECT * FROM tokenbalance WHERE address LIKE :address LIMIT 1")
    LiveData<TokenBalance> getTokenBalance(String address);

    @Query("SELECT * FROM tokenbalance WHERE address LIKE :address LIMIT 1")
    TokenBalance getTokenBalanceSync(String address);

}
