package edu.cnm.deepdive.quoteclient.service;

import edu.cnm.deepdive.quoteclient.model.Quote;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class QuoteRepository {

  private static final int NETWORK_POOL_SIZE = 10;

  private final QodService proxy;
  private final Executor networkPool;

  private QuoteRepository() {
    proxy = QodService.getInstance();
    networkPool = Executors.newFixedThreadPool(NETWORK_POOL_SIZE);

  }

  public static QuoteRepository getInstance() {
    return InstanceHolder.INSTANCE;
  }

  public Single<Quote> getRandom() {
    return proxy.getRandom()
        .subscribeOn(Schedulers.from(networkPool));
  }

  private static class InstanceHolder {

    private static final QuoteRepository INSTANCE = new QuoteRepository();
  }
}
