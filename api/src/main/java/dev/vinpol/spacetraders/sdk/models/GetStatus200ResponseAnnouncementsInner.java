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
 * GetStatus200ResponseAnnouncementsInner
 */

public class GetStatus200ResponseAnnouncementsInner {
  public static final String SERIALIZED_NAME_TITLE = "title";
  
  private String title;

  public static final String SERIALIZED_NAME_BODY = "body";
  
  private String body;

  public GetStatus200ResponseAnnouncementsInner() {
  }

  public GetStatus200ResponseAnnouncementsInner title(String title) {

    this.title = title;
    return this;
  }

   /**
   * Get title
   * @return title
  **/
  

  public String getTitle() {
    return title;
  }


  public void setTitle(String title) {
    this.title = title;
  }


  public GetStatus200ResponseAnnouncementsInner body(String body) {

    this.body = body;
    return this;
  }

   /**
   * Get body
   * @return body
  **/
  

  public String getBody() {
    return body;
  }


  public void setBody(String body) {
    this.body = body;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetStatus200ResponseAnnouncementsInner getStatus200ResponseAnnouncementsInner = (GetStatus200ResponseAnnouncementsInner) o;
    return Objects.equals(this.title, getStatus200ResponseAnnouncementsInner.title) &&
        Objects.equals(this.body, getStatus200ResponseAnnouncementsInner.body);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, body);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetStatus200ResponseAnnouncementsInner {\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    body: ").append(toIndentedString(body)).append("\n");
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
