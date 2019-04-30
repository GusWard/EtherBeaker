package uk.co.wardone.beaker;

import android.content.Context;

import com.jraska.livedata.TestObserver;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.runner.AndroidJUnit4;
import uk.co.wardone.beaker.modal.api.blockexplorer.etherscan.EtherscanService;
import uk.co.wardone.beaker.modal.data.cache.AccountBalanceDao;
import uk.co.wardone.beaker.modal.data.model.AccountBalance;
import uk.co.wardone.beaker.modal.data.AppDatabase;
import uk.co.wardone.beaker.modal.repo.AccountBalanceRepository;

import static org.hamcrest.CoreMatchers.is;

@RunWith(AndroidJUnit4.class)
public class AccountAccountBalanceRepositoryTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private AccountBalanceRepository accountBalanceRepository;
    private AccountBalanceDao balanceCache;
    private AppDatabase appDatabase;

    @Before
    public void createDb() {

        Context context = ApplicationProvider.getApplicationContext();
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        balanceCache = appDatabase.getBalanceDao();
        accountBalanceRepository = new AccountBalanceRepository(appDatabase);

    }

    @After
    public void closeDb() throws IOException {

        appDatabase.close();

    }

    @Test
    public void getInitialValue() throws InterruptedException {

        LiveData<AccountBalance> accountLiveData = accountBalanceRepository.get(EtherscanService.TEST_ADDRESS);

        TestObserver.test(accountLiveData)
                .awaitValue()
                .assertHasValue()
                .assertValue(input -> input.address.equals(EtherscanService.TEST_ADDRESS));

    }

    @Test
    public void testGetAccount(){

        LiveData<AccountBalance> accountLiveData = accountBalanceRepository.get(EtherscanService.TEST_ADDRESS);
//        Assert.ass

    }

}
