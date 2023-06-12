package discovery.plaine_example.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import discovery.plaine_example.CardItem;
import discovery.plaine_example.R;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private Context context;
    private List<CardItem> cardList;
    private OnItemClickListener listener;


    public CardAdapter(Context context, List<CardItem> cardList) {
        this.context = context;
        this.cardList = cardList;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        CardItem cardItem = cardList.get(position);

        holder.textViewMain.setText(cardItem.getMainText());
        holder.textViewSecondary.setText(cardItem.getSubText());
        holder.imageView.setImageResource(cardItem.getImageResource());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int clickedPosition = holder.getAdapterPosition();
                    if (clickedPosition != RecyclerView.NO_POSITION) {
                        listener.onItemClick(clickedPosition);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewMain;
        public TextView textViewSecondary;
        public ImageView imageView;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMain = itemView.findViewById(R.id.mainText);
            textViewSecondary = itemView.findViewById(R.id.subText);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
