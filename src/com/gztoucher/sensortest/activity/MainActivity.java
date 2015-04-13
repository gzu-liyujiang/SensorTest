package com.gztoucher.sensortest.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.os.Bundle;
import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.gztoucher.sensortest.R;
import com.gztoucher.sensortest.bean.SensorBean;
import com.gztoucher.sensortest.helper.*;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 主界面
 *
 * @author 李玉江[QQ:1023694760]
 * @version 2015/4/3
 *          Created by IntelliJ IDEA 14.1
 */
public class MainActivity extends Activity implements SensorHelper.OnSensorChangeListener, ScreenListener.ScreenStateListener {
    private WakeLocker wakeLocker;
    private ScreenListener screenListener;
    private SensorHelper accelerometerHelper;
    private SensorHelper orientationHelper;
    private TextView accelerometerX, accelerometerY, accelerometerZ;
    private TextView orientationA, orientationB, orientationC;
    private ExecutorService executorService;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WakeLocker.keepScreenBright(this);//保持屏幕长亮
        setContentView(R.layout.activity_main);
        screenListener = new ScreenListener(this);
        screenListener.start(this);
        wakeLocker = new WakeLocker(this, WakeLocker.Type.KEEP_CPU_RUN);
        wakeLocker.acquire();
        accelerometerHelper = new SensorHelper(this, Sensor.TYPE_ACCELEROMETER);
        orientationHelper = new SensorHelper(this, Sensor.TYPE_ORIENTATION);
        accelerometerX = (TextView) findViewById(R.id.main_txtAccelerometerX);
        accelerometerY = (TextView) findViewById(R.id.main_txtAccelerometerY);
        accelerometerZ = (TextView) findViewById(R.id.main_txtAccelerometerZ);
        orientationA = (TextView) findViewById(R.id.main_txtOrientationA);
        orientationB = (TextView) findViewById(R.id.main_txtOrientationB);
        orientationC = (TextView) findViewById(R.id.main_txtOrientationC);
        executorService = Executors.newCachedThreadPool();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wakeLocker.release();
    }

    @Override
    public void onBackPressed() {
        if (RepeatHelper.isFastDoubleAction(2000L)) {
            // 几毫秒之内连续按两次
            finish();
            System.exit(0);
        } else {
            Toast.makeText(getApplicationContext(), "再按一次退出软件", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onScreenOn() {
        System.out.println("屏幕已点亮");
    }

    @Override
    public void onScreenOff() {
        //在手机锁屏的时候，重新注册传感器可解决部分手机无法黑屏后收集数据的问题，
        //参考：http://bbs.csdn.net/topics/390410025
        System.out.println("屏幕已熄灭");
        accelerometerHelper.unregisterListener();
        accelerometerHelper.registerListener(this);
        orientationHelper.unregisterListener();
        orientationHelper.registerListener(this);
    }

    @Override
    public void onUserPresent() {
        System.out.println("屏幕已解锁");
    }

    @Override
    public void onSensorChanged(Sensor sensor, float[] values) {
        int sensorType = sensor.getType();
        showDataToView(sensorType, values);
        saveDataToDb(sensorType, values);
    }

    private void showDataToView(int sensorType, float[] values) {
        float x = values[0];
        float y = values[1];
        float z = values[2];
        System.out.println(String.format("x=%f,y=%f,z=%f", x, y, z));
        if (sensorType == Sensor.TYPE_ACCELEROMETER) {
            showAccelerometerDataToView(x, y, z);
        } else if (sensorType == Sensor.TYPE_ORIENTATION) {
            showOrientationDataToView(x, y, z);
        }
    }

    private void showAccelerometerDataToView(float x, float y, float z) {
        accelerometerX.setText("X轴：" + x + "米每平方秒");
        accelerometerY.setText("Y轴：" + y + "米每平方秒");
        accelerometerZ.setText("Z轴：" + z + "米每平方秒");
    }

    private void showOrientationDataToView(float x, float y, float z) {
        String orientationString = "";
        String topBottomString = "";
        String leftRightString = "";
        if (x == 0) {
            orientationString = "正北";
        } else if (x == 90) {
            orientationString = "正东";
        } else if (x == 180) {
            orientationString = "正南";
        } else if (x == 270) {
            orientationString = "正西";
        } else if (x < 90 && x > 0) {
            orientationString = "北偏东" + x + "度";
        } else if (x < 180 && x > 90) {
            orientationString = "南偏东" + (180 - x) + "度";
        } else if (x < 270 && x > 180) {
            orientationString = "南偏西" + (x - 180) + "度";
        } else if (x < 360 && x > 270) {
            orientationString = "北偏西" + (360 - x) + "度";
        }
        if (y == 0) {
            topBottomString = "手机头部或底部没有翘起";
        } else if (y > 0) {
            topBottomString = "底部向上翘起" + y + "度";
        } else if (y < 0) {
            topBottomString = "顶部向上翘起" + Math.abs(y) + "度";
        }
        if (z == 0) {
            leftRightString = "手机左侧或右侧没有翘起";
        } else if (z > 0) {
            leftRightString = "右侧向上翘起" + z + "度";
        } else if (z < 0) {
            leftRightString = "左侧向上翘起" + Math.abs(z) + "度";
        }
        orientationA.setText("手机头部指向：" + orientationString);
        orientationB.setText("手机顶部或尾部翘起的角度：" + topBottomString);
        orientationC.setText("手机左侧或右侧翘起的角度：" + leftRightString);
    }

    private void saveDataToDb(final int sensorType, float[] values) {
        final float x = values[0];
        final float y = values[1];
        final float z = values[2];
        final long timeline = System.currentTimeMillis();
        Thread thread = new Thread() {
            //java.lang.OutOfMemoryError: pthread_create (stack size 16384 bytes) failed: Try again
            //通过线程池及时间频繁度来减少OOM的发生
            @Override
            public void run() {
                long maxTimeMillis = 300L;
                if (RepeatHelper.isFastDoubleAction(maxTimeMillis)) {
                    return;//几毫秒后再保存
                }
                DbHelper dbHelper = DbHelper.getInstance(getBaseContext());
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                database.execSQL("INSERT INTO sensor_test (type,x,y,z,timeline) VALUES (?,?,?,?,?)",
                        new Object[]{sensorType, x, y, z, timeline});
            }
        };
        executorService.submit(thread);
    }

    public void exportExcel(View view) {
        final ProgressDialog dialog = ProgressDialog.show(this, null, "正在将数据导出到Excel……");
        new Thread() {
            @Override
            public void run() {
                DbHelper dbHelper = DbHelper.getInstance(getBaseContext());
                SQLiteDatabase database = dbHelper.getReadableDatabase();
                Cursor cursor = database.rawQuery("SELECT * FROM sensor_test", null);
                cursor.moveToFirst();
                ArrayList<SensorBean> data = new ArrayList<SensorBean>();
                while (cursor.moveToNext()) {
                    SensorBean bean = new SensorBean();
                    bean.setType(cursor.getInt(cursor.getColumnIndex("type")));
                    bean.setX(cursor.getFloat(cursor.getColumnIndex("x")));
                    bean.setY(cursor.getFloat(cursor.getColumnIndex("y")));
                    bean.setZ(cursor.getFloat(cursor.getColumnIndex("z")));
                    bean.setTimeline(cursor.getLong(cursor.getColumnIndex("timeline")));
                    data.add(bean);
                }
                cursor.close();
                String result;
                String path;
                try {
                    path = ExcelHelper.createExcel(data);
                    result = "导出到Excel成功！";
                } catch (Exception e) {
                    path = "";
                    e.printStackTrace();
                    result = "导出失败：" + e.getMessage();
                }
                final String finalResult = result;
                final String finalPath = path;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), finalResult, Toast.LENGTH_SHORT).show();
                        //发送到手机QQ
                        if (!TextUtils.isEmpty(finalPath)) {
                            String packageName = "com.tencent.mobileqq";
                            try {
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setPackage(packageName);
                                intent.setType("*/*");
                                intent.putExtra(Intent.EXTRA_STREAM, finalPath);
                                startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        }.start();
    }

    public void startCollectData(View view) {
        //开始监听传感器
        accelerometerHelper.registerListener(this);
        orientationHelper.registerListener(this);
    }

    public void stopCollectData(View view) {
        //取消传感器监听
        accelerometerHelper.unregisterListener();
        orientationHelper.unregisterListener();
    }

    public void clearData(View view) {
        final ProgressDialog dialog = ProgressDialog.show(this, null, "正在清空数据……");
        new Thread() {
            @Override
            public void run() {
                DbHelper dbHelper = DbHelper.getInstance(getBaseContext());
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                database.execSQL("DELETE FROM sensor_test");//删除数据库中的数据
                ExcelHelper.deleteExcel();//删除Excel文件
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "已清空数据", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }.start();
    }

}
