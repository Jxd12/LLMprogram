import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WatermarkProcessor {
    
    private static final String[] SUPPORTED_EXTENSIONS = {".jpg", ".jpeg", ".png", ".bmp", ".gif"};
    
    public void addWatermark(String imagePath, int fontSize, int[] color, WatermarkPosition position) 
            throws IOException, ImageProcessingException {
        File imageFile = new File(imagePath);
        
        if (!imageFile.exists()) {
            throw new IOException("文件不存在: " + imagePath);
        }
        
        if (!isSupportedImageFile(imageFile)) {
            throw new IOException("不支持的文件格式: " + imagePath);
        }
        
        processImageFile(imageFile, fontSize, color, position);
    }
    
    public int addWatermarkToDirectory(String directoryPath, int fontSize, int[] color, WatermarkPosition position) 
            throws IOException, ImageProcessingException {
        File directory = new File(directoryPath);
        
        if (!directory.exists() || !directory.isDirectory()) {
            throw new IOException("指定路径不是有效目录: " + directoryPath);
        }
        
        File[] files = directory.listFiles();
        if (files == null) {
            throw new IOException("无法读取目录内容: " + directoryPath);
        }
        
        int successCount = 0;
        for (File file : files) {
            if (file.isFile() && isSupportedImageFile(file)) {
                try {
                    processImageFile(file, fontSize, color, position);
                    successCount++;
                    System.out.println("已处理: " + file.getName());
                } catch (Exception e) {
                    System.err.println("处理文件失败 " + file.getName() + ": " + e.getMessage());
                }
            }
        }
        
        return successCount;
    }
    
    private void processImageFile(File imageFile, int fontSize, int[] color, WatermarkPosition position) 
            throws IOException, ImageProcessingException {
        // 读取图片
        BufferedImage originalImage = ImageIO.read(imageFile);
        BufferedImage watermarkedImage = new BufferedImage(
                originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = watermarkedImage.createGraphics();
        
        // 绘制原图
        g2d.drawImage(originalImage, 0, 0, null);
        
        // 获取拍摄时间
        String captureTime = getCaptureTime(imageFile);
        
        // 设置字体和颜色
        g2d.setColor(new Color(color[0], color[1], color[2]));
        g2d.setFont(new Font("Arial", Font.BOLD, fontSize));
        
        // 计算水印位置
        FontMetrics fontMetrics = g2d.getFontMetrics();
        int textWidth = fontMetrics.stringWidth(captureTime);
        int textHeight = fontMetrics.getHeight();
        
        int x = 0;
        int y = 0;
        
        switch (position) {
            case TOP_LEFT:
                x = 10;
                y = textHeight + 10;
                break;
            case CENTER:
                x = (originalImage.getWidth() - textWidth) / 2;
                y = (originalImage.getHeight() + textHeight) / 2;
                break;
            case BOTTOM_RIGHT:
                x = originalImage.getWidth() - textWidth - 10;
                y = originalImage.getHeight() - 10;
                break;
        }
        
        // 绘制水印
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.drawString(captureTime, x, y);
        g2d.dispose();
        
        // 创建输出目录
        File parentDir = imageFile.getParentFile();
        File outputDir = new File(parentDir, parentDir.getName() + "_watermark");
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
        
        // 保存图片
        String fileName = imageFile.getName();
        String fileNameWithoutExt = fileName.substring(0, fileName.lastIndexOf('.'));
        String fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1);
        File outputFile = new File(outputDir, fileNameWithoutExt + "_watermarked." + fileExtension);
        
        ImageIO.write(watermarkedImage, fileExtension, outputFile);
    }
    
    private String getCaptureTime(File imageFile) {
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(imageFile);
            ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
            
            if (directory != null) {
                Date date = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
                if (date == null) {
                    date = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME);
                }
                
                if (date != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    return sdf.format(date);
                }
            }
        } catch (Exception e) {
            System.err.println("读取EXIF信息时出错: " + e.getMessage());
        }
        
        // 如果无法获取EXIF信息，使用文件最后修改时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date(imageFile.lastModified()));
    }
    
    private boolean isSupportedImageFile(File file) {
        String fileName = file.getName().toLowerCase();
        for (String extension : SUPPORTED_EXTENSIONS) {
            if (fileName.endsWith(extension)) {
                return true;
            }
        }
        return false;
    }
}
