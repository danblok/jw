package com.example.jewelry;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SingUpViewModel extends ViewModel {

    private static final MutableLiveData<String> error = new MutableLiveData<>();
    private static final MutableLiveData<Boolean> isSignedUp = new MutableLiveData<>(false);
    private static final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private static final ApiService api = ApiFactory.apiService;

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<Boolean> getIsSignedUp() {
        return isSignedUp;
    }

    public void signUp(String email, String password, String name, String phone) {
        Disposable disposable = api.signUp(new SignUpUserBody(email, password, name, phone))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> isSignedUp.setValue(true),
                        throwable -> error.setValue(throwable.getMessage())
                );

        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
