package uk.co.wardone.beaker;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.wardone.beaker.viewmodel.TokensViewModel;
import uk.co.wardone.beaker.viewmodel.data.TokensViewData;

public class TokenFragment extends ButterKnifeFragment {

    @BindView(R.id.list)
    RecyclerView listView;

    private TokensViewModel tokensViewModel;
    private TokenAdapter tokenAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tokensViewModel = ViewModelProviders.of(this).get(TokensViewModel.class);
        tokensViewModel.getTokensViewData().observe(this, tokensViewData -> {

            tokenAdapter.setTokens(tokensViewData.tokens);
            tokenAdapter.notifyDataSetChanged();

        });

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

        private List<TokensViewData.TokenItem> tokens = new ArrayList<>();

        public void setTokens(List<TokensViewData.TokenItem> tokens) {
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

            TokensViewData.TokenItem tokenItem = tokens.get(position);

            holder.title.setText(tokenItem.name);
            holder.tokenBalance.setText(String.format("%.4f %s", tokenItem.balance, tokenItem.symbol.toUpperCase()));
            holder.ethBalance.setText(String.format("%.4f ETH", tokenItem.ethBalance));

        }

        @Override
        public int getItemCount() {

            return tokens.size();

        }
    }
}
