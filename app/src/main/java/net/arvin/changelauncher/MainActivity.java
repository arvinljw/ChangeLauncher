package net.arvin.changelauncher;

import android.content.ComponentName;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    String changeTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvInfo = (TextView) findViewById(R.id.tv_info);
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            tvInfo.setText("Name:" + getComponentName().getClassName() + "\nVersion:" + packageInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void changeToIcon1(View v) {
        changeTo = getClass().getName() + "1";
    }

    public void changeToIcon2(View v) {
        changeTo = getClass().getName() + "2";
    }

    public void reset(View v) {
        changeTo = getClass().getName();
    }

    @Override
    protected void onDestroy() {
        if (changeTo != null) {
            changeLauncher(changeTo);
        }
        super.onDestroy();
    }

    private void changeLauncher(String name) {
        PackageManager pm = getPackageManager();
        //隐藏之前显示的桌面组件
        pm.setComponentEnabledSetting(getComponentName(),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        //显示新的桌面组件
        pm.setComponentEnabledSetting(new ComponentName(MainActivity.this, name),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }
}
