package uk.co.wardone.beaker.model.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TokenBalance {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "address")
    public String address;

    @ColumnInfo(name = "balance")
    public double balance;

    @ColumnInfo(name = "total_tokens")
    public int totalTokens;

    @ColumnInfo(name = "updated")
    public long updated;

    public TokenBalance(@NonNull String address, double balance, int totalTokens) {

        this.address = address;
        this.balance = balance;
        this.totalTokens = totalTokens;
        this.updated = System.currentTimeMillis();

    }

    @Override
    public String toString() {
        return "ERC20Balance{" +
                "address='" + address + '\'' +
                ", balance=" + balance +
                ", totalTokens=" + totalTokens +
                ", updated=" + updated +
                '}';
    }
}
