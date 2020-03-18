package edu.cnm.deepdive.quoteclient.controller;

import android.util.Log;
import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;
import edu.cnm.deepdive.quoteclient.model.Quote;
import edu.cnm.deepdive.quoteclient.service.GoogleSignInService;
import edu.cnm.deepdive.quoteclient.service.QuoteRepository;
import io.reactivex.disposables.CompositeDisposable;
import java.util.List;

public class MainViewModel extends ViewModel {

  private MutableLiveData<Quote> quote;
  private MutableLiveData<List<Quote>> quotes;
  private final MutableLiveData<Throwable> throwable;
  private final QuoteRepository repository;
  private CompositeDisposable pending;

  public MainViewModel() {
    repository = QuoteRepository.getInstance();
    pending = new CompositeDisposable();
    quote = new MutableLiveData<>();
    quotes = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    refreshRandom();
  }

  public LiveData<Quote> getQuote() {
    return quote;
  }

  public LiveData<List<Quote>> getQuotes() {
    return quotes;
  }

  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  public void refreshRandom() {
    GoogleSignInService.getInstance().refresh()
        .addOnSuccessListener((account) -> {
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

  public void refreshQuotes() {
    GoogleSignInService.getInstance().refresh()
        .addOnSuccessListener((account) -> {
          pending.add(
            repository.getAllQuotes(account.getIdToken())
            .subscribe(
                quotes::postValue,
                throwable::postValue
            )
          );
        })
        .addOnFailureListener(throwable::postValue);
  }
  @OnLifecycleEvent(Event.ON_STOP)
  private void clearPending() {
    pending.clear();
  }
}