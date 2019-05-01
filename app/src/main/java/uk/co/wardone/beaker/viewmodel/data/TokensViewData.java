package uk.co.wardone.beaker.viewmodel.data;

import java.util.ArrayList;
import java.util.List;

public class TokensViewData {

    public List<TokenItem> tokens = new ArrayList<>();

    public List<TokenItem> getTokens() {
        return tokens;
    }

    public void setTokens(List<TokenItem> tokens) {
        this.tokens = tokens;
    }

    public static class TokenItem{

        public String name;

        public String symbol;

        public double balance;

        public double ethBalance;

        public TokenItem(String name, String symbol, double balance, double ethBalance) {

            this.name = name;
            this.symbol = symbol;
            this.balance = balance;
            this.ethBalance = ethBalance;

        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }

        public double getEthBalance() {
            return ethBalance;
        }

        public void setEthBalance(double ethBalance) {
            this.ethBalance = ethBalance;
        }
    }

}
