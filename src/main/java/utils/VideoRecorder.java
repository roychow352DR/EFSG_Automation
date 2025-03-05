package utils;

import org.monte.media.Format;
import org.monte.media.Registry;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.monte.media.FormatKeys.*;
import static org.monte.media.VideoFormatKeys.*;

public class VideoRecorder extends ScreenRecorder{
    public static ScreenRecorder screenRecorder;
    public String name;

    public VideoRecorder(GraphicsConfiguration cfg, Rectangle captureArea, Format fileFormat,
                              Format screenFormat, Format mouseFormat, Format audioFormat, File movieFolder, String name)
            throws IOException, AWTException {
        super(cfg, captureArea, fileFormat, screenFormat, mouseFormat, audioFormat, movieFolder);
        this.name = name;
    }
    @Override
    protected File createMovieFile(Format fileFormat) throws IOException {

        if (!movieFolder.exists()) {
            movieFolder.mkdirs();
        } else if (!movieFolder.isDirectory()) {
            throw new IOException("\"" + movieFolder + "\" is not a directory.");
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");
        return new File(movieFolder,
                name + "-" + dateFormat.format(new Date()) + "." + Registry.getInstance().getExtension(fileFormat));
    }
    public static ScreenRecorder startRecording(String fileName) throws Exception {
        GraphicsConfiguration gc = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration();

        File file = new File("./test-videos");
        if (!file.exists()) {
            file.mkdirs(); // Create directory if it doesn't exist
        }
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;

        Rectangle captureSize = new Rectangle(0, 0, width, height);

        screenRecorder = new VideoRecorder(gc,captureSize,
                new Format(MediaTypeKey, MediaType.FILE, MimeTypeKey, MIME_AVI),
                new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                        CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE, DepthKey, (int) 24, FrameRateKey,
                        Rational.valueOf(15), QualityKey, 1.0f, KeyFrameIntervalKey, (int) (15 * 60)),
                new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, "black", FrameRateKey, Rational.valueOf(30)),
                null,file,fileName);

        screenRecorder.start();

        return screenRecorder;
    }

    public static File stopRecording(ScreenRecorder screenRecorder) throws Exception {
        screenRecorder.stop();
        return screenRecorder.getCreatedMovieFiles().get(0);
    }

    public static void deleteRecords(String path) {
        File directory = new File(path);
        File[] files = directory.listFiles();
        for (File file : files) {
            file.delete();
        }
    }
}
