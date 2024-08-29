package us.dot.its.jpo.ode.s3.depositor.models.imp;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.*;

/**
 * ClientRegistrationPendingResponse
 */

public class ClientRegistrationPendingResponse {

  private String deviceID;

  private String message;

  public ClientRegistrationPendingResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ClientRegistrationPendingResponse(String deviceID, String message) {
    this.deviceID = deviceID;
    this.message = message;
  }

  public ClientRegistrationPendingResponse deviceID(String deviceID) {
    this.deviceID = deviceID;
    return this;
  }

  /**
   * Get deviceID
   * 
   * @return deviceID
   */
  @Schema(name = "DeviceID", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("DeviceID")
  public String getDeviceID() {
    return deviceID;
  }

  public void setDeviceID(String deviceID) {
    this.deviceID = deviceID;
  }

  public ClientRegistrationPendingResponse message(String message) {
    this.message = message;
    return this;
  }

  /**
   * Get message
   * 
   * @return message
   */
  @Schema(name = "Message", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("Message")
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ClientRegistrationPendingResponse clientRegistrationPendingResponse = (ClientRegistrationPendingResponse) o;
    return Objects.equals(this.deviceID, clientRegistrationPendingResponse.deviceID)
        && Objects.equals(this.message, clientRegistrationPendingResponse.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(deviceID, message);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ClientRegistrationPendingResponse {\n");
    sb.append("    deviceID: ").append(toIndentedString(deviceID)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
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
