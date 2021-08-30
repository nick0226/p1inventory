package xyz.devdiscovery.p1p.inventory;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xyz.devdiscovery.p1p.inventory.helper.DBHelper;

public class MainActivity extends AppCompatActivity {

    public String selectUsr;
    public Menu menu;
    ImageButton sync_server;
    ListView listUsers;
    SimpleAdapter ADAhere;
    Button statusButton;
    TextView textViewColors;
    EditText editText;

    private DBHelper mDBHelper;
    private SQLiteDatabase mDb;
    private SimpleAdapter adapter_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDBHelper = new DBHelper(this);

        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }

        listUsers = (ListView) findViewById(R.id.listUsers);
        statusButton = (Button) findViewById(R.id.status);
        textViewColors = (TextView) findViewById(R.id.textView);
        editText = (EditText) findViewById(R.id.search);

        // Скрываю элемент меню Wiew
        editText.setVisibility(View.INVISIBLE);


        // Реализация поиска в СПИСКЕ;
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

                try {
                    MainActivity.this.adapter_1.getFilter().filter(cs);
                } catch (Exception e){

                }

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });


    }


    @SuppressLint("ResourceAsColor")
    public void syncData(View v) {
        // Условие если нету интернет соединения, запускать локальную базу данных приложения;
        if ( !isOnline(this) ){
            listUsers.setBackgroundResource(R.color.orannge_list_offline);
            menu.getItem(3).setIcon(getResources().getDrawable(R.drawable.orange_signal_1));
            Toast.makeText(this, "На устройстве не доступна сеть", Toast.LENGTH_LONG).show();
            ArrayList<HashMap<String, Object>> users = new ArrayList<HashMap<String, Object>>();

            //Список параметров конкретного клиента
            HashMap<String, Object> user;

            //Отправляем запрос в БД
            Cursor cursor = mDb.rawQuery("SELECT * FROM users", null);
            cursor.moveToFirst();

            //Пробегаем по всем клиентам
            while (!cursor.isAfterLast()) {
                user = new HashMap<String, Object>();

                //Заполняем клиента
                user.put("text",  cursor.getString(1));
                //user.put("age",  cursor.getString(2));

                //Закидываем клиента в список клиентов
                users.add(user);

                //Переходим к следующему клиенту
                cursor.moveToNext();
            }
            cursor.close();

            //Какие параметры клиента мы будем отображать в соответствующих
            //элементах из разметки adapter_item.xml
            String[] from = { "text" };
            int[] to = { R.id.textView };

            //Создаем адаптер
            SimpleAdapter adapter = new SimpleAdapter(this, users, R.layout.adapter_item, from, to);
            ListView listView = (ListView) findViewById(R.id.listUsers);
            listView.setAdapter(adapter);
            // Присвоил данные адаптеру.
            adapter_1 = adapter;

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    selectUsr = String.valueOf(listView.getItemAtPosition(position));
                    String filterStringList = selectUsr.toString().replace("{", "").replace("}", "").replace("text=", "").replace("A=", "");
                    Intent intent = new Intent(getApplicationContext(), InventActivity.class);
                    intent.putExtra("user_id", filterStringList);
                    startActivity(intent);


                }
            });



        }
   }



   // Класс для работа с MYSQL СЕРВЕРОМ
    private class ConnectMySql extends AsyncTask<String, Void, String> {
        String res = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this, "Пожалуйста подождите, загрузка...", Toast.LENGTH_SHORT)
                    .show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(Const.getUrl(), Const.getUser(), Const.getPass());
                // System.out.println("Databaseection success");

                String result = "";
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("select distinct text from users");
                ResultSetMetaData rsmd = rs.getMetaData();

                List<Map<String, String>> data = null;
                data = new ArrayList<Map<String, String>>();

                while (rs.next()) {
                    Map<String, String> datanum = new HashMap<String, String>();
                    datanum.put("A", rs.getString(1).toString());
                    data.add(datanum);
                }

                String[] fromwhere = { "A" };
                int[] viewswhere = { R.id.textView };
                ADAhere = new SimpleAdapter(MainActivity.this, data,
                        R.layout.adapter_item, fromwhere, viewswhere);

                // Присвоил данные адаптеру.
                adapter_1 = ADAhere;

                while (rs.next()) {
                    result += rs.getString(1).toString() + "\n";
                }
                res = result;
            } catch (Exception e) {
                e.printStackTrace();
                res = e.toString();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            listUsers.setAdapter(ADAhere);
        }
    }


    // Проверка доступности интернета
    public static boolean isOnline(Context context)
    {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && ((NetworkInfo) netInfo).isConnectedOrConnecting())
        {
            return true;
        }
        return false;
    }

    // Меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        this.menu = menu;
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // Case с выбором нажатой кнопки;
    @SuppressLint("ResourceAsColor")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.sync_server :
                syncData(sync_server);
                // Если есть соединение с интернетом, загрузить список из удаленной базы MySQL;
                // Вызов метода, для показа списка, если устройство ОнЛайн;
                if (isOnline(this)){
                    // TODO Auto-generated method stub
                    ConnectMySql connectMySql = new ConnectMySql();
                    connectMySql.execute("");
                    listUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            selectUsr = String.valueOf(listUsers.getItemAtPosition(position));
                            String filterStringList = selectUsr.toString().replace("{", "").replace("}", "").replace("text=", "").replace("A=", "");
                            Intent intent = new Intent(getApplicationContext(), InventActivity.class);
                            intent.putExtra("user_id", filterStringList);
                            startActivity(intent);


                        }
                    });

                } if (isOnline(this)){
                listUsers.setBackgroundResource(R.color.yellow_list_online);
                menu.getItem(3).setIcon(getResources().getDrawable(R.drawable.yellow_signal_1));
            }

                return true;

            case R.id.open_filter:
                editText.setVisibility(View.VISIBLE);

                return true;

            case R.id.closed_filter:
                editText.setVisibility(View.INVISIBLE);
                editText.setText("");
                return true;

            case R.id.status:
                if (isOnline(this)) {
                    Toast.makeText(getApplicationContext(), "Есть доступ в интернет", Toast.LENGTH_LONG).show();
                }
                if (!isOnline(this))
                    Toast.makeText(getApplicationContext(), "Нету доступа в интернет", Toast.LENGTH_LONG).show();

                return true;

        }

        return super.onOptionsItemSelected(item);
    }

}