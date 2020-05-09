package lk.shenal.languagelearner.Helper_adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import lk.shenal.languagelearner.R;


public class ForeignPhrasesRecyclerViewAdapter extends RecyclerView.Adapter<ForeignPhrasesRecyclerViewAdapter.ForeignPhrasesViewHolder>{
    private Context context;
    private List<String> mForeignPhrases;

    public ForeignPhrasesRecyclerViewAdapter(Context context, List<String> ForeignPhrases) {
        this.context = context;
        mForeignPhrases = ForeignPhrases;
    }

    public class ForeignPhrasesViewHolder extends RecyclerView.ViewHolder {
        CardView foreignLanguageParentView;
        TextView foreignPhraseTextView;

        public ForeignPhrasesViewHolder(@NonNull View itemView) {
            super(itemView);
            foreignLanguageParentView = itemView.findViewById(R.id.foreignLanguageParentView);
            foreignPhraseTextView = itemView.findViewById(R.id.foreignPhraseTextView);
        }
    }

    //responsible for inflating the view
    @NonNull
    @Override
    public ForeignPhrasesRecyclerViewAdapter.ForeignPhrasesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.foreign_phrase_recyclerview_element , parent, false);
        return new ForeignPhrasesRecyclerViewAdapter.ForeignPhrasesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ForeignPhrasesRecyclerViewAdapter.ForeignPhrasesViewHolder holder, final int position) {
        String foreignPhrase = mForeignPhrases.get(position); //One translated foreign phrase
        holder.foreignPhraseTextView.setText(foreignPhrase); //Initialize textView with foreign phrase
        holder.foreignLanguageParentView.setBackgroundColor(Color.parseColor(setPhraseColor()));
    }

    public String setPhraseColor(){
        Random random = new Random();
        List<String> colors = Arrays.asList("#FED58C", "#FC6E5B", "#FF9E5C",
                "#86D0AB","#7B7382","#8A9BED","#6FD9B1",
                "#FC7D86","#DAB4DB", "#77c89f", "#fb7c85",
                "#fedea5", "#9fb3df", "#e0bddd",
                "#9daaee", "#f16e1e", "#65a4d9",
                "#7fd2f2", "#d6a5d2", "#fc6d5c", "#f1ded0", "#f5a572", "#f5a572");
        return colors.get(random.nextInt(colors.size()));
    }

    @Override
    public int getItemCount() {
        return mForeignPhrases.size();
    }
}
