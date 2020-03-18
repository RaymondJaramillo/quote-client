package edu.cnm.deepdive.quoteclient.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.cnm.deepdive.quoteclient.R;
import edu.cnm.deepdive.quoteclient.model.Quote;
import edu.cnm.deepdive.quoteclient.view.QuoteRecyclerAdapter.Holder;
import java.util.List;

public class QuoteRecyclerAdapter extends RecyclerView.Adapter<Holder> {

  // Necessary fields for adapting a collection to a RecyclerView:
  //   - Context
  //   - Collection
  //   - Listeners

  private final Context context;
  private final List<Quote> quotes;

  public QuoteRecyclerAdapter(Context context, List<Quote> quotes) {
    this.context = context;
    this.quotes = quotes;
  }

  // Overrides of methods to
  //   - return the number of items in the collection
  //   - return a holder instance for an inflated layout
  //   - binds a holder instance to an object at a specified position in the recycler view

  @NonNull
  @Override
  public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View root = LayoutInflater.from(context).inflate(R.layout.item_quote, parent, false);
    return new Holder(root);
  }

  @Override
  public void onBindViewHolder(@NonNull Holder holder, int position) {
    holder.bind(position, quotes.get(position));
  }

  @Override
  public int getItemCount() {
    return quotes.size();
  }

  // Holder class that
  //   - Holds an inflated layout
  //   - Binds a specified object in the collection to the view objects in the inflated layout.
  class Holder extends RecyclerView.ViewHolder {

    private final View root;
    // More fields for view objects inside root
    private final TextView quoteText;
    private final TextView quoteSource;

    private Holder(View root) {
      super(root);
      this.root = root;
      quoteText = root.findViewById(R.id.quote_text);
      quoteSource = root.findViewById(R.id.quote_source);
    }

    private void bind(int position, Quote quote) {
      quoteText.setText(quote.getText());
      quoteSource.setText((quote.getSource() != null)
          ? quote.getSource().getName() : context.getString(R.string.unattributed_source));
      // TODO Set any event listeners.
    }

  }

  // Event listener interface declarations

}

