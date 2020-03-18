package edu.cnm.deepdive.quoteclient.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import edu.cnm.deepdive.quoteclient.R;

public class RandomQuoteFragment extends Fragment {

  private MainViewModel viewModel;
  private TextView quoteText;
  private TextView quoteSource;

  public View onCreateView(@NonNull LayoutInflater inflater,
      ViewGroup container, Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_random_quote, container, false);
    quoteText = root.findViewById(R.id.quote_text);
    quoteSource = root.findViewById(R.id.quote_source);
    root.setOnClickListener((view) -> viewModel.refreshRandom());
    return root;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    //noinspection ConstantConditions
    viewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
    viewModel.getQuote().observe(getViewLifecycleOwner(), (quote) -> {
      quoteText.setText(quote.getText());
      quoteSource.setText((quote.getSource() != null)
          ? quote.getSource().getName() : getString(R.string.unattributed_source));
    });
  }

}