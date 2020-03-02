package com.virtualidentity.backendonboarding.test.rest;

import com.virtualidentity.backendonboarding.common.rest.controller.BaseController;
import com.virtualidentity.backendonboarding.generated.TestApi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class TestController extends BaseController implements TestApi {

  @Override
  public ResponseEntity<String> testGet() throws Exception {
    return new ResponseEntity<>("Hello VI!", HttpStatus.OK);
  }
}
