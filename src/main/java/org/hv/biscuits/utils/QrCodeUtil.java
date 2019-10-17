package org.hv.biscuits.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.codec.CharEncoding;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wujianchuan
 */
public class QrCodeUtil {
    private static volatile QrCodeUtil instance;

    private QrCodeUtil() {
    }

    public static QrCodeUtil getInstance() {
        if (instance == null) {
            synchronized (QrCodeUtil.class) {
                if (instance == null) {
                    instance = new QrCodeUtil();
                }
            }
        }
        return instance;
    }

    public void encodeToOutputStream(String code, int width, int height, String imageFormat, OutputStream outputStream) throws IOException, WriterException {
        BitMatrix bitMatrix = this.encode(code, width, height);
        MatrixToImageWriter.writeToStream(bitMatrix, imageFormat, outputStream);
    }

    public byte[] encodeToByteArray(String code, int width, int height, String imageFormat) throws WriterException, IOException {
        BitMatrix bitMatrix = this.encode(code, width, height);
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, imageFormat, pngOutputStream);
        return pngOutputStream.toByteArray();
    }

    public BufferedImage encodeToBufferedImage(String code, int width, int height) throws WriterException, IOException {
        BitMatrix bitMatrix = this.encode(code, width, height);
        return MatrixToImageWriter.toBufferedImage(bitMatrix, new MatrixToImageConfig(Color.BLACK.getRGB(), Color.WHITE.getRGB()));
    }

    public void encodeAndSave(String code, int width, int height, Path path, String imageFormat) throws WriterException, IOException {
        BitMatrix bitMatrix = this.encode(code, width, height);
        MatrixToImageWriter.writeToPath(bitMatrix, imageFormat, path);
    }

    private BitMatrix encode(String code, int width, int height) throws WriterException {
        final Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, CharEncoding.UTF_8);
        hints.put(EncodeHintType.MARGIN, 0);
        return new QRCodeWriter().encode(code, BarcodeFormat.QR_CODE, width, height, hints);
    }
}
