package uk.co.wardone.beaker;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;

import uk.co.wardone.beaker.modal.api.blockexplorer.etherscan.EtherscanService;
import uk.co.wardone.beaker.modal.api.blockexplorer.etherscan.data.Balance;

import static org.hamcrest.CoreMatchers.is;

public class EtherscanServiceTest {

    @Test
    public void testGetBalance() throws IOException {

        EtherscanService etherscanService = EtherscanService.get();

        Balance balance = etherscanService.getBalance(EtherscanService.TEST_API_KEY, EtherscanService.TEST_ADDRESS).execute().body();

        Assert.assertNotNull(balance);
        Assert.assertThat(balance.getMessage(), is("OK"));
        Assert.assertNotSame(new BigDecimal(balance.getResult()), new BigDecimal(0));


    }

}
