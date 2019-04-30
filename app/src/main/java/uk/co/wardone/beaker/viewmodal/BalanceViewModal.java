package uk.co.wardone.beaker.viewmodal;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import uk.co.wardone.beaker.modal.api.blockexplorer.etherscan.EtherscanService;
import uk.co.wardone.beaker.modal.data.AppDatabase;
import uk.co.wardone.beaker.modal.data.model.AccountBalance;
import uk.co.wardone.beaker.modal.data.model.TokenBalance;
import uk.co.wardone.beaker.modal.repo.AccountBalanceRepository;
import uk.co.wardone.beaker.modal.repo.TokenBalanceRepository;

public class BalanceViewModal extends AndroidViewModel {

    private AppDatabase appDatabase;
    private LiveData<AccountBalance> balanceLiveData;
    private LiveData<TokenBalance> tokenBalance;

    public BalanceViewModal(@NonNull Application application) {
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
