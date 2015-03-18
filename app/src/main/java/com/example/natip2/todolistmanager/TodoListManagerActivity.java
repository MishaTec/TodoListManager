package com.example.natip2.todolistmanager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.KeyEvent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TodoListManagerActivity extends ActionBarActivity {
    private class TodoListAdapter extends ArrayAdapter<String> {
        public TodoListAdapter(TodoListManagerActivity todoListManagerActivity, int simple_list_item_1, List<String> theList) {
            super(todoListManagerActivity,simple_list_item_1,theList);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView)super.getView(position, convertView, parent);
            if (position % 2 == 0) {
                view.setTextColor(Color.RED);
            } else {
                view.setTextColor(Color.BLUE);
            }
            return view;
        }
    }

    private boolean addNewItem(){
        EditText item = (EditText) findViewById(R.id.edtNewItem);
        list.add(item.getText().toString());
        item.setText("");
        adapter.notifyDataSetChanged();
        return true;
    }


    ArrayAdapter<String> adapter;
    ArrayList<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list_manager);

        ListView todoList = (ListView)findViewById(R.id.lstTodoItems);
        adapter = new TodoListAdapter(this, android.R.layout.simple_list_item_1, this.list);
        todoList.setAdapter(adapter);

        todoList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> av, View v, int pos, final long id) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(TodoListManagerActivity.this);
                final int thatPos = pos;
                DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        TodoListManagerActivity.this.list.remove(thatPos);
                        adapter.notifyDataSetChanged();

                    }
                };
                builder.setMessage(TodoListManagerActivity.this.list.get(pos));
                builder.setPositiveButton("delete", listener);
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
        if (id == R.id.action_settings) {
            addNewItem();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keycode, KeyEvent e) {
        switch(keycode) {
            case KeyEvent.KEYCODE_MENU:
                addNewItem();
                return true;
        }

        return super.onKeyDown(keycode, e);
    }

}
