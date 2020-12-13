package com.dylansalim.qrmenuapp.ui.main.account;

public class AccountPresenter implements AccountPresenterInterface {

    AccountViewInterface accountView;

    public AccountPresenter(AccountViewInterface accountView) {
        this.accountView = accountView;
    }

    @Override
    public void switchRole(String currentRole) {

    }
}
