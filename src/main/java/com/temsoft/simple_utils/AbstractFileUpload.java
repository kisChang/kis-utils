package com.temsoft.simple_utils;

import com.temsoft.simple_utils.formbean.ResponseData;
import com.temsoft.simple_utils.utils.MyUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

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

    protected abstract Map<String,String> getUploadFileType();

    protected abstract String getUploadPath();


    @RequestMapping("/uploadPic")
    public
    @ResponseBody
    ResponseData uploadGoodsPic(@RequestParam MultipartFile file){
        if (file == null){
            return new ResponseData(false, "文件为空！", -1);
        }
        if (!getUploadFileType().containsKey(file.getContentType())){
            return new ResponseData(false, "请选择jpeg、jpg、png类型的图片！", -1);
        }
        String fileName = System.currentTimeMillis() + getUploadFileType().get(file.getContentType());
        String accessPath = getUploadPath()
                + File.separator
                + fileName ;
        String savePath = MyUtils.getWebPath(accessPath);
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
    ResponseData uploadGoodsPicDel(String picName){
        if (picName == null || "".equals(picName)){
            return new ResponseData(false, "文件名错误！", -1);
        }
        String filePath = MyUtils.getWebPath(getUploadPath(), File.separator, picName);
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
