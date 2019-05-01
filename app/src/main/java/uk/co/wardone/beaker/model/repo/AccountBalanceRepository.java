package uk.co.wardone.beaker.model.repo;

import android.util.Log;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;

import retrofit2.Call;
import retrofit2.Response;
import uk.co.wardone.beaker.model.api.blockexplorer.etherscan.EtherscanService;
import uk.co.wardone.beaker.model.api.blockexplorer.etherscan.data.Balance;
import uk.co.wardone.beaker.model.api.exchange.shapeshift.ShapeShiftService;
import uk.co.wardone.beaker.model.api.exchange.shapeshift.data.Market;
import uk.co.wardone.beaker.model.data.cache.AccountBalanceDao;
import uk.co.wardone.beaker.model.data.model.AccountBalance;
import uk.co.wardone.beaker.model.data.AppDatabase;

public class AccountBalanceRepository extends BaseRepository<String, AccountBalance>{

    private static final String TAG = "AccountBalanceRepository";
    private static final String ETH_BTC_PAIR = "ETH_BTC";

    private static AccountBalanceRepository instance;

    private EtherscanService etherscanService;
    private ShapeShiftService shapeShiftService;
    private AccountBalanceDao accountBalanceDao;

    public static AccountBalanceRepository getInstance(AppDatabase appDatabase){

        if(instance == null){

            instance = new AccountBalanceRepository(appDatabase);

        }

        return instance;

    }

    public AccountBalanceRepository(AppDatabase appDatabase){

        super(appDatabase);
        this.etherscanService = EtherscanService.get();
        this.shapeShiftService = ShapeShiftService.get();
        this.accountBalanceDao = appDatabase.getBalanceDao();

    }

    /**
     * get latest account data from cache, if the data is old or doesn't exist refresh it.
     * @param address - account address
     * @return latest account data wrapped in LiveData
     */
    @Override
    public LiveData<AccountBalance> get(String address) {

        LiveData<AccountBalance> balanceLiveData = accountBalanceDao.getBalance(address);

        if(balanceLiveData.getValue() == null || System.currentTimeMillis() - balanceLiveData.getValue().updated > MAX_CACHE_AGE_MILLIS){

            refresh(address);

        }

        return balanceLiveData;

    }

    @Override
    public AccountBalance getSync(String address) {

        AccountBalance accountBalance = accountBalanceDao.getBalanceSync(address);

        if(accountBalance == null || System.currentTimeMillis() - accountBalance.updated > MAX_CACHE_AGE_MILLIS){

            refresh(address);

        }

        return accountBalance;

    }

    /**
     * request the latest account data via retrofit and update the local database cache, LiveData
     * will propagate changes to view. Raw values returned by API may be / end up being too big for
     * java so us BigDecimal and divide by the base unit to get ETH.
     */
    @Override
    public void refresh(String address) {

        executorService.submit(() -> {

            Call<Balance> balanceCall = etherscanService.getBalance(EtherscanService.TEST_API_KEY, EtherscanService.TEST_ADDRESS);

            try {

                Response<Balance> balanceResponse = balanceCall.execute();

                if(balanceResponse.isSuccessful() && balanceResponse.body() != null){

                    Balance balance = balanceResponse.body();
                    BigDecimal raw = new BigDecimal(balance.getResult()).setScale(21, RoundingMode.FLOOR);
                    BigDecimal result = raw.divide(new BigDecimal(DIVISOR), RoundingMode.FLOOR);
                    double ethBalance = result.doubleValue();
                    double btcBalance = getBtcBalance(ethBalance);

                    AccountBalance accountBalance = new AccountBalance(address, ethBalance, btcBalance);

                    Log.i(TAG, "get balance success: " + accountBalance.toString());
                    accountBalanceDao.save(accountBalance);

                }else{

                    Log.e(TAG, "failed account balance call - " + balanceResponse.message());

                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        });

    }

    @WorkerThread
    private double getBtcBalance(double ethBalance) {

        double result = -1.0D;

        try {

            Call<Market> marketCall = shapeShiftService.getMarket(ETH_BTC_PAIR);
            Response<Market> marketResponse = marketCall.execute();

            if(marketResponse.isSuccessful() && marketResponse.body() != null){

                result = marketResponse.body().getRate() * ethBalance;

            }

        }catch (IOException e) {
            e.printStackTrace();
        }

        return result;

    }
}
