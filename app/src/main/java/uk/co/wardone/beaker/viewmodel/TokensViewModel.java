package uk.co.wardone.beaker.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import uk.co.wardone.beaker.BuildConfig;
import uk.co.wardone.beaker.model.api.blockexplorer.etherscan.EtherscanService;
import uk.co.wardone.beaker.model.data.AppDatabase;
import uk.co.wardone.beaker.model.data.model.ERC20Token;
import uk.co.wardone.beaker.model.repo.TokenRepository;

public class TokensViewModel extends AndroidViewModel {

    private AppDatabase appDatabase;
    private LiveData<List<ERC20Token>> tokens;

    public TokensViewModel(@NonNull Application application) {
        super(application);

        appDatabase = AppDatabase.getInstance(application);
        tokens = TokenRepository.getInstance(appDatabase).get(BuildConfig.TEST_ETHEREUM_WALLET_ADDRESS);

    }

    @Nullable
    public LiveData<List<ERC20Token>> getTokens() {

        return tokens;

    }
}
