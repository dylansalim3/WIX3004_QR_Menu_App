package com.dylansalim.qrmenuapp.ui.main.account;

public interface AccountPresenterInterface {

    void switchRole(String currentRole, String token);

    void logout();

    void disposeObserver();
}
