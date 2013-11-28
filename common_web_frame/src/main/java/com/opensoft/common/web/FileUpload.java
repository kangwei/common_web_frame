/**
 * ClassName: FileUpload
 * CopyRight: TalkWeb
 * Date: 13-10-12
 * Version: 1.0
 */
package com.opensoft.common.web;

import com.opensoft.common.utils.CollectionUtils;
import com.opensoft.common.utils.ImageUtils;
import com.opensoft.common.utils.StringUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description :
 *
 * @author : KangWei
 */
public class FileUpload {
    private static final Logger log = LoggerFactory.getLogger(FileUpload.class);

    /**
     * 文件上传
     *
     * @param request    请求
     * @param uploadPath 上传路径
     * @return 上传文件地址
     * @throws Exception 异常
     */
    public static Map<String, Object> upload(HttpServletRequest request, String uploadPath) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        String uploadFileName = null;
        String realPath = request.getSession().getServletContext().getRealPath("/") + uploadPath;
        File uploadDir = mkdirs(realPath);

        request.setCharacterEncoding("UTF-8");
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setRepository(uploadDir);
        //设置创建缓冲大小，单位：byte
        factory.setSizeThreshold(100 * 1024 * 1024);

        ServletFileUpload servletFileUpload = new ServletFileUpload(factory);

        List<FileItem> items = (List<FileItem>) servletFileUpload.parseRequest(request);
        if (CollectionUtils.isNotEmpty(items)) {
            for (FileItem item : items) {
                if (!item.isFormField() && StringUtils.isNotEmpty(item.getName())) {
                    if (log.isDebugEnabled()) {
                        log.debug("上传文件名：{}，大小：{}，类型：{}", new Object[]{item.getName(), item.getSize(), item.getContentType()});
                    }

                    String fileName;
                    if (item.getName().indexOf(".") > 0) {
                        fileName = System.currentTimeMillis() + "." + StringUtils.substringAfterLast(item.getName(), ".");
                    } else {
                        fileName = System.currentTimeMillis() + "";
                    }
                    File uploadFile = new File(realPath + File.separator + fileName);
                    item.write(uploadFile);
                    uploadFileName = uploadPath + "/" + uploadFile.getName();
                    result.put(item.getFieldName(), uploadFileName);
                } else {
                    result.put(item.getFieldName(), item.getString("UTF-8"));
                }
            }
        }

        return result;
    }

    /**
     * 文件上传
     *
     * @param request    请求
     * @param uploadPath 上传路径
     * @return 上传文件地址
     * @throws Exception 异常
     */
    public static Map<String, Object> uploadImgAndCompress(HttpServletRequest request, String uploadPath, int width, int height) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        String uploadFileName = null;
        String realPath = request.getSession().getServletContext().getRealPath("/") + uploadPath;
        File uploadDir = mkdirs(realPath);

        request.setCharacterEncoding("UTF-8");
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setRepository(uploadDir);
        //设置创建缓冲大小，单位：byte
        factory.setSizeThreshold(100 * 1024 * 1024);

        ServletFileUpload servletFileUpload = new ServletFileUpload(factory);

        List<FileItem> items = (List<FileItem>) servletFileUpload.parseRequest(request);
        if (CollectionUtils.isNotEmpty(items)) {
            for (FileItem item : items) {
                if (!item.isFormField() && StringUtils.isNotEmpty(item.getName())) {
                    if (log.isDebugEnabled()) {
                        log.debug("上传文件名：{}，大小：{}，类型：{}", new Object[]{item.getName(), item.getSize(), item.getContentType()});
                    }

                    String fileName;
                    if (item.getName().indexOf(".") > 0) {
                        fileName = System.currentTimeMillis() + "." + StringUtils.substringAfterLast(item.getName(), ".");
                    } else {
                        fileName = System.currentTimeMillis() + "";
                    }
                    File uploadFile = new File(realPath + File.separator + fileName);
                    BufferedImage bufferedImage = ImageUtils.compressPic(item.getInputStream(), width, height);
//                    item.write(uploadFile);
                    ImageUtils.write(bufferedImage, StringUtils.substringAfterLast(fileName, "."), new FileOutputStream(uploadFile));
                    uploadFileName = uploadPath + "/" + uploadFile.getName();
                    result.put(item.getFieldName(), uploadFileName);
                } else {
                    result.put(item.getFieldName(), item.getString("UTF-8"));
                }
            }
        }

        return result;
    }

    private static File mkdirs(String realPath) {
        File uploadDir = new File(realPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        return uploadDir;
    }
}
