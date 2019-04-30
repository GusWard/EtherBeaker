package uk.co.wardone.beaker.modal.data.cache;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import uk.co.wardone.beaker.modal.data.model.ERC20Token;

@Dao
public interface ERC20TokenDao extends BaseDao<List<ERC20Token>> {

    @Query("SELECT * FROM erc20token WHERE address LIKE :address")
    LiveData<List<ERC20Token>> getTokens(String address);

    @Query("SELECT * FROM erc20token WHERE address LIKE :address")
    List<ERC20Token> getTokensSync(String address);


}
