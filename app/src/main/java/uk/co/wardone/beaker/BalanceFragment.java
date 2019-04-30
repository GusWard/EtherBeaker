package uk.co.wardone.beaker;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import butterknife.BindView;
import uk.co.wardone.beaker.modal.data.model.AccountBalance;
import uk.co.wardone.beaker.modal.data.model.TokenBalance;
import uk.co.wardone.beaker.viewmodal.BalanceViewModal;

public class BalanceFragment extends ButterknifeFragment {

    private static final String ETH_SUFFIX = " ETH";
    private static final String BTC_SUFFIX = " BTC";

    @BindView(R.id.eth_balance_text_view)
    TextView ethBalanceTextView;
    @BindView(R.id.btc_balance_text_view)
    TextView btcBalanceTextView;
    @BindView(R.id.token_balance_text_view)
    TextView tokenBalanceTextView;
    @BindView(R.id.token_total_count)
    TextView totalTokenCountView;

    @BindView(R.id.refresh_account_balance)
    ImageView refreshAccountBalanceView;

    @BindView(R.id.refresh_token_balance)
    ImageView refreshTokenBalanceView;

    private BalanceViewModal accountViewModal;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        accountViewModal = ViewModelProviders.of(this).get(BalanceViewModal.class);
        observeBalanceData(accountViewModal);
        observeTokenBalanceData(accountViewModal);

    }

    @Override
    public View inflate(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_balance, container, false);

    }

    @Override
    public void onViewBound(View view) {

        refreshAccountBalanceView.setOnClickListener(v -> {

            refreshAccountBalanceView.setClickable(false);
            refreshAccountBalanceView.setRotation(0);
            refreshAccountBalanceView.animate()
                    .rotation(360)
                    .setInterpolator(new DecelerateInterpolator())
                    .withEndAction(() -> refreshAccountBalanceView.setClickable(true))
                    .start();
            accountViewModal.refreshAccountBalance();

        });

        refreshTokenBalanceView.setOnClickListener(v -> {

            refreshTokenBalanceView.setClickable(false);
            refreshTokenBalanceView.setRotation(0);
            refreshTokenBalanceView.animate()
                    .rotation(360)
                    .setInterpolator(new DecelerateInterpolator())
                    .withEndAction(() -> refreshTokenBalanceView.setClickable(true))
                    .start();
            accountViewModal.refreshTokenBalance();

        });
    }

    private void observeBalanceData(BalanceViewModal accountViewModal) {

        LiveData<AccountBalance> balanceLiveData = accountViewModal.getBalanceLiveData();

        if(balanceLiveData != null){

            accountViewModal.getBalanceLiveData().observe(this, this::updateAccountBalance);

        }
    }

    @SuppressLint("DefaultLocale")
    private void updateAccountBalance(AccountBalance accountBalance) {

        float currentEthValue = Float.parseFloat(ethBalanceTextView.getText().toString().replaceAll("[^\\d.]", ""));

        ValueAnimator ethValueAnimator = ValueAnimator.ofFloat(currentEthValue, accountBalance == null ? 0 : (float) accountBalance.balance);
        ethValueAnimator.addUpdateListener(valueAnimator1 -> ethBalanceTextView.setText(String.format("%.4f" + ETH_SUFFIX, (float)valueAnimator1.getAnimatedValue())));
        ethValueAnimator.setDuration(500);
        ethValueAnimator.setInterpolator(new DecelerateInterpolator());
        ethValueAnimator.start();

        float currentBtcValue = Float.parseFloat(btcBalanceTextView.getText().toString().replaceAll("[^\\d.]", ""));

        ValueAnimator btcValueAnimator = ValueAnimator.ofFloat(currentBtcValue, accountBalance == null ? 0 : (float) accountBalance.btcBalance);
        btcValueAnimator.addUpdateListener(valueAnimator1 -> btcBalanceTextView.setText(String.format("%.4f" + BTC_SUFFIX, (float)valueAnimator1.getAnimatedValue())));
        btcValueAnimator.setDuration(500);
        btcValueAnimator.setInterpolator(new DecelerateInterpolator());
        btcValueAnimator.start();

    }

    private void observeTokenBalanceData(BalanceViewModal accountViewModal) {

        LiveData<TokenBalance> tokenBalanceLiveData = accountViewModal.getTokenBalance();

        if(tokenBalanceLiveData != null){

            accountViewModal.getTokenBalance().observe(this, this::updateTokenBalance);

        }
    }

    @SuppressLint("DefaultLocale")
    private void updateTokenBalance(TokenBalance tokenBalance) {

        float currentValue = Float.parseFloat(tokenBalanceTextView.getText().toString().replaceAll("[^\\d.]", ""));

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(currentValue, tokenBalance == null ? 0 : (float) tokenBalance.balance);
        valueAnimator.addUpdateListener(valueAnimator1 -> tokenBalanceTextView.setText(String.format("%.4f" + ETH_SUFFIX, (float)valueAnimator1.getAnimatedValue())));
        valueAnimator.setDuration(500);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.start();

        int currentTotalTokens = Integer.parseInt(totalTokenCountView.getText().toString().replaceAll("[^\\d.]", ""));

        ValueAnimator totalValueAnimator = ValueAnimator.ofInt(currentTotalTokens, tokenBalance == null ? 0 : tokenBalance.totalTokens);
        totalValueAnimator.addUpdateListener(valueAnimator1 -> totalTokenCountView.setText(String.format("Across %d tokens", (int)valueAnimator1.getAnimatedValue())));
        totalValueAnimator.setDuration(500);
        totalValueAnimator.setInterpolator(new DecelerateInterpolator());
        totalValueAnimator.start();

    }
}
