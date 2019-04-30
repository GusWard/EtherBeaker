package uk.co.wardone.beaker;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.customview.widget.ViewDragHelper;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.wardone.beaker.modal.data.model.ERC20Token;
import uk.co.wardone.beaker.viewmodal.BalanceViewModal;
import uk.co.wardone.beaker.viewmodal.TokensViewModel;

public class TokenFragment extends ButterknifeFragment {

    @BindView(R.id.list)
    RecyclerView listView;

    private TokensViewModel tokensViewModel;
    private TokenAdapter tokenAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tokensViewModel = ViewModelProviders.of(this).get(TokensViewModel.class);
        observeTokens(tokensViewModel);

    }

    @Override
    public View inflate(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_tokens, container, false);

    }

    @Override
    public void onViewBound(View view) {

        tokenAdapter = new TokenAdapter();
        listView.setLayoutManager(new LinearLayoutManager(getActivity()));
        listView.setAdapter(tokenAdapter);
        listView.setAlpha(0);
        listView.animate().alpha(1).start();

    }

    private void observeTokens(TokensViewModel tokensViewModel) {

        tokensViewModel.getTokens().observe(this, erc20Tokens -> {

            Collections.sort(erc20Tokens, (t1, t2) -> {

                if(t1.ethBalance < t2.ethBalance){

                    return 1;

                }else if(t1.ethBalance > t2.ethBalance){

                    return -1;

                }else{

                    return 0;

                }
            });

            tokenAdapter.setTokens(erc20Tokens);
            tokenAdapter.notifyDataSetChanged();

        });
    }

    public class TokenViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.token_balance)
        TextView tokenBalance;
        @BindView(R.id.eth_balance)
        TextView ethBalance;

        public TokenViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

        }
    }

    private class TokenAdapter extends RecyclerView.Adapter<TokenViewHolder>{

        private List<ERC20Token> tokens = new ArrayList<>();

        public void setTokens(List<ERC20Token> tokens) {
            this.tokens = tokens;
        }

        @NonNull
        @Override
        public TokenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            return new TokenViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_tokens_item, parent, false));

        }

        @SuppressLint("DefaultLocale")
        @Override
        public void onBindViewHolder(@NonNull TokenViewHolder holder, int position) {

            ERC20Token erc20Token = tokens.get(position);

            holder.title.setText(erc20Token.name);
            holder.tokenBalance.setText(String.format("%.4f %s", erc20Token.balance, erc20Token.symbol.toUpperCase()));
            holder.ethBalance.setText(String.format("%.4f ETH", erc20Token.ethBalance));

        }

        @Override
        public int getItemCount() {

            return tokens.size();

        }
    }
}
