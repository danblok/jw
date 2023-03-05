package com.example.jewelry;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SingInViewModel extends ViewModel {

    private static final MutableLiveData<String> error = new MutableLiveData<>();
    private static final MutableLiveData<String> token = new MutableLiveData<>();
    private static final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private static final ApiService api = ApiFactory.apiService;

    public LiveData<String> getError() {
        return error;
    }
    public LiveData<String> getToken() {
        return token;
    }

    public void signInWithEmailAndPassword(String email, String password) {
        Disposable disposable = api.logIn(new UserBody(email, password))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userResponse -> {
                            token.setValue(userResponse.getToken());
                        },
                        throwable -> {
                            error.setValue(throwable.getMessage());
                        });

        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
