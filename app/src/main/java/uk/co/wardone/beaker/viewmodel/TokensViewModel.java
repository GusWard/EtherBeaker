package uk.co.wardone.beaker.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uk.co.wardone.beaker.BuildConfig;
import uk.co.wardone.beaker.model.data.AppDatabase;
import uk.co.wardone.beaker.model.data.model.ERC20Token;
import uk.co.wardone.beaker.model.repo.TokenRepository;
import uk.co.wardone.beaker.viewmodel.data.TokensViewData;

public class TokensViewModel extends AndroidViewModel {

    private AppDatabase appDatabase;
    private MutableLiveData<TokensViewData> tokensViewLiveData;

    public TokensViewModel(@NonNull Application application) {
        super(application);

        appDatabase = AppDatabase.getInstance(application);
        initViewData();
        initRepositories();

    }

    private void initViewData() {

        tokensViewLiveData = new MutableLiveData<>();
        tokensViewLiveData.setValue(new TokensViewData());

    }

    private void initRepositories() {

        LiveData<List<ERC20Token>> tokens = TokenRepository.getInstance(appDatabase).get(BuildConfig.TEST_ETHEREUM_WALLET_ADDRESS);
        tokens.observeForever(erc20Tokens -> {

            TokensViewData tokensViewData = tokensViewLiveData.getValue();

            if(tokensViewData != null){

                Collections.sort(erc20Tokens, (t1, t2) -> {

                    if(t1.ethBalance < t2.ethBalance){

                        return 1;

                    }else if(t1.ethBalance > t2.ethBalance){

                        return -1;

                    }else{

                        return 0;

                    }
                });

                List<TokensViewData.TokenItem> tokenItems = new ArrayList<>();

                for(ERC20Token erc20Token : erc20Tokens){

                    tokenItems.add(new TokensViewData.TokenItem(erc20Token.name, erc20Token.symbol, erc20Token.balance, erc20Token.ethBalance));

                }

                if(!tokensViewData.tokens.equals(tokenItems)){

                    tokensViewData.setTokens(tokenItems);
                    tokensViewLiveData.postValue(tokensViewData);

                }
            }
        });
    }

    public LiveData<TokensViewData> getTokensViewData() {

        return tokensViewLiveData;

    }
}
