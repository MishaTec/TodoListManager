package il.ac.huji.todolist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TodoListManagerActivity extends ActionBarActivity {
    private static final int NEW_ITEM_REQUEST = 42;
    private ArrayAdapter<TodoListItem> adapter;
    private ArrayList<TodoListItem> list = new ArrayList<>();

    private class TodoListItem {
        private String title;
        private Date dueDate;

        private TodoListItem(String title, Date dueDate) {
            this.title = title;
            this.dueDate = dueDate;
        }
    }

    private class TodoListAdapter extends ArrayAdapter<TodoListItem> {
        public TodoListAdapter(TodoListManagerActivity todoListManagerActivity, int simple_list_item_1, List<TodoListItem> theList) {
            super(todoListManagerActivity, simple_list_item_1, theList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.todo_list_item, null);

            TextView titleText = (TextView) view.findViewById(R.id.txtTodoTitle);
            titleText.setText(getItem(position).title);

            TextView dueDateText = (TextView) view.findViewById(R.id.txtTodoDueDate);
            Date dueDate = getItem(position).dueDate;
            if (dueDate != null) {
                SimpleDateFormat dateOnly = new SimpleDateFormat("dd/MM/yyyy");
                dueDateText.setText(dateOnly.format(dueDate));
                if (isOverdue(dueDate)) {
                    dueDateText.setTextColor(Color.RED);
                    titleText.setTextColor(Color.RED);
                }
            } else {
                dueDateText.setText("No due date");
            }
            return view;
        }

        /**
         * Checks if the dueDate is before the current day (today)
         *
         * @param dueDate - assumes dueDate's time is set to 00:00:00
         * @return true iff dueDate is before the current day
         */
        private boolean isOverdue(Date dueDate) {
            Calendar todayCal = Calendar.getInstance();
            todayCal.set(Calendar.HOUR_OF_DAY, 0);
            todayCal.set(Calendar.MINUTE, 0);
            todayCal.set(Calendar.SECOND, 0);
            todayCal.set(Calendar.MILLISECOND, 0);
            Calendar dueCal = Calendar.getInstance();
            dueCal.setTime(dueDate);
            return dueCal.before(todayCal);
        }
    }

    /**
     * Opens a dialog for adding a to-do
     *
     * @return true
     */
    private boolean addNewItem() {
        Intent intent = new Intent(this, AddNewTodoItemActivity.class);
        startActivityForResult(intent, NEW_ITEM_REQUEST);
        return true;
    }

    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent data) {
        if (resCode == Activity.RESULT_OK && reqCode == NEW_ITEM_REQUEST) {
            String title = data.getStringExtra("title");
            Date dueDate = (Date) data.getSerializableExtra("dueDate");
            if (!title.equals("")) {
                list.add(new TodoListItem(title, dueDate));
            }
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list_manager);

        ListView todoList = (ListView) findViewById(R.id.lstTodoItems);
        adapter = new TodoListAdapter(this, R.layout.todo_list_item, this.list);
        todoList.setAdapter(adapter);

        todoList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> av, View v, int pos, final long id) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(TodoListManagerActivity.this);
                final int posFinal = pos;
                String title = ((TextView) v.findViewById(R.id.txtTodoTitle)).getText().toString();
                builder.setMessage(title);
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        TodoListManagerActivity.this.list.remove(posFinal);
                        adapter.notifyDataSetChanged();
                    }
                });

                if (title.toLowerCase().matches("call\\s[^\\s]+.*")) { // starts with 'call'
                    final String tel = title.substring(5);
                    builder.setNegativeButton("Call " + tel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel));
                            startActivity(dial);
                        }
                    });
                }

                builder.show();
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_todo_list_manager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menuItemAdd) {
            addNewItem();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

/*
    @Override
    public boolean onKeyDown(int keycode, KeyEvent e) {
        switch(keycode) {
            case KeyEvent.KEYCODE_MENU:
                addNewItem();
                return true;
        }

        return super.onKeyDown(keycode, e);
    }
*/

}
