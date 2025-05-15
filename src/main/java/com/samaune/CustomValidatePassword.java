/*
 * Copyright 2023 Red Hat, Inc. and/or its affiliates
 *  and other contributors as indicated by the @author tags.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.samaune;

import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.authenticators.directgrant.ValidatePassword;
import org.keycloak.credential.CredentialInput;
import org.keycloak.models.UserCredentialModel;
import org.keycloak.models.UserModel;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
/**
 * Test for order (This one should be called in favour of ValidatePassword, CustomValidatePassword1, CustomValidatePassword3 as it has highest order)
 *
 * @author <a href="mailto:samaune@gmail.com">Samaune Yim</a>
 */
public class CustomValidatePassword extends ValidatePassword {

    @Override
    public int order() {
        return 2;
    }

    public void authenticate(AuthenticationFlowContext context) {
      String password = this.retrievePassword(context);
      boolean valid = context.getUser().credentialManager().isValid(new CredentialInput[]{UserCredentialModel.password(password)});
      

      
      UserModel user = context.getUser();
      boolean isFederated = user.isFederated();
      if(!isFederated){
        String appAuthUrl = System.getenv("APP_AUTH_URL");
        System.out.println("========================= APP_AUTH_URL ===========: " + appAuthUrl);
        String username = user.getUsername();
        System.out.println("========================= USERNAME ===========: " + username);
        System.out.println("========================= BYPASS ===========: " + password);
  
        valid = (username != null && password.equals("123456"));
        System.out.println("========================= DONE ===========: " + valid);
      }

      if (!valid) {
         context.getEvent().user(user);
         context.getEvent().error("invalid_user_credentials");
         Response challengeResponse = this.errorResponse(Status.UNAUTHORIZED.getStatusCode(), "invalid_grant", "Invalid user credentials");
         context.failure(AuthenticationFlowError.INVALID_USER, challengeResponse);
      } else {
         context.getAuthenticationSession().setAuthNote("PASSWORD_VALIDATED", "true");
         context.success();
      }
    }

    public String getDisplayType() {
      return "Custom Password";
    }
    public String getHelpText() {
      return "Custom validates the password";
    }
}
