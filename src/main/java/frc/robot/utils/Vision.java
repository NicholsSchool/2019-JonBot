package frc.robot.utils;

import java.util.ArrayList;
import java.util.Arrays;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class Vision {

    /**
     * Grabs cargo lines from an image using contour analysis
     */
    public static void grabLinesC(Mat src, Mat dst) {

        if (src.empty()) {
            return;
        }

        // Convert to grayscale
        Imgproc.cvtColor(src, dst, Imgproc.COLOR_BGR2GRAY);

        // Separate bright areas from dark areas
        Imgproc.threshold(dst, dst, 127, 255, Imgproc.THRESH_BINARY);

        // Find contours
        ArrayList<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(dst, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        // Approximate contours with rotated rectangles
        src.copyTo(dst);
        for (MatOfPoint contour : contours) {

            // Only include large rectangles
            if (Imgproc.contourArea(contour) > 750) {
                RotatedRect rect = Imgproc.minAreaRect(new MatOfPoint2f(contour.toArray()));

                // Draw the rect
                drawRotatedRect(dst, rect, new Scalar(0, 255, 0), 2);
            }
        }
    }

    private static void drawRotatedRect(Mat image, RotatedRect rotatedRect, Scalar color, int thickness) {
        // Get the vertices of the RotatedRect as a MatOfPoint
	    Point[] vertices = new Point[4];
	    rotatedRect.points(vertices);
        MatOfPoint points = new MatOfPoint(vertices);
        
        // Draw
	    Imgproc.drawContours(image, Arrays.asList(points), -1, color, thickness);
	}

    /**
     * Grabs cargo lines from an image using the Probabilistic Hough Line Transform.
     *
     * @deprecated use {@link #grabLinesC()} instead.
     */
    @Deprecated
    public static void grabLinesHT(Mat src, Mat dst) {

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
        for (int i = 0; i < lines.rows(); i++) {
            double[] l = lines.get(i, 0);

            // Make sure each line begins at the top point and goes to the bottom point
            if (l[1] > l[3]) {
                double temp = l[0];
                l[0] = l[2];
                l[2] = temp;

                temp = l[1];
                l[1] = l[3];
                l[3] = temp;
            }

            Imgproc.line(dst, new Point(l[0], l[1]), new Point(l[2], l[3]), new Scalar(0, 0, 255), 2, Imgproc.LINE_AA,
                    0);
        }
    }
}