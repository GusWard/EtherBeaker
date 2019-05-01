package uk.co.wardone.beaker.model.repo;

import android.util.Log;

import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import uk.co.wardone.beaker.BuildConfig;
import uk.co.wardone.beaker.model.api.blockexplorer.etherscan.EtherscanService;
import uk.co.wardone.beaker.model.api.blockexplorer.etherscan.data.ERC20Balance;
import uk.co.wardone.beaker.model.api.exchange.shapeshift.ShapeShiftService;
import uk.co.wardone.beaker.model.api.exchange.shapeshift.data.Market;
import uk.co.wardone.beaker.model.data.AppDatabase;
import uk.co.wardone.beaker.model.data.cache.ERC20TokenDao;
import uk.co.wardone.beaker.model.data.model.ERC20Token;

public class TokenRepository extends BaseRepository<String, List<ERC20Token>>{

    private static final String TAG = "TokenRepository";
    private static final String ETH_PAIR_SUFFIX = "_ETH";
    private static final String CONTRACT_OMISEGO_ADDRESS = "0xd26114cd6EE289AccF82350c8d8487fedB8A0C07";
    private static final String CONTRACT_REPUTATION_ADDRESS = "0xa74476443119A942dE498590Fe1f2454d7D4aC0d";
    private static final String CONTRACT_GOLEM_ADDRESS = "0x1985365e9f78359a9B6AD760e32412f4a445E862";

    private static TokenRepository instance;

    private List<TokenDefinition> tokenDefinitions = new ArrayList<>();
    private ShapeShiftService shapeShiftService;
    private EtherscanService etherscanService;
    private ERC20TokenDao tokenDao;
    private int currentPage = 0;
    private int offset = 100;

    public static TokenRepository getInstance(AppDatabase appDatabase){

        if(instance == null){

            instance = new TokenRepository(appDatabase);

        }

        return instance;

    }

    public TokenRepository(AppDatabase appDatabase){

        super(appDatabase);
        this.shapeShiftService = ShapeShiftService.get();
        this.etherscanService = EtherscanService.getWithExecutor(executorService);
        this.tokenDao = appDatabase.getTokenDao();

        this.tokenDefinitions.add(new TokenDefinition("OmiseGo", "OMG", CONTRACT_OMISEGO_ADDRESS));
        this.tokenDefinitions.add(new TokenDefinition("Reputation", "REP", CONTRACT_REPUTATION_ADDRESS));
        this.tokenDefinitions.add(new TokenDefinition("Golem", "GNT", CONTRACT_GOLEM_ADDRESS));

    }

    /**
     * get latest account data from cache, is the data is old refresh it
     * @param address - account address
     * @return latest account data wrapped in LiveData
     */
    @Override
    public LiveData<List<ERC20Token>> get(String address) {

        LiveData<List<ERC20Token>> tokensLiveData = tokenDao.getTokens(address);

        if(tokensLiveData.getValue() == null || tokensLiveData.getValue().isEmpty() || isAnyOld(tokensLiveData.getValue())){

            refresh(address);

        }

        return tokensLiveData;

    }

    @Override
    public List<ERC20Token> getSync(String address) {

        List<ERC20Token> tokens = tokenDao.getTokensSync(address);

        if(tokens == null || tokens.isEmpty() || isAnyOld(tokens)){

            refresh(address);

        }

        return tokens;

    }

    /**
     * page through transactions for ERC20 tokens, add each transaction value to its respective token
     * then save all results in cache.
     */
    @Override
    public void refresh(String address) {

        currentPage = 0;

        executorService.submit(() -> {

            List<ERC20Token> tokens = new ArrayList<>();

            for(TokenDefinition tokenDefinition : tokenDefinitions){

                try {

                    Call<ERC20Balance> balanceCall = etherscanService.getTokenBalance(BuildConfig.ETHERSCAN_TEST_API_KEY, BuildConfig.TEST_ETHEREUM_WALLET_ADDRESS, tokenDefinition.contract);
                    Response<ERC20Balance> balanceResponse = balanceCall.execute();

                    if(balanceResponse.isSuccessful() && balanceResponse.body() != null){

                        ERC20Balance erc20Balance = balanceResponse.body();

                        BigDecimal raw = new BigDecimal(erc20Balance.getResult()).setScale(21, RoundingMode.FLOOR);
                        BigDecimal result = raw.divide(new BigDecimal(DIVISOR), RoundingMode.FLOOR);
                        double balance = result.doubleValue();
                        double ethBalance = getEthBalance(tokenDefinition.symbol, balance);

                        ERC20Token erc20Token = new ERC20Token(address, tokenDefinition.name, tokenDefinition.symbol, balance, ethBalance);

                        Log.i(TAG, "get token success: " + erc20Token.toString());

                        tokens.add(erc20Token);

                    }else{

                        Log.e(TAG, "failed token balance call - " + balanceResponse.message());

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Log.i(TAG, "saving tokens...");
            tokenDao.save(tokens);

        });

    }

    @WorkerThread
    private double getEthBalance(String symbol, double value) {

        try {

            String pair = symbol.toUpperCase() + ETH_PAIR_SUFFIX;

            Call<Market> rateCall = shapeShiftService.getMarket(pair);
            Response<Market> rateResponse = rateCall.execute();

            if(rateResponse.isSuccessful() && rateResponse.body() != null){

                Market market = rateResponse.body();
                Log.i(TAG, "market success: " + market.toString());
                return market.getRate() * value;

            }else{

                Log.e(TAG, "market failed for " + symbol + ": " + rateResponse.message());

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;

    }

    private boolean isAnyOld(List<ERC20Token> tokens){

        for(ERC20Token token : tokens){

            if(System.currentTimeMillis() - token.updated > MAX_CACHE_AGE_MILLIS){

                return true;

            }
        }

        return false;

    }

    /**
     * get a list of currency pairs that are supported by the exchange.
     * @return list of valid pairs
     */
    @WorkerThread
    private List<String> getValidPairs() {

        List<String> result = new ArrayList<>();

        try {

            Response<List<String>> validPairsResponse = shapeShiftService.getValidPairs().execute();

            if(validPairsResponse.isSuccessful() && validPairsResponse.body() != null){

                result = validPairsResponse.body();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;

    }

    private class TokenDefinition{

        public String name;
        public String symbol;
        public String contract;

        public TokenDefinition(String name, String symbol, String contract) {
            this.name = name;
            this.symbol = symbol;
            this.contract = contract;
        }
    }

}
