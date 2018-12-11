package com.elise.userinfocenter.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class QuickResCodeGenerator {

    private static final MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
    private static Map<EncodeHintType, Object> hints ;
    private static Integer width;
    private static Integer height;

    public QuickResCodeGenerator(Map<EncodeHintType, Object> hints,Integer width,Integer height){
           this.hints = hints;
           this.width = width;
           this.height = height;
    }
    public void writeToStream(String imageFormat, String qrMsg, OutputStream out) throws WriterException, IOException {
        BitMatrix bitMatrix = multiFormatWriter.encode(qrMsg, BarcodeFormat.QR_CODE, width, height, hints);
        MatrixToImageWriter.writeToStream(bitMatrix,imageFormat,out);
    }

    public BufferedImage encode(String imageFormat, String qrMsg) throws WriterException {
        BitMatrix bitMatrix = multiFormatWriter.encode(qrMsg, BarcodeFormat.QR_CODE, width, height, hints);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

}
