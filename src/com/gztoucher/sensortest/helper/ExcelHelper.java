package com.gztoucher.sensortest.helper;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.hardware.Sensor;
import android.os.Environment;
import com.gztoucher.sensortest.bean.SensorBean;
import org.apache.poi.hssf.record.formula.functions.Date;
import org.apache.poi.hssf.usermodel.*;

/**
 * 使用POI创建Excel示例
 *
 * @author 李玉江[QQ:1023694760]
 * @version 2015/4/3
 *          Created by IntelliJ IDEA 14.1
 */
public class ExcelHelper {

    /**
     * 表格数据和SQLite中的要对应
     */
    public static String createExcel(ArrayList<SensorBean> data) throws Exception {
        ArrayList<SensorBean> accelerometer = new ArrayList<SensorBean>();//加速度传感器
        ArrayList<SensorBean> linearAccelerometer = new ArrayList<SensorBean>();//线性加速度传感器
        ArrayList<SensorBean> ambientTemperature = new ArrayList<SensorBean>();//环境温度传感器
        ArrayList<SensorBean> temperature = new ArrayList<SensorBean>();//设备温度传感器
        ArrayList<SensorBean> gravity = new ArrayList<SensorBean>();//重力传感器
        ArrayList<SensorBean> gyroscope = new ArrayList<SensorBean>();//陀螺仪传感器
        ArrayList<SensorBean> light = new ArrayList<SensorBean>();//光线传感器
        ArrayList<SensorBean> magnetic = new ArrayList<SensorBean>();//磁场传感器
        ArrayList<SensorBean> orientation = new ArrayList<SensorBean>();//方向传感器
        ArrayList<SensorBean> pressure = new ArrayList<SensorBean>();//压力传感器
        ArrayList<SensorBean> proximity = new ArrayList<SensorBean>();//近距感应传感器
        ArrayList<SensorBean> rotation = new ArrayList<SensorBean>();//旋转矢量传感器
        for (SensorBean bean : data) {
            switch (bean.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    accelerometer.add(bean);
                    break;
                case Sensor.TYPE_LINEAR_ACCELERATION:
                    linearAccelerometer.add(bean);
                    break;
                case Sensor.TYPE_AMBIENT_TEMPERATURE:
                    ambientTemperature.add(bean);
                    break;
                case Sensor.TYPE_TEMPERATURE:
                    temperature.add(bean);
                    break;
                case Sensor.TYPE_GRAVITY:
                    gravity.add(bean);
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    gyroscope.add(bean);
                    break;
                case Sensor.TYPE_LIGHT:
                    light.add(bean);
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    magnetic.add(bean);
                    break;
                case Sensor.TYPE_ORIENTATION:
                    orientation.add(bean);
                    break;
                case Sensor.TYPE_PRESSURE:
                    pressure.add(bean);
                    break;
                case Sensor.TYPE_PROXIMITY:
                    proximity.add(bean);
                    break;
                case Sensor.TYPE_ROTATION_VECTOR:
                    rotation.add(bean);
                    break;
            }
        }
        // 创建文档
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_BOTTOM);
        HSSFSheet accelerometerSheet = workbook.createSheet("加速度");
        int accelerometerSize = accelerometer.size();
        if (accelerometerSize > 0) {
            for (short i = 0; i < accelerometerSize; i++) {
                SensorBean accelerometerBean = accelerometer.get(i);
                Object[] values = {accelerometerBean.getType(), accelerometerBean.getX(), accelerometerBean.getY(), accelerometerBean.getZ(), accelerometerBean.getTimeline()};
                insertRow(accelerometerSheet, i, values, cellStyle);
            }
        }
        HSSFSheet linearAccelerometerSheet = workbook.createSheet("线性加速度");
        int linearAccelerometerSize = linearAccelerometer.size();
        if (linearAccelerometerSize > 0) {
            for (short i = 0; i < linearAccelerometerSize; i++) {
                SensorBean linearAccelerometerBean = linearAccelerometer.get(i);
                Object[] values = {linearAccelerometerBean.getType(), linearAccelerometerBean.getX(), linearAccelerometerBean.getY(), linearAccelerometerBean.getZ(), linearAccelerometerBean.getTimeline()};
                insertRow(linearAccelerometerSheet, i, values, cellStyle);
            }
        }
        HSSFSheet gravitySheet = workbook.createSheet("重力");
        int gravitySize = gravity.size();
        if (gravitySize > 0) {
            for (short i = 0; i < gravitySize; i++) {
                SensorBean gravityBean = gravity.get(i);
                Object[] values = {gravityBean.getType(), gravityBean.getX(), gravityBean.getY(), gravityBean.getZ(), gravityBean.getTimeline()};
                insertRow(gravitySheet, i, values, cellStyle);
            }
        }
        HSSFSheet gyroscopeSheet = workbook.createSheet("陀螺仪");
        int gyroscopeSize = gyroscope.size();
        if (gyroscopeSize > 0) {
            for (short i = 0; i < gyroscopeSize; i++) {
                SensorBean gyroscopeBean = gyroscope.get(i);
                Object[] values = {gyroscopeBean.getType(), gyroscopeBean.getX(), gyroscopeBean.getY(), gyroscopeBean.getZ(), gyroscopeBean.getTimeline()};
                insertRow(gyroscopeSheet, i, values, cellStyle);
            }
        }
        HSSFSheet lightSheet = workbook.createSheet("光线");
        int lightSize = light.size();
        if (lightSize > 0) {
            for (short i = 0; i < lightSize; i++) {
                SensorBean lightBean = light.get(i);
                Object[] values = {lightBean.getType(), lightBean.getX(), lightBean.getY(), lightBean.getZ(), lightBean.getTimeline()};
                insertRow(lightSheet, i, values, cellStyle);
            }
        }
        HSSFSheet orientationSheet = workbook.createSheet("方向");
        int orientationSize = orientation.size();
        if (orientationSize > 0) {
            for (short i = 0; i < orientationSize; i++) {
                SensorBean orientationBean = orientation.get(i);
                Object[] values = {orientationBean.getType(), orientationBean.getX(), orientationBean.getY(), orientationBean.getZ(), orientationBean.getTimeline()};
                insertRow(orientationSheet, i, values, cellStyle);
            }
        }
        HSSFSheet proximitySheet = workbook.createSheet("近距离");
        int proximitySize = proximity.size();
        if (proximitySize > 0) {
            for (short i = 0; i < proximitySize; i++) {
                SensorBean proximityBean = proximity.get(i);
                Object[] values = {proximityBean.getType(), proximityBean.getX(), proximityBean.getY(), proximityBean.getZ(), proximityBean.getTimeline()};
                insertRow(proximitySheet, i, values, cellStyle);
            }
        }
        HSSFSheet rotationSheet = workbook.createSheet("旋转矢量");
        int rotationSize = rotation.size();
        if (rotationSize > 0) {
            for (short i = 0; i < rotationSize; i++) {
                SensorBean rotationBean = rotation.get(i);
                Object[] values = {rotationBean.getType(), rotationBean.getX(), rotationBean.getY(), rotationBean.getZ(), rotationBean.getTimeline()};
                insertRow(rotationSheet, i, values, cellStyle);
            }
        }
        HSSFSheet magneticSheet = workbook.createSheet("磁力");
        int magneticSize = magnetic.size();
        if (magneticSize > 0) {
            for (short i = 0; i < magneticSize; i++) {
                SensorBean magneticBean = magnetic.get(i);
                Object[] values = {magneticBean.getType(), magneticBean.getX(), magneticBean.getY(), magneticBean.getZ(), magneticBean.getTimeline()};
                insertRow(magneticSheet, i, values, cellStyle);
            }
        }
        // 保存文档
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tmp";
        new File(dirPath).mkdirs();
        File file = new File(dirPath, "sensor_test.xls");
        FileOutputStream fos;
        if (!file.exists()) {
            file.createNewFile();
        }
        fos = new FileOutputStream(file);
        workbook.write(fos);
        fos.close();
        return file.getAbsolutePath();
    }

    public static void deleteExcel() {
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tmp";
        File file = new File(dirPath, "sensor_test.xls");
        file.delete();
    }

    /**
     * 插入一行数据
     *
     * @param sheet        插入数据行的表单
     * @param rowIndex     插入的行的索引
     * @param columnValues 要插入一行中的数据，数组表示
     * @param cellStyle    该格中数据的显示样式
     */

    private static void insertRow(HSSFSheet sheet, short rowIndex,
                                  Object[] columnValues, HSSFCellStyle cellStyle) {
        HSSFRow row = sheet.createRow(rowIndex);
        int column = columnValues.length;
        for (short i = 0; i < column; i++) {
            createCell(row, i, columnValues[i], cellStyle);
        }
    }

    /**
     * 在一行中插入一个单元值
     *
     * @param row         要插入的数据的行
     * @param columnIndex 插入的列的索引
     * @param cellValue   该cell的值：如果是Calendar或者Date类型，就先对其格式化
     * @param cellStyle   该格中数据的显示样式
     */
    private static void createCell(HSSFRow row, short columnIndex, Object cellValue,
                                   HSSFCellStyle cellStyle) {
        HSSFCell cell = row.createCell(columnIndex);
        // 如果是Calender或者Date类型的数据，就格式化成字符串
        if (cellValue instanceof Date) {
            SimpleDateFormat format = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            String value = format.format(cellValue);
            HSSFRichTextString richTextString = new HSSFRichTextString(value);
            cell.setCellValue(richTextString);
        } else if (cellValue instanceof Calendar) {
            SimpleDateFormat format = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            String value = format.format(((Calendar) cellValue).getTime());
            HSSFRichTextString richTextString = new HSSFRichTextString(value);
            cell.setCellValue(richTextString);
        } else {
            HSSFRichTextString richTextString = new HSSFRichTextString(cellValue.toString());
            cell.setCellValue(richTextString);
        }
        cell.setCellStyle(cellStyle);
    }

}
