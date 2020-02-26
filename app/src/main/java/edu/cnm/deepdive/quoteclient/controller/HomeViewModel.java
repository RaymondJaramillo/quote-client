package edu.cnm.deepdive.quoteclient.controller;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import edu.cnm.deepdive.quoteclient.model.Quote;
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
    pending.add(
        repository.getRandom()
            .subscribe(
                quote::postValue,
                throwable::postValue
            )
    );
  }
}