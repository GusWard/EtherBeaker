package uk.co.wardone.beaker.model.api.blockexplorer.etherscan;

import com.github.aurae.retrofit2.LoganSquareConverterFactory;

import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;
import uk.co.wardone.beaker.model.api.blockexplorer.etherscan.data.Balance;
import uk.co.wardone.beaker.model.api.blockexplorer.etherscan.data.ERC20Balance;

public interface EtherscanService {

    @GET("api?module=account&action=balance&tag=latest")
    Call<Balance> getBalance(@Query("apikey") String apiKey, @Query("address") String address);

    @GET("api?module=account&action=tokenbalance&tag=latest")
    Call<ERC20Balance> getTokenBalance(@Query("apikey") String apiKey, @Query("address") String address, @Query("contractaddress") String contractAddress);

    static EtherscanService get(){

        return new Retrofit.Builder()
                .baseUrl("https://api.etherscan.io/")
                .addConverterFactory(LoganSquareConverterFactory.create())
                .build()
                .create(EtherscanService.class);

    }

    static EtherscanService getWithExecutor(Executor callbackExecutor){

        return new Retrofit.Builder()
                .baseUrl("https://api.etherscan.io/")
                .addConverterFactory(LoganSquareConverterFactory.create())
                .callbackExecutor(callbackExecutor)
                .build()
                .create(EtherscanService.class);

    }

}
