package lk.shenal.languagelearner.Helper_adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import lk.shenal.languagelearner.DB.DatabaseHelper;
import lk.shenal.languagelearner.Models.Phrase;
import lk.shenal.languagelearner.R;

public class TranslationRecyclerViewAdapter extends RecyclerView.Adapter<TranslationRecyclerViewAdapter.TranslationViewHolder> {
    private Context context;
    private List<Phrase> mPhrases;
    private DatabaseHelper db;
    private String clickedPhraseName = null;
    private int row_index;

    public TranslationRecyclerViewAdapter(Context context, List<Phrase> phrases) {
        this.context = context;
        mPhrases = phrases;
        db = new DatabaseHelper(context);
    }

    public class TranslationViewHolder extends RecyclerView.ViewHolder {
        TextView displayPhrase;
        ConstraintLayout constraintContainer;
        CardView parentLayout;
        ImageView cardImage;

        public TranslationViewHolder(@NonNull View itemView) {
            super(itemView);
            displayPhrase = itemView.findViewById(R.id.displayPhraseTextView);
            parentLayout = itemView.findViewById(R.id.parentView);
            constraintContainer = itemView.findViewById(R.id.constraintContainer);
            cardImage = itemView.findViewById(R.id.cardImage);
        }
    }

    //responsible for inflating the view
    @NonNull
    @Override
    public TranslationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycle_view_element , parent, false);
        return new TranslationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TranslationViewHolder holder, final int position) {
        String name = mPhrases.get(position).getPhraseContent();
        holder.displayPhrase.setText(name);
        String backgroundColor = mPhrases.get(position).getPhraseColor();
        holder.constraintContainer.setBackgroundColor(Color.parseColor(backgroundColor));
        holder.cardImage.setImageResource(mPhrases.get(position).getPhraseImage());

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedPhraseName = holder.displayPhrase.getText().toString();
                row_index = position;
                notifyDataSetChanged();
            }
        });

        if(row_index == position){
            holder.displayPhrase.setTextSize(25);
            holder.parentLayout.setBackgroundResource(R.drawable.selected_background_border);
        }else{
            holder.displayPhrase.setTextSize(18);
            holder.parentLayout.setBackgroundResource(R.drawable.non_selected);
        }
    }

    @Override
    public int getItemCount() {
        return mPhrases.size();
    }

    public String getClickedPhrase(){
        return clickedPhraseName;
    }

}
