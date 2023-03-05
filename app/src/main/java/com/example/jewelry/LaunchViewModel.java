package com.example.jewelry;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.function.BiFunction;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleOnSubscribe;
import io.reactivex.rxjava3.core.SingleSource;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import kotlin.jvm.functions.Function2;

public class LaunchViewModel extends AndroidViewModel {

    private static final String TAG = "LaunchViewModel";
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(true);
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final DishDao dao;
    private final ApiService api;

    public LaunchViewModel(@NonNull Application application) {
        super(application);
        dao = DishDatabase.getInstance(application).dishDao();
        api = ApiFactory.apiService;
    }

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void refreshDishes() {

            Disposable disposable = dao.removeAllDishes()
                    .andThen(api.loadDishes())
                    .map(DishResponse::getDishes)
                    .flatMapCompletable(dishes -> dao.insertDishes(dishes).subscribeOn(Schedulers.io()))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            () -> {
                                isLoading.setValue(false);
                            },
                            throwable -> error.setValue(throwable.getMessage())
                    );


            compositeDisposable.add(disposable);
    }

    public void compareLocalVersionsToRemote() {
        io.reactivex.rxjava3.functions.BiFunction<VersionResponse, List<String>, Boolean> zipper =
                (versionResponse, localVersions) -> {
                    List<String> versions = versionResponse.getVersions();
                    return versions.equals(localVersions);
                };
        Disposable disposable = Single.zip(
                ApiFactory.apiService.loadVersions(),
                dao.getVersions(),
                zipper
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((isEqual, err) -> {
                    if (err != null) {
                        error.setValue(err.getMessage());
                    } else {
                        if (isEqual) {
                            isLoading.setValue(false);
                        } else {
                            isLoading.setValue(true);
                        }
                    }
                });
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
