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


import java.io.IOException;

/**
 * Meta details for pagination.
 */

public class Meta {
  public static final String SERIALIZED_NAME_TOTAL = "total";

  private Integer total;

  public static final String SERIALIZED_NAME_PAGE = "page";

  private Integer page = 1;

  public static final String SERIALIZED_NAME_LIMIT = "limit";

  private Integer limit = 10;

  public Meta() {
  }

  public Meta total(Integer total) {

    this.total = total;
    return this;
  }

   /**
   * Shows the total amount of items of this kind that exist.
   * minimum: 0
   * @return total
  **/


  public Integer getTotal() {
    return total;
  }


  public void setTotal(Integer total) {
    this.total = total;
  }


  public Meta page(Integer page) {

    this.page = page;
    return this;
  }

   /**
   * A page denotes an amount of items, offset from the first item. Each page holds an amount of items equal to the &#x60;limit&#x60;.
   * minimum: 1
   * @return page
  **/


  public Integer getPage() {
    return page;
  }


  public void setPage(Integer page) {
    this.page = page;
  }


  public Meta limit(Integer limit) {

    this.limit = limit;
    return this;
  }

   /**
   * The amount of items in each page. Limits how many items can be fetched at once.
   * minimum: 1
   * maximum: 20
   * @return limit
  **/


  public Integer getLimit() {
    return limit;
  }


  public void setLimit(Integer limit) {
    this.limit = limit;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Meta meta = (Meta) o;
    return Objects.equals(this.total, meta.total) &&
        Objects.equals(this.page, meta.page) &&
        Objects.equals(this.limit, meta.limit);
  }

  @Override
  public int hashCode() {
    return Objects.hash(total, page, limit);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Meta {\n");
    sb.append("    total: ").append(toIndentedString(total)).append("\n");
    sb.append("    page: ").append(toIndentedString(page)).append("\n");
    sb.append("    limit: ").append(toIndentedString(limit)).append("\n");
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

