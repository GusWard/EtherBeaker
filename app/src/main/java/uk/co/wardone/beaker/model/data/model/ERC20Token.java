package uk.co.wardone.beaker.model.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Objects;

import static androidx.room.ForeignKey.CASCADE;

@Entity(indices = {@Index("address")},
        foreignKeys = @ForeignKey(entity = AccountBalance.class,
                parentColumns = "address",
                childColumns = "address",
                onDelete = CASCADE))
public class ERC20Token {

    @NonNull
    @PrimaryKey
    public String primaryKey;

    @NonNull
    @ColumnInfo(name = "address")
    public String address;

    @NonNull
    @ColumnInfo(name = "name")
    public String name;

    @NonNull
    @ColumnInfo(name = "symbol")
    public String symbol;

    @ColumnInfo(name = "balance")
    public double balance;

    @ColumnInfo(name = "eth_balance")
    public double ethBalance;

    @ColumnInfo(name = "updated")
    public long updated;

    public ERC20Token(@NonNull String address, @NonNull String name, @NonNull String symbol, double balance, double ethBalance) {

        this.balance = balance;
        this.ethBalance = ethBalance;
        this.primaryKey = address + "-" + name;
        this.address = address;
        this.name = name;
        this.symbol = symbol;
        this.updated = System.currentTimeMillis();

    }

    @Override
    public String toString() {
        return "ERC20Token{" +
                "primaryKey='" + primaryKey + '\'' +
                ", address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", symbol='" + symbol + '\'' +
                ", balance=" + balance +
                ", ethBalance=" + ethBalance +
                ", updated=" + updated +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ERC20Token that = (ERC20Token) o;
        return Double.compare(that.balance, balance) == 0 &&
                Double.compare(that.ethBalance, ethBalance) == 0 &&
                updated == that.updated &&
                primaryKey.equals(that.primaryKey) &&
                address.equals(that.address) &&
                name.equals(that.name) &&
                symbol.equals(that.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(primaryKey, address, name, symbol, balance, ethBalance, updated);
    }
}
