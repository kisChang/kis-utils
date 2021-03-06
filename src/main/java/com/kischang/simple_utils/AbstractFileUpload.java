package com.kischang.simple_utils;

import com.kischang.simple_utils.formbean.ResponseData;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传
 *
 * @author KisChang
 * @version 1.0
 */
public abstract class AbstractFileUpload {

    /**
     * 方法调用前先执行
     */
    protected abstract boolean beforeMethod();

    /**
     * 获取可用文件类型
     * @return Map (content-type, .文件后缀)
     */
    protected abstract Map<String, String> getUploadFileType();

    /**
     * @param accessPath    访问路径
     * @return              存储路径
     */
    protected abstract String getUploadPath(String accessPath);


    /**
     * 文件上传完成后调用的方法
     */
    protected boolean parseUploadFile(Long uId, String accessPath, File saveFile, HttpServletRequest request) {
        return true;
    }

    /**
     * @param fileName  文件名称
     * @return          存储路径
     */
    protected abstract String getAccessPath(String fileName);

    protected abstract Long getUid(HttpServletRequest request);

    /**
     * @param uId       接收的用户ID
     * @return  true 可操作 false 不可操作
     */
    protected abstract boolean checkUId(Long uId, HttpServletRequest request);

    /**
     * @return 文件名称
     */
    protected abstract String getFileName(MultipartFile file, HttpServletRequest request);


    @RequestMapping("/uploadPic")
    public
    @ResponseBody
    ResponseData uploadFilePic(@RequestParam MultipartFile file, HttpServletRequest request){
        return uploadFile(file, request);
    }

    @RequestMapping("/upload")
    public
    @ResponseBody
    ResponseData uploadFile(@RequestParam MultipartFile file, HttpServletRequest request){
        if (!beforeMethod()){
            return new ResponseData(false, "不允许的操作！", -1);
        }
        if (file == null){
            return new ResponseData(false, "文件为空！", -1);
        }
        if (!getUploadFileType().containsKey(file.getContentType())){
            return new ResponseData(false, "请选择[" + getUploadFileType().values() + "]类型的图片！", -1);
        }
        String fileName = getFileName(file, request);
        String accessPath = f2a(getAccessPath(fileName));
        String savePath = a2f(getUploadPath(accessPath));
        OutputStream os = null;
        File saveFile;
        try {
            saveFile = new File(savePath);
            if(!saveFile.getParentFile().exists()){
                saveFile.getParentFile().mkdirs();
            }
            os = new FileOutputStream(saveFile);
            IOUtils.copy(file.getInputStream(), os);
        } catch (IOException e) {
            return new ResponseData(false, "保存文件失败！", -1);
        } finally {
            IOUtils.closeQuietly(os);
        }
        Map<String,Object> map = new HashMap<>();
        map.put("picName", fileName);
        Long uId = getUid(request);
        if(uId != null && uId > 0){
            map.put("uId", uId);
        }
        map.put("accessPath", accessPath);
        if (parseUploadFile(uId, accessPath, saveFile, request)){
            return new ResponseData<Map<String,Object>>(true, accessPath, 0).setContent(map);
        }else {
            return new ResponseData<Map<String,Object>>(false, "上传失败！", -2);
        }
    }

    /**删除已上传的文件*/
    @RequestMapping("/uploadPicDel")
    public
    @ResponseBody
    ResponseData uploadPicDel(Long uId, String picName, HttpServletRequest request){
        return uploadFileDel(uId, picName, request);
    }

    @RequestMapping("/uploadDel")
    public
    @ResponseBody
    ResponseData uploadFileDel(Long uId, String picName, HttpServletRequest request){
        if (!beforeMethod()){
            return new ResponseData(false, "不允许的操作！", -1);
        }
        if (picName == null || "".equals(picName)){
            return new ResponseData(false, "文件名错误！", -1);
        }
        if (!checkUId(uId, request)){
            //实际为无操作权限
            return new ResponseData(false, "没有找到文件！", -2);
        }
        String filePath = a2f(getUploadPath(getAccessPath(picName)));
        File file = new File(filePath);
        if (!file.exists()){
            return new ResponseData(false, "没有找到文件！", -2);
        }
        if (file.delete()){
            return new ResponseData(true, "删除成功！", 0);
        }else{
            return new ResponseData(false, "删除失败，请稍后重试！", -3);
        }
    }

    private static String f2a(String str){
        if (str == null || "".equals(str)){
            return null;
        }
        if (File.separator.equalsIgnoreCase("/")){
            return str;
        }else {
            return str.replaceAll("\\\\", "/");
        }
    }

    private static String a2f(String str){
        if (str == null || "".equals(str)){
            return null;
        }
        if (File.separator.equalsIgnoreCase("/")){
            return str;
        }else {
            return str.replaceAll("/", "\\\\");
        }
    }

}
