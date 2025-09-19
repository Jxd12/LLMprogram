import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WatermarkPositionTest {
    
    @Test
    public void testWatermarkPositionValues() {
        // 测试枚举值是否存在
        assertNotNull(WatermarkPosition.TOP_LEFT);
        assertNotNull(WatermarkPosition.CENTER);
        assertNotNull(WatermarkPosition.BOTTOM_RIGHT);
    }
    
    @Test
    public void testWatermarkPositionOrdinal() {
        // 测试枚举顺序
        assertEquals(0, WatermarkPosition.TOP_LEFT.ordinal());
        assertEquals(1, WatermarkPosition.CENTER.ordinal());
        assertEquals(2, WatermarkPosition.BOTTOM_RIGHT.ordinal());
    }
}
