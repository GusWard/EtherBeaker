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
import androidx.lifecycle.ViewModelProviders;

import butterknife.BindView;
import uk.co.wardone.beaker.viewmodel.BalanceViewModel;
import uk.co.wardone.beaker.viewmodel.data.BalanceViewData;

public class BalanceFragment extends ButterKnifeFragment {

    private static final String ETH_SUFFIX = "ETH";
    private static final String BTC_SUFFIX = "BTC";

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

    private BalanceViewModel accountViewModel;
    private BalanceViewData currentBalanceViewData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentBalanceViewData = new BalanceViewData();

        accountViewModel = ViewModelProviders.of(this).get(BalanceViewModel.class);
        accountViewModel.getBalanceViewData().observe(this, balanceViewData -> {

            refresh(balanceViewData);
            /* create new instance so we are not comparing LiveData instance with itself */
            currentBalanceViewData = BalanceViewData.from(balanceViewData);

        });

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
            accountViewModel.refreshAccountBalance();

        });

        refreshTokenBalanceView.setOnClickListener(v -> {

            refreshTokenBalanceView.setClickable(false);
            refreshTokenBalanceView.setRotation(0);
            refreshTokenBalanceView.animate()
                    .rotation(360)
                    .setInterpolator(new DecelerateInterpolator())
                    .withEndAction(() -> refreshTokenBalanceView.setClickable(true))
                    .start();
            accountViewModel.refreshTokenBalance();

        });
    }

    private void refresh(BalanceViewData balanceViewData) {

        updateEtherBalance(balanceViewData);
        updateBtcBalance(balanceViewData);
        updateTokenBalance(balanceViewData);
        updateTokenTotal(balanceViewData);

    }

    @SuppressLint("DefaultLocale")
    private void updateEtherBalance(BalanceViewData balanceViewData) {

        if(currentBalanceViewData.ethBalance != balanceViewData.ethBalance){

            ValueAnimator ethValueAnimator = ValueAnimator.ofFloat(currentBalanceViewData.ethBalance, balanceViewData.ethBalance);
            ethValueAnimator.addUpdateListener(va -> ethBalanceTextView.setText(String.format("%.4f %s", (float)va.getAnimatedValue(), ETH_SUFFIX)));
            ethValueAnimator.setDuration(500);
            ethValueAnimator.setInterpolator(new DecelerateInterpolator());
            ethValueAnimator.start();

        }

    }

    @SuppressLint("DefaultLocale")
    private void updateBtcBalance(BalanceViewData balanceViewData) {

        if(currentBalanceViewData.btcBalance != balanceViewData.btcBalance){

            ValueAnimator btcValueAnimator = ValueAnimator.ofFloat(currentBalanceViewData.btcBalance, balanceViewData.btcBalance);
            btcValueAnimator.addUpdateListener(va -> btcBalanceTextView.setText(String.format("%.4f %s", (float)va.getAnimatedValue(), BTC_SUFFIX)));
            btcValueAnimator.setDuration(500);
            btcValueAnimator.setInterpolator(new DecelerateInterpolator());
            btcValueAnimator.start();

        }

    }

    @SuppressLint("DefaultLocale")
    private void updateTokenBalance(BalanceViewData balanceViewData) {

        if(currentBalanceViewData.aggregateTokenBalance != balanceViewData.aggregateTokenBalance){

            ValueAnimator valueAnimator = ValueAnimator.ofFloat(currentBalanceViewData.aggregateTokenBalance, balanceViewData.aggregateTokenBalance);
            valueAnimator.addUpdateListener(va -> tokenBalanceTextView.setText(String.format("%.4f %s", (float)va.getAnimatedValue(), ETH_SUFFIX)));
            valueAnimator.setDuration(500);
            valueAnimator.setInterpolator(new DecelerateInterpolator());
            valueAnimator.start();

        }

    }

    @SuppressLint("DefaultLocale")
    private void updateTokenTotal(BalanceViewData balanceViewData) {

        if(currentBalanceViewData.totalTokens != balanceViewData.totalTokens){

            ValueAnimator totalValueAnimator = ValueAnimator.ofInt(currentBalanceViewData.totalTokens, balanceViewData.totalTokens);
            totalValueAnimator.addUpdateListener(va -> totalTokenCountView.setText(String.format("Across %d tokens", (int)va.getAnimatedValue())));
            totalValueAnimator.setDuration(500);
            totalValueAnimator.setInterpolator(new DecelerateInterpolator());
            totalValueAnimator.start();

        }
    }
}
