package com.temsoft.simple_utils;

import com.temsoft.simple_utils.formbean.ResponseData;
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
     * @param fileName  文件名称
     * @return          存储路径
     */
    protected abstract String getAccessPath(String fileName);

    /**
     * @param uId       接收的用户ID
     * @return  true 可操作 false 不可操作
     */
    protected abstract boolean checkUId(long uId, HttpServletRequest request);


    /**
     * @return 文件名称
     */
    protected abstract String getFileName(MultipartFile file, HttpServletRequest request);

    @RequestMapping("/uploadPic")
    public
    @ResponseBody
    ResponseData uploadGoodsPic(@RequestParam MultipartFile file, HttpServletRequest request){
        if (file == null){
            return new ResponseData(false, "文件为空！", -1);
        }
        if (!getUploadFileType().containsKey(file.getContentType())){
            return new ResponseData(false, "请选择jpeg、jpg、png类型的图片！", -1);
        }
        String fileName = getFileName(file, request);
        String accessPath = getAccessPath(fileName);
        String savePath = getUploadPath(accessPath);
        OutputStream os = null;
        try {
            File saveFile = new File(savePath);
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
        map.put("accessPath", accessPath);
        return new ResponseData<Map<String,Object>>(true, accessPath, 0).setContent(map);
    }

    /**删除已上传的文件*/
    @RequestMapping("/uploadPicDel")
    public
    @ResponseBody
    ResponseData uploadGoodsPicDel(Long uId, String picName, HttpServletRequest request){
        if (picName == null || "".equals(picName)){
            return new ResponseData(false, "文件名错误！", -1);
        }
        if (!checkUId(uId, request)){
            //实际为无操作权限
            return new ResponseData(false, "没有找到文件！", -2);
        }
        String filePath = getUploadPath(getAccessPath(picName));
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

}
