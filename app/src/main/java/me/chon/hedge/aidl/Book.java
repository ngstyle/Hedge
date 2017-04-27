package me.chon.hedge.aidl;

import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chon on 2017/2/27.
 * What? How? Why?
 */

public class Book implements Parcelable {
    public int bookID;
    public String bookName;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.bookID);
        dest.writeString(this.bookName);

        Message message = Message.obtain();
    }

    public Book() {
    }

    protected Book(Parcel in) {
        this.bookID = in.readInt();
        this.bookName = in.readString();
    }

    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
}
