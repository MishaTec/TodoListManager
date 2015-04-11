package il.ac.huji.todolist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;


public class AddNewTodoItemActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_todo_item);

        final Button btnOK = (Button)findViewById(R.id.btnOK);
        btnOK.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent result = new Intent();
                String title = ((EditText) findViewById(R.id.edtNewItem)).getText().toString();
                result.putExtra("title", title);
                DatePicker dp = (DatePicker) findViewById(R.id.datePicker);
                Calendar cal = Calendar.getInstance();
                cal.set(dp.getYear(),dp.getMonth(),dp.getDayOfMonth(),0,0,0);
                result.putExtra("dueDate", cal.getTime()); // send java.util.Date
                setResult(RESULT_OK, result);
                finish();
            }
        });

        final Button btnCancel = (Button)findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent result = new Intent();
                setResult(RESULT_CANCELED, result);
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_new_todo_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
/*        if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }
}
