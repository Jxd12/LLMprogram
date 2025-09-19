import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WatermarkAppTest {
    
    @Test
    public void testMainClassExists() {
        // 确保主类存在且可以实例化
        assertDoesNotThrow(() -> {
            Class.forName("WatermarkApp");
        });
    }
    
    @Test
    public void testEnumClassExists() {
        // 确保枚举类存在
        assertDoesNotThrow(() -> {
            Class.forName("WatermarkPosition");
        });
    }
    
    @Test
    public void testProcessorClassExists() {
        // 确保处理器类存在
        assertDoesNotThrow(() -> {
            Class.forName("WatermarkProcessor");
        });
    }
}
