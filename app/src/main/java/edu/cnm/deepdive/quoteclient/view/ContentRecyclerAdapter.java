package edu.cnm.deepdive.quoteclient.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import edu.cnm.deepdive.quoteclient.R;
import edu.cnm.deepdive.quoteclient.model.Content;
import edu.cnm.deepdive.quoteclient.model.Quote;
import edu.cnm.deepdive.quoteclient.model.Source;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContentRecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {

  private final Context context;
  private final List<Content> contents;
  private final Map<Class<? extends Content>, Integer> layouts;
  private final Map<Integer, Class<? extends ViewHolder>> holders;

  public ContentRecyclerAdapter(Context context, List<Content> contents) {
    this.context = context;
    this.contents = contents;
    layouts = new HashMap<>();
    holders = new HashMap<>();
    layouts.put(Source.class, R.layout.item_content_source);
    layouts.put(Quote.class, R.layout.item_content_quote);
    holders.put(R.layout.item_content_source, SourceHolder.class);
    holders.put(R.layout.item_content_quote, QuoteHolder.class);
  }

  @Override
  public int getItemViewType(int position) {
    return layouts.get(contents.get(position).getClass());
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    try {
      Class<? extends ViewHolder> holderClass = holders.get(viewType);
      Constructor<? extends ViewHolder> constructor = holderClass.getConstructor(View.class);
      View root = LayoutInflater.from(context).inflate(viewType, parent, false);
      return constructor.newInstance(root);
    } catch (NoSuchMethodException
        | IllegalAccessException
        | InstantiationException
        | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    try {
      Class<? extends ViewHolder> holderClass = holder.getClass();
      Method binder = holderClass.getMethod("bind", int.class, Content.class);
      binder.invoke(holder, position, contents.get(position));
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public int getItemCount() {
    return contents.size();
  }

  private static class SourceHolder extends ViewHolder {

    private final TextView sourceName;

    public SourceHolder(@NonNull View itemView) {
      super(itemView);
      sourceName = itemView.findViewById(R.id.source_name);
    }

    public void bind(int position, Content source) {
      Source s = (Source) source;
      sourceName.setText((s.getName() != null) ? s.getName() : "(Unattributed)");
    }

  }

  private static class QuoteHolder extends ViewHolder {

    private final TextView quoteText;

    public QuoteHolder(@NonNull View itemView) {
      super(itemView);
      quoteText = itemView.findViewById(R.id.quote_text);
    }

    public void bind(int position, Content quote) {
      quoteText.setText(((Quote) quote).getText());
    }

  }

}




