package uk.co.wardone.beaker;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;

import uk.co.wardone.beaker.model.api.blockexplorer.etherscan.EtherscanService;
import uk.co.wardone.beaker.model.api.blockexplorer.etherscan.data.Balance;

import static org.hamcrest.CoreMatchers.is;

public class EtherscanServiceTest {

    @Test
    public void testGetBalance() throws IOException {

        EtherscanService etherscanService = EtherscanService.get();

        Balance balance = etherscanService.getBalance(BuildConfig.ETHERSCAN_TEST_API_KEY, BuildConfig.TEST_ETHEREUM_WALLET_ADDRESS).execute().body();

        Assert.assertNotNull(balance);
        Assert.assertThat(balance.getMessage(), is("OK"));
        Assert.assertNotSame(new BigDecimal(balance.getResult()), new BigDecimal(0));


    }

}
