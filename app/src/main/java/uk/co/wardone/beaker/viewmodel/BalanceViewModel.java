package uk.co.wardone.beaker.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import uk.co.wardone.beaker.BuildConfig;
import uk.co.wardone.beaker.model.data.AppDatabase;
import uk.co.wardone.beaker.model.data.model.AccountBalance;
import uk.co.wardone.beaker.model.data.model.TokenBalance;
import uk.co.wardone.beaker.model.repo.AccountBalanceRepository;
import uk.co.wardone.beaker.model.repo.TokenBalanceRepository;
import uk.co.wardone.beaker.viewmodel.data.BalanceViewData;

public class BalanceViewModel extends AndroidViewModel {

    private AppDatabase appDatabase;
    private MutableLiveData<BalanceViewData> balanceViewData;

    public BalanceViewModel(@NonNull Application application) {
        super(application);

        appDatabase = AppDatabase.getInstance(application);
        initViewData();
        initRepositories();


    }

    private void initRepositories() {

        LiveData<AccountBalance> balanceLiveData = AccountBalanceRepository.getInstance(appDatabase).get(BuildConfig.TEST_ETHEREUM_WALLET_ADDRESS);
        balanceLiveData.observeForever(accountBalance -> {

            BalanceViewData data = balanceViewData.getValue();

            if(data != null && accountBalance != null){

                data.setEthBalance((float) accountBalance.balance);
                data.setBtcBalance((float) accountBalance.btcBalance);
                balanceViewData.postValue(data);

            }

        });

        LiveData<TokenBalance> tokenBalance = TokenBalanceRepository.getInstance(appDatabase).get(BuildConfig.TEST_ETHEREUM_WALLET_ADDRESS);
        tokenBalance.observeForever(tokenBalance1 -> {

            BalanceViewData data = balanceViewData.getValue();

            if(data != null && tokenBalance1 != null){

                data.setAggregateTokenBalance((float) tokenBalance1.balance);
                data.setTotalTokens(tokenBalance1.totalTokens);
                balanceViewData.postValue(data);

            }
        });
    }

    private void initViewData() {

        balanceViewData = new MutableLiveData<>();
        balanceViewData.setValue(new BalanceViewData());

    }

    public LiveData<BalanceViewData> getBalanceViewData(){

        return balanceViewData;

    }

    public void refreshAccountBalance(){

        AccountBalanceRepository.getInstance(appDatabase).refresh(BuildConfig.TEST_ETHEREUM_WALLET_ADDRESS);

    }

    public void refreshTokenBalance(){

        TokenBalanceRepository.getInstance(appDatabase).refresh(BuildConfig.TEST_ETHEREUM_WALLET_ADDRESS);

    }

}
