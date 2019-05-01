package uk.co.wardone.beaker.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import uk.co.wardone.beaker.model.api.blockexplorer.etherscan.EtherscanService;
import uk.co.wardone.beaker.model.data.AppDatabase;
import uk.co.wardone.beaker.model.data.model.AccountBalance;
import uk.co.wardone.beaker.model.data.model.TokenBalance;
import uk.co.wardone.beaker.model.repo.AccountBalanceRepository;
import uk.co.wardone.beaker.model.repo.TokenBalanceRepository;

public class BalanceViewModel extends AndroidViewModel {

    private AppDatabase appDatabase;
    private LiveData<AccountBalance> balanceLiveData;
    private LiveData<TokenBalance> tokenBalance;

    public BalanceViewModel(@NonNull Application application) {
        super(application);

        appDatabase = AppDatabase.getInstance(application);
        balanceLiveData = AccountBalanceRepository.getInstance(appDatabase).get(EtherscanService.TEST_ADDRESS);
        tokenBalance = TokenBalanceRepository.getInstance(appDatabase).get(EtherscanService.TEST_ADDRESS);

    }

    @Nullable
    public LiveData<AccountBalance> getBalanceLiveData(){

        return balanceLiveData;

    }

    @Nullable
    public LiveData<TokenBalance> getTokenBalance() {

        return tokenBalance;

    }

    public void refreshAccountBalance(){

        AccountBalanceRepository.getInstance(appDatabase).refresh(EtherscanService.TEST_ADDRESS);

    }

    public void refreshTokenBalance(){

        TokenBalanceRepository.getInstance(appDatabase).refresh(EtherscanService.TEST_ADDRESS);

    }

}
