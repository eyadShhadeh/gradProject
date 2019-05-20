import android.app.Application;

import com.firebase.client.Firebase;


/**
 * Created by me on 2/14/2018.
 */

public class lazqaApp2  extends Application{
    public  void onCreate (){
    super.onCreate();

        Firebase.setAndroidContext(this);

    }

}
