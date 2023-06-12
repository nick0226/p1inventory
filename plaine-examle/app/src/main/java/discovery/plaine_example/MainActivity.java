package discovery.plaine_example;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import discovery.plaine_example.Adapter.CardAdapter;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CardAdapter cardAdapter;
    private List<CardItem> cardList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String applicationName = getString(R.string.content_title);

        recyclerView = findViewById(R.id.recyclerView);
        cardList = new ArrayList<>();
        cardAdapter = new CardAdapter(this, cardList);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(cardAdapter);

        prepareCardData();

        cardAdapter.setOnItemClickListener(new CardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                // get position list
                CardItem clickedItem = cardList.get(position);

                // animation
                Animation animation = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f);
                animation.setDuration(600);
                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
                if (viewHolder != null) {
                    TextView mainText = viewHolder.itemView.findViewById(R.id.mainText);
                    TextView subText = viewHolder.itemView.findViewById(R.id.subText);
                    ImageView imageView = viewHolder.itemView.findViewById(R.id.imageView);
                    mainText.startAnimation(animation);
                    subText.startAnimation(animation);
                    imageView.startAnimation(animation);
                }

                // start activity content
                Intent intent = new Intent(MainActivity.this, ArticleActivity.class);
                //intent.putExtra("title", clickedItem.getMainText());
                //intent.putExtra("text", clickedItem.getSubText());
                startActivity(intent);
            }
        });

    }

    private void prepareCardData() {
        int[] imageResArray = {R.drawable.boing_747_image, R.drawable.boing_747_image, R.drawable.boing_747_image};
        String[] mainTextArray = {"Boeing 747", "Boeing 747", "Boeing 747"};
        String[] secondaryTextArray = {"1970", "1970", "1970"};

        for (int i = 0; i < mainTextArray.length; i++) {
            CardItem cardItem = new CardItem(mainTextArray[i], secondaryTextArray[i], imageResArray[i]);
            cardList.add(cardItem);
        }
        cardAdapter.notifyDataSetChanged();
    }

}