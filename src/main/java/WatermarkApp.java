import java.io.File;
import java.util.Scanner;

public class WatermarkApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // 获取图片文件路径或目录路径
        System.out.print("请输入图片文件路径或目录路径: ");
        String path = scanner.nextLine();
        
        // 获取字体大小
        System.out.print("请输入字体大小 (默认24): ");
        String fontSizeInput = scanner.nextLine();
        int fontSize = fontSizeInput.isEmpty() ? 24 : Integer.parseInt(fontSizeInput);
        
        // 获取字体颜色
        System.out.print("请输入字体颜色 (RGB格式，如: 255,255,255，默认白色): ");
        String colorInput = scanner.nextLine();
        String[] colorParts = colorInput.isEmpty() ? new String[]{"255", "255", "255"} : colorInput.split(",");
        int red = Integer.parseInt(colorParts[0].trim());
        int green = Integer.parseInt(colorParts[1].trim());
        int blue = Integer.parseInt(colorParts[2].trim());
        
        // 获取水印位置
        System.out.println("请选择水印位置:");
        System.out.println("1. 左上角");
        System.out.println("2. 居中");
        System.out.println("3. 右下角");
        System.out.print("请输入选项 (1-3，默认3): ");
        String positionInput = scanner.nextLine();
        WatermarkPosition position = positionInput.isEmpty() ? WatermarkPosition.BOTTOM_RIGHT : 
                                    positionInput.equals("1") ? WatermarkPosition.TOP_LEFT :
                                    positionInput.equals("2") ? WatermarkPosition.CENTER : 
                                    WatermarkPosition.BOTTOM_RIGHT;
        
        try {
            WatermarkProcessor processor = new WatermarkProcessor();
            File file = new File(path);
            
            if (file.isFile()) {
                // 处理单个文件
                processor.addWatermark(path, fontSize, new int[]{red, green, blue}, position);
                System.out.println("水印添加成功！");
            } else if (file.isDirectory()) {
                // 处理目录中的所有图片文件
                int successCount = processor.addWatermarkToDirectory(path, fontSize, new int[]{red, green, blue}, position);
                System.out.println("处理完成！成功处理 " + successCount + " 个文件。");
            } else {
                System.err.println("指定的路径不存在！");
            }
        } catch (Exception e) {
            System.err.println("处理过程中出现错误: " + e.getMessage());
            e.printStackTrace();
        }
        
        scanner.close();
    }
}
