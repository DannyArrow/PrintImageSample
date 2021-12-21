package com.example.printimagesample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.brother.ptouch.sdk.LabelInfo;
import com.brother.ptouch.sdk.Printer;
import com.brother.ptouch.sdk.PrinterInfo;
import com.brother.ptouch.sdk.PrinterStatus;
import com.brother.sdk.lmprinter.Channel;
import com.brother.sdk.lmprinter.OpenChannelError;
import com.brother.sdk.lmprinter.PrintError;
import com.brother.sdk.lmprinter.PrinterDriver;
import com.brother.sdk.lmprinter.PrinterDriverGenerateResult;
import com.brother.sdk.lmprinter.PrinterDriverGenerator;
import com.brother.sdk.lmprinter.PrinterModel;
import com.brother.sdk.lmprinter.setting.PrintSettings;
import com.brother.sdk.lmprinter.setting.QLPrintSettings;

public class MainActivity extends AppCompatActivity {
    PrinterInfo myprinterInfo2;
    Printer myprinter2;
    PrinterStatus printerStatus;
    Bitmap bitmap;
    Button button;
    private static final String ACTION_USB_PERMISSION =
            "com.android.example.USB_PERMISSION";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        myprinter2  = new Printer();
        myprinterInfo2 = new PrinterInfo();

        UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        UsbDevice usbDevice = myprinter2.getUsbDevice(usbManager);

        PendingIntent permissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), PendingIntent.FLAG_IMMUTABLE);
        usbManager.requestPermission(usbDevice, permissionIntent);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

        myprinterInfo2 = myprinter2.getPrinterInfo();
        myprinterInfo2.printerModel = PrinterInfo.Model.QL_1100;
        myprinterInfo2.port = PrinterInfo.Port.USB;
        myprinterInfo2.paperSize = PrinterInfo.PaperSize.A5_LANDSCAPE;
        myprinterInfo2.orientation = PrinterInfo.Orientation.LANDSCAPE;
        myprinterInfo2.valign = PrinterInfo.VAlign.MIDDLE;
        myprinterInfo2.printMode = PrinterInfo.PrintMode.ORIGINAL;
        myprinterInfo2.numberOfCopies = 1;

        myprinterInfo2.labelNameIndex = LabelInfo.QL1100.W103H164.ordinal();

        //define cutting method
        myprinterInfo2.isAutoCut = true;
        myprinterInfo2.isCutAtEnd = false;
        myprinterInfo2.isHalfCut = false;
        myprinterInfo2.isSpecialTape = false;
        myprinterInfo2.workPath =  getCacheDir().getPath();

        myprinter2.setPrinterInfo(myprinterInfo2);

       bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.smile);

     //   bitmap = textAsBitmap("hdfttgyhujk");



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrinterThread printerThread = new PrinterThread();
                printerThread.start();
            }
        });

    }


    public Bitmap textAsBitmap(String text){
        Paint paint = new Paint();
        paint.setTextSize(3.5f);
        paint.setColor(Color.BLACK);
        paint.setTextAlign(Paint.Align.CENTER);
        float baseline = -paint.ascent();
        int width = (int) (paint.measureText(text) + 0.5f);
        int height = (int) (baseline + paint.descent() + 0.5f);
        Bitmap image = Bitmap.createBitmap(width+500,height+350, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawRect(0,0, width + 500, height + 350, paint);
        paint.setColor(Color.BLACK);
        canvas.drawText(text,0,baseline,paint);
        return image;
    }


      protected class PrinterThread extends Thread {





          @Override
          public void run() {
           printerStatus = new PrinterStatus();



              myprinter2.startCommunication();


              //  printerStatus = myprinter2.printImage(bitmap);
              String file2 = "/data/user/0/com.brother.ptouch.sdk.printdemo/files/com.brother.ptouch.sdk/myPdfFile.pdf";
             // String tag = "storage/emulated/0/Pictures/title.jpg";
              printerStatus = myprinter2.printPdfFile(file2,1);
              //printerStatus = myprinter2.printFile(file2);
              //printerStatus = myprinter2.printImage(bitmap);

              if (printerStatus.errorCode != PrinterInfo.ErrorCode.ERROR_NONE){
                  Log.d("error code = ", printerStatus.errorCode.toString());
              }

              myprinter2.endCommunication();




             // myprinter2.endCommunication();
          }

//          PrinterDriverGenerateResult result = PrinterDriverGenerator.openChannel(Channel.newUsbChannel((UsbManager) getSystemService(Context.USB_SERVICE)));
//          PrinterDriver printerDriver = result.getDriver();
//
//              if (result.getError().getCode() != OpenChannelError.ErrorCode.NoError) {
//              Log.d("error == ", result.getError().getCode().toString());
//              return;
//          }
//            printerDriver.closeChannel();
//           if(printError.getCode() != PrintError.ErrorCode.NoError){
//              // pdf page error
//              Log.d("error = ", String.valueOf(printError.getCode()));
//          }
//        PrintError printError = printerDriver.printPDF(file,num,printSettings);
//PrintSettings printSettings = new QLPrintSettings(PrinterModel.QL_1100);

      }



}