// Copyright 2007 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.enterprise.connector.sharepoint.wsclient.soap;

import com.google.enterprise.connector.sharepoint.client.SPConstants;
import com.google.enterprise.connector.sharepoint.client.SharepointClientContext;
import com.google.enterprise.connector.sharepoint.client.Util;
import com.google.enterprise.connector.sharepoint.generated.gsbulkauthorization.AuthDataPacket;
import com.google.enterprise.connector.sharepoint.generated.gsbulkauthorization.BulkAuthorization;
import com.google.enterprise.connector.sharepoint.generated.gsbulkauthorization.BulkAuthorizationLocator;
import com.google.enterprise.connector.sharepoint.generated.gsbulkauthorization.BulkAuthorizationSoap_BindingStub;
import com.google.enterprise.connector.sharepoint.generated.gsbulkauthorization.holders.ArrayOfAuthDataPacketHolder;
import com.google.enterprise.connector.sharepoint.spiimpl.SharepointException;
import com.google.enterprise.connector.sharepoint.wsclient.client.BulkAuthorizationWS;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.rpc.ServiceException;

/**
 * Java Client for calling GSBulkAuthorization.asmx. Provides a layer to talk to
 * the GSBulkAuthorization Web Service deployed on the SharePoint server. Any
 * call to this Web Service must go through this layer.
 *
 * @author nitendra_thakur
 */
public class GSBulkAuthorizationWS implements BulkAuthorizationWS {
  private final Logger LOGGER = Logger.getLogger(GSBulkAuthorizationWS.class.getName());
  private SharepointClientContext sharepointClientContext;
  private BulkAuthorizationSoap_BindingStub stub = null;
  private String endpoint;

  /**
   * @param inSharepointClientContext The Context is passed so that necessary
   *          information can be used to create the instance of current class.
   *          Web Service endpoint is set to the default SharePoint URL stored
   *          in SharePointClientContext.
   * @throws SharepointException
   */
  public GSBulkAuthorizationWS(
      final SharepointClientContext inSharepointClientContext)
      throws SharepointException {
    sharepointClientContext = inSharepointClientContext;
    endpoint = Util.encodeURL(sharepointClientContext.getSiteURL())
        + SPConstants.GSPBULKAUTHORIZATION_ENDPOINT;
    LOGGER.log(Level.CONFIG, "Endpoint set to: " + endpoint);

    final BulkAuthorizationLocator bulkloc = new BulkAuthorizationLocator();
    bulkloc.setBulkAuthorizationSoapEndpointAddress(endpoint);
    final BulkAuthorization service = bulkloc;

    try {
      stub = (BulkAuthorizationSoap_BindingStub)
          service.getBulkAuthorizationSoap();
    } catch (final ServiceException e) {
      LOGGER.log(Level.WARNING, "Unable to get bulk authZ soap stub ", e);
      throw new SharepointException("Unable to get bulk authZ soap stub");
    }
  }

  /**
   * (@inheritDoc)
   */
  public String getUsername() {
    return stub.getUsername();
  }

  /**
   * (@inheritDoc)
   */
  public void setUsername(final String username) {
    stub.setUsername(username);
  }

  /**
   * (@inheritDoc)
   */
  public void setPassword(final String password) {
    stub.setPassword(password);
  }

  /**
   * (@inheritDoc)
   */
  public void setTimeout(final int timeout) {
    stub.setTimeout(timeout);
  }

  /**
   * To call the Authorize() Web Method of GSBulkAuthorization Web Service
   *
   * @param authDataPacketArray Contains the list of documents to be authorized
   * @param userId The username to be authorized
   * @return the updated {@link AuthDataPacket} object reflecting the
   *         authorization status for each document
   * @throws RemoteException
   */
  public AuthDataPacket[] authorize(final AuthDataPacket[] authDataPacketArray,
      final String userId) throws RemoteException {
    ArrayOfAuthDataPacketHolder arrayOfAuthDataPacketHolder = 
        new ArrayOfAuthDataPacketHolder(authDataPacketArray);
    stub.authorize(arrayOfAuthDataPacketHolder, userId);
    return arrayOfAuthDataPacketHolder.value;
  }

  /**
   * For checking connectivity to the GSBulkAuthorization web service
   *
   * @return The connectivity status "success" if succeed or the reason for
   *         failure.
   */
  public String checkConnectivity() throws RemoteException {
    return stub.checkConnectivity();
  }
}
