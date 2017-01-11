package com.voyagerinnovation.grpcclient.secure;

/**
 * Created by amw on 1/11/2017.
 */

public interface SecureDataSource {
    void requestFromServer(String query, String password);
}
