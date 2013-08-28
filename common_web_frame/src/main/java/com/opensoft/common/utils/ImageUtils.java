package com.opensoft.common.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * Description:
 *
 * @author KangWei
 * @Date 11-2-23
 */
public class ImageUtils {
	
    /**
     * 读取远程图片
     *
     * @param url 图片地址
     * @return BufferedImage 对象
     * @throws java.io.IOException IO异常
     */
    public static BufferedImage getBufferedImageFromUrl(String url) throws IOException {
        return ImageIO.read(new URL(url).openStream());
    }

    /**
     * 将图片写入输出流
     *
     * @param image  BufferedImage 对象
     * @param format 输出格式
     * @param output 输出流
     * @throws java.io.IOException IO异常
     */
    public static void write(BufferedImage image, String format, OutputStream output) throws IOException {
        ImageIO.write(image, format, output);
    }

    /**
     * 重定义图片的大小
     *
     * @param source     原图
     * @param destWidth  目标宽度
     * @param destHeight 目标高度
     * @return 新图
     */
    public static BufferedImage resize(BufferedImage source, int destWidth, int destHeight) {
        Image image = source.getScaledInstance(destWidth, destHeight, Image.SCALE_DEFAULT);
        BufferedImage tag = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);  //缩放图像
        Graphics2D g = tag.createGraphics();
        g.setBackground(Color.WHITE);
        g.clearRect(0, 0, destWidth, destHeight);
        g.drawImage(image, 0, 0, null); // 绘制缩小后的图
        g.dispose();

        return tag;
    }

    public static BufferedImage resize(String photoUrl, int destWidth, int destHeight) throws IOException {
        BufferedImage source = getBufferedImageFromUrl(photoUrl);
        Image image = source.getScaledInstance(destWidth, destHeight, Image.SCALE_DEFAULT);
        BufferedImage tag = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);  //缩放图像
        Graphics2D g = tag.createGraphics();
        g.setBackground(Color.WHITE);
        g.clearRect(0, 0, destWidth, destHeight);
        g.drawImage(image, 0, 0, null); // 绘制缩小后的图
        g.dispose();

        return tag;
    }

    /**
     * Graphics2D 画矩形
     *
     * @param source Graphics2D
     * @param x      横轴坐标
     * @param y      纵轴坐标
     * @param width  宽度
     * @param height 高度
     * @return Graphics2D Graphics2D
     */
    public static Graphics2D drawRect(Graphics2D source, double x, double y, double width, double height, Color color) {
        Rectangle2D rect = new Rectangle2D.Double(x, y, width, height);
        source.setColor(color);
        source.fill(rect);
        return source;
    }

    /**
     * Graphics2D 画线
     *
     * @param source Graphics2D
     * @param x      起始横坐标
     * @param y      起始纵坐标
     * @param x1     结束横坐标
     * @param y1     结束纵坐标
     * @param color  线体颜色
     * @return Graphics2D
     */
    public static Graphics2D drawLine(Graphics2D source, int x, int y, int x1, int y1, Color color) {
        source.setColor(color);
        source.drawLine(x, y, x1, y1);
        return source;
    }

    /**
     * Graphics2D 写字
     *
     * @param source Graphics2D
     * @param str    要输出的string
     * @param x      横坐标
     * @param y      纵坐标
     * @param color  颜色
     * @param font   字体
     * @return Graphics2D
     */
    public static Graphics2D drawString(Graphics2D source, String str, int x, int y, Color color, Font font) {
        source.setColor(color);
        source.setFont(font);
        source.drawString(str, x, y);
        return source;
    }

    /**
     * Graphics2D 写字
     *
     * @param source Graphics2D
     * @param str    要输出的string
     * @param x      横坐标
     * @param y      纵坐标
     * @param color  颜色
     * @return Graphics2D
     */
    public static Graphics2D drawString(Graphics2D source, String str, int x, int y, Color color) {
        source.setColor(color);
        source.drawString(str, x, y);
        return source;
    }
	/**
	 * 图片等比缩放,先要设置宽和高
	 * @param image
	 * @return
	 * @throws java.io.IOException
	 */
	public static BufferedImage compressPic(InputStream image,int outputWidth,int outputHeight) throws IOException{
		Image img = ImageIO.read(image);
		if(img.getWidth(null) == -1){
			return null;
		}
		int newWidth; int newHeight;
		// 为等比缩放计算输出的图片宽度及高度
		double rate1 = ((double) img.getWidth(null)) / (double) outputWidth;
        double rate2 = ((double) img.getHeight(null)) / (double) outputHeight;
        // 根据缩放比率大的进行缩放控制
        double rate = rate1 > rate2 ? rate1 : rate2;
        newWidth = (int) (((double) img.getWidth(null)) / rate);
        newHeight = (int) (((double) img.getHeight(null)) / rate);
        BufferedImage tag = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        System.out.println("rate====" + rate);
        System.out.println("Width====" + img.getWidth(null) + "Height====" + img.getHeight(null));
        System.out.println("newWidth====" + newWidth + "newHeight====" + newHeight);
        /*
         * Image.SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的
         * 优先级比速度高 生成的图片质量比较好 但速度慢
         */
        tag.getGraphics().drawImage(img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH), 0, 0, null);
		return tag;
	}
	/**
	 * 图片等比缩放,先要设置宽和高
	 * @param image
	 * @return
	 * @throws java.io.IOException
	 */
	public static BufferedImage compressPic(BufferedImage image,int outputWidth,int outputHeight) throws IOException{
		if(image == null){
			return null;
		}
		int newWidth; int newHeight;
		// 为等比缩放计算输出的图片宽度及高度
		double rate1 = ((double) image.getWidth(null)) / (double) outputWidth;
        double rate2 = ((double) image.getHeight(null)) / (double) outputHeight;
        // 根据缩放比率大的进行缩放控制
        double rate = rate1 > rate2 ? rate1 : rate2;
        newWidth = (int) (((double) image.getWidth(null)) / rate);
        newHeight = (int) (((double) image.getHeight(null)) / rate);
        BufferedImage tag = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        /*
         * Image.SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的
         * 优先级比速度高 生成的图片质量比较好 但速度慢
         */
        tag.getGraphics().drawImage(image.getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT), 0, 0, null);
		return tag;
	}
	/**
	 * 返回图片缩放比例
	 * @param image
	 * @return
	 * @throws java.io.IOException
	 */
	public static double getRate(InputStream image,int outputWidth,int outputHeight) throws IOException{
		Image img = ImageIO.read(image);
		if(img.getWidth(null) == -1){
			return 0;
		}
		// 为等比缩放计算输出的图片宽度及高度  
		double rate1 = ((double) img.getWidth(null)) / (double) outputWidth;   
        double rate2 = ((double) img.getHeight(null)) / (double) outputHeight;
        // 根据缩放比率大的进行缩放控制   
        return rate1 > rate2 ? rate1 : rate2;
	}
	
    

}
