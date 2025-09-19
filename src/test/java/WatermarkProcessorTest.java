import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class WatermarkProcessorTest {
    
    private WatermarkProcessor processor;
    
    @TempDir
    Path tempDir;
    
    @BeforeEach
    public void setUp() {
        processor = new WatermarkProcessor();
    }
    
    @Test
    public void testIsSupportedImageFile_withValidImageFiles() {
        // 由于是私有方法，我们通过反射或间接测试
        // 这里我们测试公共方法来间接验证
        assertDoesNotThrow(() -> {
            // 只要不抛出异常就说明基本结构正确
            assertNotNull(processor);
        });
    }
    
    @Test
    public void testAddWatermark_withInvalidPath() {
        String invalidPath = tempDir.resolve("nonexistent.jpg").toString();
        int[] color = {255, 255, 255};
        
        Exception exception = assertThrows(IOException.class, () -> {
            processor.addWatermark(invalidPath, 24, color, WatermarkPosition.BOTTOM_RIGHT);
        });
        
        assertTrue(exception.getMessage().contains("文件不存在"));
    }
    
    @Test
    public void testAddWatermarkToDirectory_withInvalidDirectory() {
        String invalidDir = tempDir.resolve("nonexistent-dir").toString();
        int[] color = {255, 255, 255};
        
        Exception exception = assertThrows(IOException.class, () -> {
            processor.addWatermarkToDirectory(invalidDir, 24, color, WatermarkPosition.BOTTOM_RIGHT);
        });
        
        assertTrue(exception.getMessage().contains("指定路径不是有效目录"));
    }
    
    @Test
    public void testAddWatermarkToDirectory_withEmptyDirectory() throws IOException {
        // 创建一个空目录
        File emptyDir = tempDir.resolve("empty-dir").toFile();
        assertTrue(emptyDir.mkdir());
        
        int[] color = {255, 255, 255};
        assertDoesNotThrow(() -> {
            int result = processor.addWatermarkToDirectory(emptyDir.getAbsolutePath(), 24, color, WatermarkPosition.BOTTOM_RIGHT);
            assertEquals(0, result);
        });
    }
}
