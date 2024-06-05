package dev.vinpol.spacetraders.sdk.models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Market {

    private String symbol;

    /**
     * The list of goods that are exported/you can buy from this market.
     **/
    private List<TradeGood> exports = new ArrayList<>();

    /**
     * The list of goods that are sought as imports/you can sell in this market.
     **/
    private List<TradeGood> imports = new ArrayList<>();
    /**
     * The list of goods that are bought and sold between agents at this market.
     **/
    private List<TradeGood> exchange = new ArrayList<>();

    /**
     * The list of recent transactions at this market. Visible only when a ship is present at the market.
     **/
    private List<MarketTransaction> transactions;

    /**
     * The list of goods that are traded at this market. Visible only when a ship is present at the market.
     **/
    private List<MarketTradeGood> tradeGoods;

    public Market symbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    public Market exports(List<TradeGood> exports) {
        this.exports = exports;
        return this;
    }

    public Market addExportsItem(TradeGood exportsItem) {
        if (this.exports == null) {
            this.exports = new ArrayList<>();
        }
        this.exports.add(exportsItem);
        return this;
    }


    public Market imports(List<TradeGood> imports) {
        this.imports = imports;
        return this;
    }

    public Market addImportsItem(TradeGood importsItem) {
        if (this.imports == null) {
            this.imports = new ArrayList<>();
        }

        this.imports.add(importsItem);
        return this;
    }

    public Market exchange(List<TradeGood> exchange) {
        this.exchange = exchange;
        return this;
    }

    public Market addExchangeItem(TradeGood exchangeItem) {
        if (this.exchange == null) {
            this.exchange = new ArrayList<>();
        }

        this.exchange.add(exchangeItem);
        return this;
    }

    public Market transactions(List<MarketTransaction> transactions) {
        this.transactions = transactions;
        return this;
    }

    public Market addTransactionsItem(MarketTransaction transactionsItem) {
        if (this.transactions == null) {
            this.transactions = new ArrayList<>();
        }

        this.transactions.add(transactionsItem);
        return this;
    }

    public Market tradeGoods(List<MarketTradeGood> tradeGoods) {
        this.tradeGoods = tradeGoods;
        return this;
    }

    public Market addTradeGoodsItem(MarketTradeGood tradeGoodsItem) {
        if (this.tradeGoods == null) {
            this.tradeGoods = new ArrayList<>();
        }

        this.tradeGoods.add(tradeGoodsItem);
        return this;
    }
}

