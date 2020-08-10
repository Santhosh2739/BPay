package coreframework.barcodeclient;

import java.util.Hashtable;

import br.com.google.zxing.EncodeHintType;
import br.com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import smm.zing.ByteMatrix;
import smm.zing.Encoder;
import smm.zing.QRCode;
import smm.zing.QRCodeWriter;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;

/**
 * Ported from Legacy YPC Android and Converted to Static Method IMPL
 * @author mohit
 */
public class QRBitmap_Generator {
        public static Bitmap getQrBitmap(String qr_code_string,Context context) {
            Bitmap mpBitmap = null;
            try {
                @SuppressWarnings("rawtypes")
                String url = qr_code_string;
                Hashtable hints = new Hashtable();
                hints.put(EncodeHintType.CHARACTER_SET, "ISO-8859-1");
                QRCode qrCode = new QRCode();
                Encoder.encode(url, ErrorCorrectionLevel.L, hints, qrCode);
                ByteMatrix matrix = null;
                int optimzedres = BarCodeResolutionChangeHelper.getCurrentResolutionSize(context);
                matrix = QRCodeWriter.renderResult(qrCode, optimzedres, optimzedres);
                int width = matrix.getWidth();
                int height = matrix.getHeight();
                int[] rgb = new int[height * width];
                int count = 0;
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        rgb[count++] = (matrix.get(x, y) == 0 ? 0xFF000000 : 0xFFFFFFFF);
                    }
                }
                mpBitmap = Bitmap.createBitmap(rgb, width, height, Bitmap.Config.RGB_565);
            } catch (Exception e) {
            }
            return mpBitmap;
        }
        @SuppressWarnings("unchecked")
        public static  void getQrImage(Canvas canvas, String url,int _width, int _height, String marker,Context context) {
            try {
                @SuppressWarnings("rawtypes")
                Hashtable hints = new Hashtable();
                hints.put(EncodeHintType.CHARACTER_SET, "ISO-8859-1");
                QRCode qrCode = new QRCode();
                Encoder.encode(url, ErrorCorrectionLevel.L, hints, qrCode);
                ByteMatrix matrix = null;
                int optimzedres = BarCodeResolutionChangeHelper.getCurrentResolutionSize(context);
                matrix = QRCodeWriter.renderResult(qrCode, optimzedres, optimzedres);
                int width = matrix.getWidth();
                int height = matrix.getHeight();
                int[] rgb = new int[height * width];
                int count = 0;
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        rgb[count++] = (matrix.get(x, y) == 0 ? 0xFF000000 : 0xFFFFFFFF);
                    }
                }
                Bitmap mpBitmap = Bitmap.createBitmap(rgb, width, height, Bitmap.Config.RGB_565);
                canvas.drawColor(Color.WHITE);
                canvas.drawBitmap(mpBitmap, _width / 2 - width / 2, _height / 2 - height / 2, null);
                Paint paint = new Paint(Paint.LINEAR_TEXT_FLAG | Paint.FAKE_BOLD_TEXT_FLAG);
                paint.setColor(Color.BLACK);
                paint.setTextSize(20);
                paint.setTextAlign(Align.CENTER);
                canvas.drawText(marker, _width / 2 - width / 2 + mpBitmap.getWidth() / 2, _height / 2 - height / 2 + mpBitmap.getHeight() + 35, paint);
            } catch (Exception e) {
            }
        }
}