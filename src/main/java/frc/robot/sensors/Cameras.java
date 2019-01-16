package frc.robot.sensors;

import org.opencv.core.Mat;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.utils.Vision;

public class Cameras extends Subsystem {

    private CvSink videoSink;

    @Override
    protected void initDefaultCommand() {

    }

    public Cameras() {
        // start capturing video from camera
        CameraServer.getInstance().startAutomaticCapture().setResolution(Constants.VIDEO_RESOLUTION_X,
                Constants.VIDEO_RESOLUTION_Y);

        // get a reference to the video feed as a CvSink
        videoSink = CameraServer.getInstance().getVideo();
    }

    public void startLineProcessing(String outputName) {
        // start an output stream with the given name
        CvSource outputStream = CameraServer.getInstance().putVideo(outputName, Constants.VIDEO_RESOLUTION_X,
                Constants.VIDEO_RESOLUTION_Y);


        // start a new thread as not to block the main thread
        new Thread(() -> {
            SmartDashboard.putBoolean("Vision running", true);

            Mat src = new Mat();

            while (!Thread.interrupted()) {
                videoSink.grabFrame(src);
                Vision.grabLinesC(src, src);
                outputStream.putFrame(src);
            }

            SmartDashboard.putBoolean("Vision running", false);
        }).start();
    }

}