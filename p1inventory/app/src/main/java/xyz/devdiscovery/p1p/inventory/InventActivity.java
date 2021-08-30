package xyz.devdiscovery.p1p.inventory;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class InventActivity extends AppCompatActivity {

    public TextView showUser;
    public LinearLayout linearLayout;
    public ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invent);


        showUser = (TextView) findViewById(R.id.showUser);
        linearLayout = (LinearLayout) findViewById(R.id.userAutorization);
        String user = getIntent().getExtras().getString("user_id");
        // Вывод пользователя в Activity
        //showUser.setText(user);

        actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(user);
        // Активация кнопки назад в ActionBar
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        linearLayout.setVisibility(View.INVISIBLE);
    }

    // Добавление пользовательской кнопки на ActionBar назад
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
