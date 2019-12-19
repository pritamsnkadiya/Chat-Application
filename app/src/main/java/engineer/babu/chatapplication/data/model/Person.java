package engineer.babu.chatapplication.data.model;

import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "person")
public class Person {
    @PrimaryKey(autoGenerate = true)
    int id;
    public String name;
    public String number;

    @Ignore
    public Person(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public Person(){

    }

    public Person(int id, String name, String number) {
        this.id = id;
        this.name = name;
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @BindingAdapter({"bind:name"})
    public static void name(TextView view, String name) {
        view.setText(name);
    }

    @BindingAdapter({"bind:number"})
    public static void number(TextView view, String number) {
        view.setText(number);
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", number='" + number + '\'' +
                '}';
    }
}
