package uk.co.wardone.beaker.modal.repo;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.HashMap;
import java.util.List;

import uk.co.wardone.beaker.modal.data.AppDatabase;
import uk.co.wardone.beaker.modal.data.cache.TokenBalanceDao;
import uk.co.wardone.beaker.modal.data.model.ERC20Token;
import uk.co.wardone.beaker.modal.data.model.TokenBalance;

public class TokenBalanceRepository extends BaseRepository<String, TokenBalance>{

    private static final String TAG = "AccountBalanceRepository";

    private static TokenBalanceRepository instance;


    private TokenBalanceDao tokenBalanceDao;
    private HashMap<String, Observer<List<ERC20Token>>> observers = new HashMap<>();

    public TokenBalanceRepository(AppDatabase appDatabase) {
        super(appDatabase);

        this.tokenBalanceDao = appDatabase.getTokenBalanceDao();
    }

    public static TokenBalanceRepository getInstance(AppDatabase appDatabase){

        if(instance == null){

            instance = new TokenBalanceRepository(appDatabase);

        }

        return instance;

    }

    @Override
    public LiveData<TokenBalance> get(String address) {

        LiveData<TokenBalance> tokenBalanceLiveData = tokenBalanceDao.getTokenBalance(address);

        if(tokenBalanceLiveData.getValue() == null || System.currentTimeMillis() - tokenBalanceLiveData.getValue().updated > MAX_CACHE_AGE_MILLIS){

            refresh(address);

        }

        return tokenBalanceLiveData;

    }

    @Override
    public TokenBalance getSync(String address) {

        TokenBalance tokenBalance = tokenBalanceDao.getTokenBalanceSync(address);

        if(tokenBalance == null || System.currentTimeMillis() - tokenBalance.updated > MAX_CACHE_AGE_MILLIS){

            refresh(address);

        }

        return tokenBalance;

    }

    /**
     * observe the token repository and whenever there is a change re calculate the the total token
     * balance
     * @param address account address
     */
    @Override
    public void refresh(String address) {

        LiveData<List<ERC20Token>> tokenLiveData = TokenRepository.getInstance(appDatabase).get(address);

        if (!observers.containsKey(address)) {

            Observer<List<ERC20Token>> observer = erc20Tokens -> {

                Log.i(TAG, "erc20 tokens changed");

                if(erc20Tokens.isEmpty()){

                    return;

                }

                TokenBalance tokenBalance = new TokenBalance(address, 0, 0);

                for (ERC20Token erc20Token : erc20Tokens) {

                    tokenBalance.balance += erc20Token.ethBalance;
                    tokenBalance.totalTokens++;

                }

                Log.i(TAG, "saving: " + tokenBalance.toString());
                tokenBalanceDao.save(tokenBalance);

            };

            observers.put(address, observer);

            tokenLiveData.observeForever(observer);
        }

    }
}
