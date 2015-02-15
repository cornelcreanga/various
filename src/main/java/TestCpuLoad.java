import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;


public class TestCpuLoad {

    public static void main(String[] args) {
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        System.out.println(osBean.getAvailableProcessors());
        System.out.println(osBean.getSystemLoadAverage());
    }
}
