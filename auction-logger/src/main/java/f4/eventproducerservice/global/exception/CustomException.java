package f4.eventproducerservice.global.exception;

import f4.eventproducerservice.global.constant.CustomErrorCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

  private final CustomErrorCode customErrorCode;

  public CustomException(CustomErrorCode customErrorCode) {
    super(customErrorCode.getMessage());
    this.customErrorCode = customErrorCode;
  }
}
