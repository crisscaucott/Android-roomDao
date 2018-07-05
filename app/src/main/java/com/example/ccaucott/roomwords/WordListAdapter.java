package com.example.ccaucott.roomwords;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordViewHolder> {

    private final LayoutInflater mInflater;
    private List<Word> mWords;
    private Context mContext;
    OnItemInteractionListener mListener;
    private int lastPosition = -1;
    private long wordIdToAnimate = -1;

    interface OnItemInteractionListener{
        void onClickItem(Word word);
    }

    WordListAdapter(Context context){
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
        mListener = (OnItemInteractionListener) context;
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new WordViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        if (mWords != null){
            Word current = mWords.get(position);
            holder.wordItemView.setText(current.getWord());

            if (current.getId() == wordIdToAnimate){
                setAnimation(holder.wordItemView);
                wordIdToAnimate = -1;
            }

        }else{
            // En cuyo caso de que no este seteado el array de palabras.
            holder.wordItemView.setText(R.string.no_word);
        }
    }

    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(View viewToAnimate)
    {
        ColorDrawable[] color = {
                new ColorDrawable(mContext.getResources().getColor(R.color.colorAccent)),
                new ColorDrawable( mContext.getResources().getColor(R.color.colorPrimaryLight))
        };
        TransitionDrawable trans = new TransitionDrawable(color);
        viewToAnimate.setBackgroundDrawable(trans);
        trans.startTransition(2000);
    }

    public void setWordIdToAnimate(long idWord){
        this.wordIdToAnimate = idWord;
    }

    public void setWords(List<Word> words){
        mWords = words;
        notifyDataSetChanged();
    }

    public Word getWordAtPosition(int position){
        return mWords.get(position);
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        if (mWords != null)
            return mWords.size();
        else
            return 0;
    }

    public class WordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView wordItemView;

        public WordViewHolder(View itemView) {
            super(itemView);
            wordItemView = itemView.findViewById(R.id.textView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.onClickItem(mWords.get(getAdapterPosition()));
        }
    }
}
