package ru.synergy.rvconrentproviderwithsql;

import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CustomVieHolder extends RecyclerView.ViewHolder {

    public TextView textView1;

    public CustomVieHolder(@NonNull View itemView) {
        super(itemView);
        textView1 = (TextView) itemView.findViewById(android.R.id.text1);

    }

    public void setData(Cursor c){
        textView1.setText(c.getString(c.getColumnIndex("text")));
    }


}
