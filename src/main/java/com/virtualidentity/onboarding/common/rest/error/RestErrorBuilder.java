package com.virtualidentity.onboarding.common.rest.error;

import com.virtualidentity.onboarding.generated.model.Error;
import java.util.UUID;

public class RestErrorBuilder {

  public Error buildError(Exception e, RestErrorCode errorCode) {
    return new Error()
        .id(UUID.randomUUID().toString())
        .code(errorCode.getErrorCode())
        .message(e.getMessage());
  }

}
