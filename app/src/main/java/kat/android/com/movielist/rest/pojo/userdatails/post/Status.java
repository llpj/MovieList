package kat.android.com.movielist.rest.pojo.userdatails.post;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

//Using GSON to convert json to java object
public class Status implements Serializable {

    @SerializedName("status_code")
    private int status_code;

    @SerializedName("status_message")
    private String status_message;

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public String getStatus_message() {
        return status_message;
    }

    public void setStatus_message(String status_message) {
        this.status_message = status_message;
    }
}
