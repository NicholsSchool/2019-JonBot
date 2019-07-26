/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.sensors;

import java.util.ArrayList;

/**
 * Add your docs here.
 */
public class Scans {

    private static  ArrayList<ScanPacket> scans;
    
    public Scans()
    {
        scans = new ArrayList<ScanPacket>();
    }

    public void addScanPacket(ScanPacket s)
    {
        if(s.isEmpty())
        {
            if(scans.size() != 0 && !scans.get(scans.size() - 1).isEmpty())
                scans.add(s);
        }
        else
            scans.add(s);
    }
    public void print()
    {
        for(ScanPacket s : scans)
        {
            if(s.isEmpty())
                System.out.println("\n\n------NEW SCAN------\n\n");
            else
                System.out.println(s + "\n" );
        }
    }
    
}
