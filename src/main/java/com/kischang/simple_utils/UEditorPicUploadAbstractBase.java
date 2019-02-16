package com.kischang.simple_utils;

import com.baidu.ueditor.define.AppInfo;
import com.baidu.ueditor.define.BaseState;
import com.baidu.ueditor.define.State;
import com.baidu.ueditor.hunter.FileManager;
import com.baidu.ueditor.upload.StorageManager;
import com.kischang.simple_utils.utils.BytesUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * UEditor 图片上传功能父类
 *
 * @author KisChang
 * @version 1.0
 */
public abstract class UEditorPicUploadAbstractBase {

    protected abstract String getUploadSave(String path);

    protected abstract long getUserId(HttpSession session);

    //获取文件上传父路径
    protected abstract String getPicPath();

    protected abstract boolean beforeMethod();

    //默认的的图片类型
    private static final Map<String,String> GOODS_PIC_TYPE = new HashMap<String,String>(){{
        put("image/jpeg", ".jpeg");
        put("image/jpg", ".jpg");
        put("image/png", ".png");
    }};

    protected Map<String,String> getPicTypeMap(){
        return GOODS_PIC_TYPE;
    }

    protected String getUEditorController(){
        return "/public/ueditor/controller";
    }

    @RequestMapping("/ueditor/controller")
    public String ueditorController() throws IOException {
        return getUEditorController();
    }

    /**
     * UEditor控制器
     * 自定义处理二进制图片上传
     * */
    @RequestMapping("/ueditor/imageBin")
    public @ResponseBody
    String ueditorImageBin(MultipartFile upfile, HttpSession session) throws IOException {
        long userId = getUserId(session);
        if (!beforeMethod()){
            return new BaseState(false, AppInfo.INVALID_ACTION).toJSONString();
        }
        if (upfile == null){
            return new BaseState(false, AppInfo.NOTFOUND_UPLOAD_DATA).toJSONString();
        }
        if (!getPicTypeMap().containsKey(upfile.getContentType())){
            return new BaseState(false, AppInfo.NOT_ALLOW_FILE_TYPE).toJSONString();
        }
        String fileName = System.currentTimeMillis() + getPicTypeMap().get(upfile.getContentType());
        String accessPath = getPicPath()
                + userId + File.separator
                + fileName ;
        String savePath = getUploadSave(accessPath);
        OutputStream os = null;
        try {
            File saveFile = new File(savePath);
            if(!saveFile.getParentFile().exists()){
                saveFile.getParentFile().mkdirs();
            }
            os = new FileOutputStream(saveFile);
            IOUtils.copy(upfile.getInputStream(), os);
        } catch (IOException e) {
            e.printStackTrace();
            return new BaseState(false, AppInfo.IO_ERROR).toJSONString();
        } finally {
            IOUtils.closeQuietly(os);
        }
        State state = new BaseState(true);
        state.putInfo("url", accessPath);
        state.putInfo("title", fileName);
        state.putInfo("original", fileName);
        return state.toJSONString();
    }

    /**
     * UEditor控制器
     * 自定义处理Base图片上传
     * */
    @RequestMapping("/ueditor/imageBase")
    public @ResponseBody
    String ueditorImageBase(String upfile, HttpSession session) throws IOException {
        long userId = getUserId(session);
        if (!beforeMethod()){
            return new BaseState(false, AppInfo.INVALID_ACTION).toJSONString();
        }
        byte[] data = BytesUtils.base642bytes(upfile);
        long maxSize = 2048000;
        if(!  ((long)data.length <= maxSize)) {
            return new BaseState(false, 1).toJSONString();
        } else {
            String fileName = System.currentTimeMillis() + ".jpg";
            String accessPath = getPicPath()
                    + userId + File.separator
                    + fileName ;
            String savePath = getUploadSave(accessPath);

            State storageState = new StorageManager().saveBinaryFile(data, savePath, "");
            if(storageState.isSuccess()) {
                storageState.putInfo("url", accessPath);
                storageState.putInfo("type", "JPG");
                storageState.putInfo("original", "");
            }
            return storageState.toJSONString();
        }
    }

    /**
     * UEditor控制器
     * 图片列表
     * */
    @RequestMapping("/ueditor/imageList")
    public @ResponseBody
    String ueditorImageList(int start, int size, HttpSession session) throws IOException {
        long userId = getUserId(session);
        if (!beforeMethod()){
            return new BaseState(false, AppInfo.INVALID_ACTION).toJSONString();
        }
        File picPath = new File( getUploadSave(getPicPath() + userId + File.separator) );
        if (!picPath.exists()){
            return new BaseState(false).toJSONString();
        }
        Map<String,Object> map = new HashMap<>();
        map.put("rootPath", getUploadSave(File.separator));
        map.put("dir", (getPicPath() + userId).substring(1));
        map.put("allowFiles", getPicTypeMap().values().toArray(new String[1]));
        map.put("count", size);
        return new FileManager(map).listFile(start).toJSONString();
    }


}
