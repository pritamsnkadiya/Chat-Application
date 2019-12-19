package engineer.babu.chatapplication.data.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserModel {

    public String userName;
    public String userEmail;
    public String mobileNo;
    public String imageEncoded;

    public String user_id;
    public String name;
    public String status;
    public String image;
    public String thumb_image;

    public UserModel() {
    }

    public UserModel(String userName, String userEmail, String mobileNo, String imageEncoded) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.mobileNo = mobileNo;
        this.imageEncoded = imageEncoded;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getImageEncoded() {
        return imageEncoded;
    }

    public void setImageEncoded(String imageEncoded) {
        this.imageEncoded = imageEncoded;
    }

    @BindingAdapter({"bind:name"})
    public static void name(TextView view, String name) {
        view.setText(name);
    }

    @BindingAdapter({"bind:status"})
    public static void status(TextView view, String status) {
        view.setText(status);
    }

    @BindingAdapter({"bind:userName"})
    public static void userName(TextView view, String userName) {
        view.setText(userName);
    }

    @BindingAdapter({"bind:userEmail"})
    public static void userEmail(TextView view, String userEmail) {
        view.setText(userEmail);
    }

    @BindingAdapter({"bind:mobileNo"})
    public static void mobileNo(TextView view, String mobileNo) {
        view.setText(mobileNo);
    }

    @BindingAdapter({"bind:imageEncoded"})
    public static void imageEncoded(CircleImageView view, String imageEncoded) {
        byte[] decodedByte = Base64.decode(imageEncoded, 0);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
        view.setImageBitmap(bitmap);
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "userName='" + userName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", imageEncoded='" + imageEncoded + '\'' +
                ", user_id='" + user_id + '\'' +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", image='" + image + '\'' +
                ", thumb_image='" + thumb_image + '\'' +
                '}';
    }
}
