package lk.shenal.languagelearner.Helper_adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import lk.shenal.languagelearner.Models.Language;
import lk.shenal.languagelearner.R;

public class LanguageListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private ArrayList<Language> mlanguages;
    private HashMap<Language, Boolean> savedLanguages = new HashMap<>();


    public LanguageListAdapter(Context context, ArrayList<Language> languages) {
        mContext = context;
        mlanguages = languages;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return mlanguages.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.language_box_element, parent, false);
            holder = new ViewHolder();
            holder.subscriptionBox = convertView.findViewById(R.id.languageCheckBox);
            holder.availableLanguagesTextView = convertView.findViewById(R.id.availableLanguagesTextView);
            holder.countryFlag = convertView.findViewById(R.id.countryFlag);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        Language language = mlanguages.get(position);
        holder.availableLanguagesTextView.setText(language.getLanguageName());
        holder.subscriptionBox.setChecked(language.getIsSubscribed());
        holder.subscriptionBox.setTag(language);
        holder.countryFlag.setImageResource(language.getCountryImage());

        holder.subscriptionBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()) {
                    ischecked(position,true);
                }else {
                    ischecked(position,false);
                }
            }
        });

        if (savedLanguages.get(language) != null) {
            holder.subscriptionBox.setChecked(savedLanguages.get(language));
        }
        return convertView;

    }

    public void ischecked(int position,boolean flag ) {
        savedLanguages.put(mlanguages.get(position), flag);
    }

    private static class ViewHolder {
        CheckBox subscriptionBox;
        TextView availableLanguagesTextView;
        ImageView countryFlag;
    }

    //Return checked languages as a hashMap
    public HashMap<Language, Boolean> getCheckedLanguages(){
        return savedLanguages;
    }
}
