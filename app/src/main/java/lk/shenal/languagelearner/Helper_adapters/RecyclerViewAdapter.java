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
import java.util.ArrayList;
import java.util.List;
import lk.shenal.languagelearner.Models.Phrase;
import lk.shenal.languagelearner.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private Context context;
    private List<Phrase> mPhrases;

    public RecyclerViewAdapter(Context context, List<Phrase> phrases) {
        this.context = context;
        mPhrases = phrases;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView displayPhrase;
        ConstraintLayout constraintContainer;
        CardView parentLayout;
        ImageView cardImage;

        public ViewHolder(@NonNull View itemView) {
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycle_view_element , parent, false);//inflating the view with recycler_view_element

        return new ViewHolder(view);
    }

    //Repeat the viewHolder of size of phrases
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name = mPhrases.get(position).getPhraseContent();
        holder.displayPhrase.setText(name);
        String backgroundColor = mPhrases.get(position).getPhraseColor();
        holder.constraintContainer.setBackgroundColor(Color.parseColor(backgroundColor));
        holder.cardImage.setImageResource(mPhrases.get(position).getPhraseImage());
    }


    @Override
    public int getItemCount() {
        return mPhrases.size();
    }
    
    public void filterList(ArrayList<Phrase> filteredList){
        mPhrases = filteredList;
        notifyDataSetChanged();
    }
}
