package edu.cnm.deepdive.quoteclient.service;

import edu.cnm.deepdive.quoteclient.model.Quote;
import edu.cnm.deepdive.quoteclient.model.Source;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class QuoteRepository {

  private static final int NETWORK_POOL_SIZE = 10;
  private static final String OAUTH_HEADER_FORMAT = "Bearer %s";

  private final QodService proxy;
  private final Executor networkPool;

  private QuoteRepository() {
    proxy = QodService.getInstance();
    networkPool = Executors.newFixedThreadPool(NETWORK_POOL_SIZE);

  }

  public static QuoteRepository getInstance() {
    return InstanceHolder.INSTANCE;
  }

  public Single<Quote> getRandom(String token) {
    return proxy.getRandom(String.format(OAUTH_HEADER_FORMAT, token))
        .subscribeOn(Schedulers.from(networkPool));
  }

  public Single<List<Quote>> getAllQuotes(String token) {
    return proxy.getAll(String.format(OAUTH_HEADER_FORMAT, token))
        .subscribeOn(Schedulers.from(networkPool));
  }

  public Single<List<Source>> getAllSources(String token, boolean includeNull) {
    return proxy.gerAllSources(String.format(OAUTH_HEADER_FORMAT, token), includeNull)
        .subscribeOn(Schedulers.from(networkPool));
  }

  public Single<Quote> add(String token, Quote quote) {
    return proxy.post(String.format(OAUTH_HEADER_FORMAT, token), quote)
        .subscribeOn(Schedulers.from(networkPool));
  }

  private static class InstanceHolder {

    private static final QuoteRepository INSTANCE = new QuoteRepository();
  }
}
