package uk.co.wardone.beaker.modal.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class AccountBalance {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "address")
    public String address;

    @ColumnInfo(name = "balance")
    public double balance;

    @ColumnInfo(name = "btc_balance")
    public double btcBalance;

    @ColumnInfo(name = "updated")
    public long updated;

    public AccountBalance(@NonNull String address, double balance, double btcBalance) {

        this.address = address;
        this.balance = balance;
        this.btcBalance = btcBalance;
        this.updated = System.currentTimeMillis();

    }

    @Override
    public String toString() {
        return "AccountBalance{" +
                "address='" + address + '\'' +
                ", balance=" + balance +
                ", updated=" + updated +
                '}';
    }
}
