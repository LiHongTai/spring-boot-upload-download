package com.paic.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;

@Slf4j
public class FileUtil {

    /**
     * 这里传入的path为绝对路径，或者为省略 System.getProperty("user.dir") 的path
     * @param source
     * @param destination
     * @throws IOException
     */
    public static void copyFile(String source, String destination) throws IOException {
        //文件输入流
        FileInputStream fin = new FileInputStream(source);
        //文件输出流
        FileOutputStream fout = new FileOutputStream(destination);
        //获取读文件通道
        FileChannel readChannel = fin.getChannel();
        //获取写文件通道
        FileChannel writeChannel = fout.getChannel();
        //读入数据缓存
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        int len = -1;
        while ((len = readChannel.read(byteBuffer)) != -1) {
            byteBuffer.flip();
            writeChannel.write(byteBuffer);
            byteBuffer.clear();
        }

        readChannel.close();
        writeChannel.close();
    }

    /**
     * 根据指定的目录，获取目录及其子目录下的所有文件
     * 并存储到一个容器中去
     * @param dir
     * @param fileList
     */
    public static void getFileList(String dir, List<File> fileList){
        File root = new File(dir);
        if(!root.exists()){
            return;
        }
        if(root.isFile()){
            fileList.add(root);
            return;
        }
        File[] files = root.listFiles();
        for(File file : files){
            if(file.isFile()){
                fileList.add(file);
                continue;
            }
            getFileList(file.getAbsolutePath(),fileList);
        }
    }

    /**
     * 第一步：判断文件是否为空   true：返回提示为空信息   false：执行第二步
     * 第二步：判断目录是否存在   不存在：创建目录
     * 第三部：通过输出流将文件写入硬盘文件夹并关闭流
     * @param multipartFile
     * @return
     */
    public static boolean uploadFile(MultipartFile multipartFile,String uploadPath) {
        String fileName = multipartFile.getOriginalFilename();
        if(multipartFile.isEmpty()){
            log.info("上传文件" + fileName + "内容为空.");
            return false;
        }
        //上传目录
        File targetDirectory = new File(uploadPath);
        if (!targetDirectory.exists()){
            targetDirectory.mkdirs();
        }
        BufferedOutputStream outputStream = null;
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(uploadPath + fileName));
            outputStream.write(multipartFile.getBytes());
            outputStream.flush();
        } catch (FileNotFoundException e) {
            log.error("上传服务器目录下文件创建不成功，异常信息：" + e.getMessage());
            return false;
        }catch (IOException e){
            log.error("上传文件，不存在，异常信息：" + e.getMessage());
            return false;
        }
        finally {
            if(outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.error("IO流关闭异常，异常信息：" + e.getMessage());
                    return false;
                }
            }
        }
        return true;
    }

}
