package com.itcag.util;

import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;

/**
 *
 * @author IT Consulting Alicja Grużdź (Nahum)
 */
public final class SystemStats {

    public final static Double getCpuUsage() {

        try {

            MBeanServer mbs    = ManagementFactory.getPlatformMBeanServer();
            ObjectName name    = ObjectName.getInstance("java.lang:type=OperatingSystem");
            AttributeList list = mbs.getAttributes(name, new String[]{ "ProcessCpuLoad" });
        
            if (list.isEmpty()) return Double.NaN;

            // usually takes a couple of seconds before we get real values
            for (int i = 0; i < 5; i++) {
                
                Attribute att = (Attribute) list.get(0);
                Double value = (Double) att.getValue();

                if (value != -1.0) {
                    BigDecimal retVal = new BigDecimal(value);
                    retVal = retVal.setScale(2, RoundingMode.HALF_UP);
                    return retVal.doubleValue();
                }

                Thread.sleep(1000);
                
            }

            return Double.NaN;

        } catch (Exception ex) {
            return Double.NaN;
        }

    }
    
    public final static double getJVMMemory() {
        return (double) Runtime.getRuntime().maxMemory() / (1024 * 1024);
    }
    
    public final static double getUsedJVMMemory() {
        return (double) Runtime.getRuntime().freeMemory() / (1024 * 1024);
    }
    
    public final static Double getJVMMemoryUsage() {

        double max = (double) Runtime.getRuntime().maxMemory() / (1024 * 1024);
        double free = (double) Runtime.getRuntime().freeMemory() / (1024 * 1024);
        
        double used = max - free;
        
        BigDecimal retVal = new BigDecimal((double) used / max);
        retVal = retVal.setScale(2, RoundingMode.HALF_UP);
        
        return retVal.doubleValue();

    }
    
    public final static double getMachineMemory() {
        OperatingSystemMXBean bean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        return (double) bean.getTotalPhysicalMemorySize() / (1024 * 1024);
    }
    
    public final static double getUsedMachineMemory() {
        OperatingSystemMXBean bean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        return (double) bean.getFreePhysicalMemorySize() / (1024 * 1024);
    }
    
    public final static Double getMachineMemoryUsage() {

        OperatingSystemMXBean bean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        double max = (double) bean.getTotalPhysicalMemorySize() / (1024 * 1024);
        double free = (double) bean.getFreePhysicalMemorySize() / (1024 * 1024);
        
        double used = max - free;
        
        BigDecimal retVal = new BigDecimal((double) used / max);
        retVal = retVal.setScale(2, RoundingMode.HALF_UP);
        
        return retVal.doubleValue();

    }

}
