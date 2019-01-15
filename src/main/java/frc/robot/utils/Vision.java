package frc.robot.utils;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class Vision {

    public static void grabLines(Mat src, Mat dst) {

        if (src.empty()) {
            return;
        }

        // Convert to grayscale
        Imgproc.cvtColor(src, dst, Imgproc.COLOR_BGR2GRAY);

        // Edge detection
        Imgproc.Canny(dst, dst, 350, 350, 3, false);

        // Slight blur to get rid of jagged edges
        Imgproc.blur(dst, dst, new Size(2, 2));

        // Probabilistic Line Transform
        Mat lines = new Mat();
        Imgproc.HoughLinesP(dst, lines, 1, Math.PI / 180, 60, 40, 10);

        // Convert grayscale back to BGR
        Imgproc.cvtColor(dst, dst, Imgproc.COLOR_GRAY2BGR);

        // Draw the lines
        for (int x = 0; x < lines.rows(); x++) {
            double[] l = lines.get(x, 0);
            Imgproc.line(dst, new Point(l[0], l[1]), new Point(l[2], l[3]), new Scalar(0, 0, 255), 2, Imgproc.LINE_AA,
                    0);
        }
    }

    private static void fixOrientation(Mat lines) {

    }

    private static void removeIntersectingLines(Mat lines) {
        
    }

    private static void mergeParallelLines(Mat lines, double threshold, int minLineGap, int maxLineGap) {
        
    }
}