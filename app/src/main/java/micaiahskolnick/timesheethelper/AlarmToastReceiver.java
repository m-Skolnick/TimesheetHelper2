package micaiahskolnick.timesheethelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Inspiron on 11/30/2017.
 */

public class AlarmToastReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"This is a toast Alarm",Toast.LENGTH_LONG).show();

    }
}
