package edu.cnm.deepdive.quoteclient.controller;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import edu.cnm.deepdive.quoteclient.model.Quote;

public class HomeViewModel extends ViewModel {

  private MutableLiveData<Quote> quote = new MutableLiveData<>();

  public HomeViewModel() {
  }

 public LiveData<Quote> getQuote() {
    return quote;
 }

 public void refresh() {
    // TODO Invoke method in repository to get new quote
 }
}