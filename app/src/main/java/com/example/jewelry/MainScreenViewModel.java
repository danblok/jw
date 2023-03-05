package com.example.jewelry;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainScreenViewModel extends ViewModel {

    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<List<Dish>> dishes = new MutableLiveData<>();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final ApiService api = ApiFactory.apiService;

    public LiveData<List<Dish>> getDishes() {
        return dishes;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void loadDishes() {
        Disposable disposable = api.loadDishes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        dishResponse -> {
                            dishes.setValue(dishResponse.getDishes());
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
