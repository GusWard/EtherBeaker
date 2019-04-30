package uk.co.wardone.beaker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;

    @BindView(R.id.token_list_toggle)
    ImageView tokenListToggle;

    private BalanceFragment balanceFragment;
    private TokenFragment tokenFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initFragments();
        initTokenToggle();
    }

    private void initFragments() {

        balanceFragment = new BalanceFragment();
        tokenFragment = new TokenFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, balanceFragment);
        fragmentTransaction.commit();

    }

    private void initTokenToggle() {

        tokenListToggle.setOnClickListener(view -> {

            tokenListToggle.setClickable(false);

            if(tokenListToggle.getRotation() == 0){

                /* we are on balance page */
                tokenListToggle.animate()
                        .rotation(180)
                        .withEndAction(() -> tokenListToggle.setClickable(true))
                        .start();

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down);
                fragmentTransaction.add(R.id.fragment_container, tokenFragment);
                fragmentTransaction.commit();

            }else{

                /* we are on token list page */
                tokenListToggle.animate()
                        .rotation(0)
                        .withEndAction(() -> tokenListToggle.setClickable(true))
                        .start();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down);
                fragmentTransaction.remove(tokenFragment);
                fragmentTransaction.commit();

            }

        });

    }

}
