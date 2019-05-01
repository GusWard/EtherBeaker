package uk.co.wardone.beaker.viewmodel.data;

public class BalanceViewData {

    public float ethBalance;

    public float btcBalance;

    public float aggregateTokenBalance;

    public int totalTokens;

    public float getEthBalance() {
        return ethBalance;
    }

    public void setEthBalance(float ethBalance) {
        this.ethBalance = ethBalance;
    }

    public float getBtcBalance() {
        return btcBalance;
    }

    public void setBtcBalance(float btcBalance) {
        this.btcBalance = btcBalance;
    }

    public float getAggregateTokenBalance() {
        return aggregateTokenBalance;
    }

    public void setAggregateTokenBalance(float aggregateTokenBalance) {
        this.aggregateTokenBalance = aggregateTokenBalance;
    }

    public int getTotalTokens() {
        return totalTokens;
    }

    public void setTotalTokens(int totalTokens) {
        this.totalTokens = totalTokens;
    }

    public static BalanceViewData from(BalanceViewData balanceViewData){

        BalanceViewData newBalanceViewData = new BalanceViewData();
        newBalanceViewData.ethBalance = balanceViewData.ethBalance;
        newBalanceViewData.btcBalance = balanceViewData.btcBalance;
        newBalanceViewData.aggregateTokenBalance = balanceViewData.aggregateTokenBalance;
        newBalanceViewData.totalTokens = balanceViewData.totalTokens;

        return newBalanceViewData;

    }

}
