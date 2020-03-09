package edu.cnm.deepdive.quoteclient.controller;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import edu.cnm.deepdive.quoteclient.model.Quote;
import edu.cnm.deepdive.quoteclient.service.GoogleSignInService;
import edu.cnm.deepdive.quoteclient.service.QuoteRepository;
import io.reactivex.disposables.CompositeDisposable;

public class HomeViewModel extends ViewModel {

  private MutableLiveData<Quote> quote;
  private final MutableLiveData<Throwable> throwable;
  private final QuoteRepository repository;
  private CompositeDisposable pending;

  public HomeViewModel() {
    repository = QuoteRepository.getInstance();
    pending = new CompositeDisposable();
    quote = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    refresh();
  }

  public LiveData<Quote> getQuote() {
    return quote;
  }

  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  public void refresh() {
    GoogleSignInService.getInstance().refresh()
        .addOnSuccessListener((account) -> {
          Log.d(getClass().getName(), account.getDisplayName());
          Log.d(getClass().getName(), account.getIdToken());
          Log.d(getClass().getName(), account.getEmail());
          Log.d(getClass().getName(), account.getId());
          pending.add(
              repository.getRandom(account.getIdToken())
                  .subscribe(
                      quote::postValue,
                      throwable::postValue
                  )
          );

        })
        .addOnFailureListener(throwable::postValue);
  }
}