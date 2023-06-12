package discovery.plaine_example;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ArticleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        // Intent intent = getIntent();
        // String articleTitle = intent.getStringExtra("title");
        // String articleText = intent.getStringExtra("text");

        TextView titleTextView = findViewById(R.id.textViewTitle);

        String htmlAsString = getString(R.string.content);
        Spanned htmlAsSpanned = Html.fromHtml(htmlAsString);

        TextView textView = (TextView) findViewById(R.id.tvContentText);
        textView.setText(htmlAsSpanned);

    }

}