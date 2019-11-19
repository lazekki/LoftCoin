package com.loftschool.ozaharenko.loftcoin19.ui.welcome;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.loftschool.ozaharenko.loftcoin19.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

class WelcomePageAdapter extends Adapter<WelcomePageAdapter.ViewHolder>{

    private static final WelcomePage[] PAGES = {
      WelcomePage.create(
              R.drawable.welcome_page_1,
              R.string.welcome_page_1_title,
              R.string.welcome_page_1_subtitle
      ),

      WelcomePage.create(
              R.drawable.welcome_page_2,
              R.string.welcome_page_2_title,
              R.string.welcome_page_2_subtitle
      ),

      WelcomePage.create(
              R.drawable.welcome_page_3,
              R.string.welcome_page_3_title,
              R.string.welcome_page_3_subtitle
      )
    };

    private LayoutInflater inflater;

    @Override
    public int getItemCount() {
        return PAGES.length;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.welcome_page, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final WelcomePage page = PAGES[position];
        holder.image.setImageResource(page.imageResId());
        holder.title.setText(page.titleResId());
        holder.subtitle.setText(page.subtitleResId());
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        inflater = LayoutInflater.from(recyclerView.getContext());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;

        private TextView title;

        private TextView subtitle;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            subtitle = itemView.findViewById(R.id.subtitle);
        }
    }

}
