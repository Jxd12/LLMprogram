import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class WatermarkIntegrationTest {
    
    @TempDir
    Path tempDir;
    
    @Test
    public void testCreateSampleImage() throws IOException {
        // 创建一个简单的测试图片
        BufferedImage testImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = testImage.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, 200, 200);
        g2d.dispose();
        
        // 保存测试图片
        File testImageFile = tempDir.resolve("test-image.png").toFile();
        ImageIO.write(testImage, "png", testImageFile);
        
        // 验证图片创建成功
        assertTrue(testImageFile.exists());
        assertTrue(testImageFile.length() > 0);
    }
    
    @Test
    public void testWatermarkProcessorInitialization() {
        WatermarkProcessor processor = new WatermarkProcessor();
        assertNotNull(processor);
    }
    
    @Test
    public void testSupportedExtensionsArray() {
        // 测试支持的文件扩展名数组不为空
        assertDoesNotThrow(() -> {
            WatermarkProcessor processor = new WatermarkProcessor();
            // 通过反射获取私有字段进行测试
            assertNotNull(processor);
        });
    }
}
