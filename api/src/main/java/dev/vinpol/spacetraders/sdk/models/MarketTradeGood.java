/*
 * SpaceTraders API
 * SpaceTraders is an open-universe game and learning platform that offers a set of HTTP endpoints to control a fleet of ships and explore a multiplayer universe.  The API is documented using [OpenAPI](https://github.com/SpaceTradersAPI/api-docs). You can send your first request right here in your browser to check the status of the game server.  ```json http {   \"method\": \"GET\",   \"url\": \"https://api.spacetraders.io/v2\", } ```  Unlike a traditional game, SpaceTraders does not have a first-party client or app to play the game. Instead, you can use the API to build your own client, write a script to automate your ships, or try an app built by the community.  We have a [Discord channel](https://discord.com/invite/jh6zurdWk5) where you can share your projects, ask questions, and get help from other players.
 *
 * The version of the OpenAPI document: 2.0.0
 * Contact: joel@spacetraders.io
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package dev.vinpol.spacetraders.sdk.models;

import java.util.Objects;
import java.util.Arrays;


import dev.vinpol.spacetraders.sdk.models.ActivityLevel;
import dev.vinpol.spacetraders.sdk.models.SupplyLevel;
import dev.vinpol.spacetraders.sdk.models.TradeSymbol;
import java.io.IOException;

/**
 * MarketTradeGood
 */

public class MarketTradeGood {
  public static final String SERIALIZED_NAME_SYMBOL = "symbol";

  private TradeSymbol symbol;

  /**
   * The type of trade good (export, import, or exchange).
   */

  public enum TypeEnum {
    EXPORT("EXPORT"),

    IMPORT("IMPORT"),

    EXCHANGE("EXCHANGE");

    private String value;

    TypeEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

  }

  public static final String SERIALIZED_NAME_TYPE = "type";

  private TypeEnum type;

  public static final String SERIALIZED_NAME_TRADE_VOLUME = "tradeVolume";

  private Integer tradeVolume;

  public static final String SERIALIZED_NAME_SUPPLY = "supply";

  private SupplyLevel supply;

  public static final String SERIALIZED_NAME_ACTIVITY = "activity";

  private ActivityLevel activity;

  public static final String SERIALIZED_NAME_PURCHASE_PRICE = "purchasePrice";

  private Integer purchasePrice;

  public static final String SERIALIZED_NAME_SELL_PRICE = "sellPrice";

  private Integer sellPrice;

  public MarketTradeGood() {
  }

  public MarketTradeGood symbol(TradeSymbol symbol) {

    this.symbol = symbol;
    return this;
  }

   /**
   * Get symbol
   * @return symbol
  **/


  public TradeSymbol getSymbol() {
    return symbol;
  }


  public void setSymbol(TradeSymbol symbol) {
    this.symbol = symbol;
  }


  public MarketTradeGood type(TypeEnum type) {

    this.type = type;
    return this;
  }

   /**
   * The type of trade good (export, import, or exchange).
   * @return type
  **/


  public TypeEnum getType() {
    return type;
  }


  public void setType(TypeEnum type) {
    this.type = type;
  }


  public MarketTradeGood tradeVolume(Integer tradeVolume) {

    this.tradeVolume = tradeVolume;
    return this;
  }

   /**
   * This is the maximum number of units that can be purchased or sold at this market in a single trade for this good. Trade volume also gives an indication of price volatility. A market with a low trade volume will have large price swings, while high trade volume will be more resilient to price changes.
   * minimum: 1
   * @return tradeVolume
  **/


  public Integer getTradeVolume() {
    return tradeVolume;
  }


  public void setTradeVolume(Integer tradeVolume) {
    this.tradeVolume = tradeVolume;
  }


  public MarketTradeGood supply(SupplyLevel supply) {

    this.supply = supply;
    return this;
  }

   /**
   * Get supply
   * @return supply
  **/


  public SupplyLevel getSupply() {
    return supply;
  }


  public void setSupply(SupplyLevel supply) {
    this.supply = supply;
  }


  public MarketTradeGood activity(ActivityLevel activity) {

    this.activity = activity;
    return this;
  }

   /**
   * Get activity
   * @return activity
  **/


  public ActivityLevel getActivity() {
    return activity;
  }


  public void setActivity(ActivityLevel activity) {
    this.activity = activity;
  }


  public MarketTradeGood purchasePrice(Integer purchasePrice) {

    this.purchasePrice = purchasePrice;
    return this;
  }

   /**
   * The price at which this good can be purchased from the market.
   * minimum: 0
   * @return purchasePrice
  **/


  public Integer getPurchasePrice() {
    return purchasePrice;
  }


  public void setPurchasePrice(Integer purchasePrice) {
    this.purchasePrice = purchasePrice;
  }


  public MarketTradeGood sellPrice(Integer sellPrice) {

    this.sellPrice = sellPrice;
    return this;
  }

   /**
   * The price at which this good can be sold to the market.
   * minimum: 0
   * @return sellPrice
  **/


  public Integer getSellPrice() {
    return sellPrice;
  }


  public void setSellPrice(Integer sellPrice) {
    this.sellPrice = sellPrice;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MarketTradeGood marketTradeGood = (MarketTradeGood) o;
    return Objects.equals(this.symbol, marketTradeGood.symbol) &&
        Objects.equals(this.type, marketTradeGood.type) &&
        Objects.equals(this.tradeVolume, marketTradeGood.tradeVolume) &&
        Objects.equals(this.supply, marketTradeGood.supply) &&
        Objects.equals(this.activity, marketTradeGood.activity) &&
        Objects.equals(this.purchasePrice, marketTradeGood.purchasePrice) &&
        Objects.equals(this.sellPrice, marketTradeGood.sellPrice);
  }

  @Override
  public int hashCode() {
    return Objects.hash(symbol, type, tradeVolume, supply, activity, purchasePrice, sellPrice);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MarketTradeGood {\n");
    sb.append("    symbol: ").append(toIndentedString(symbol)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    tradeVolume: ").append(toIndentedString(tradeVolume)).append("\n");
    sb.append("    supply: ").append(toIndentedString(supply)).append("\n");
    sb.append("    activity: ").append(toIndentedString(activity)).append("\n");
    sb.append("    purchasePrice: ").append(toIndentedString(purchasePrice)).append("\n");
    sb.append("    sellPrice: ").append(toIndentedString(sellPrice)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}

