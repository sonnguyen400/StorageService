package com.sonnguyen.storageservice.utils;

import com.sonnguyen.storageservice.viewmodel.ThumbnailParamsVm;
import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUtils {
    public static Thumbnails.Builder<File> resize(File image, ThumbnailParamsVm thumbnailParamsVm) throws IOException {
        BufferedImage bufferedImage= ImageIO.read(image);
        int originalHeight=bufferedImage.getHeight();
        int originalWidth=bufferedImage.getWidth();
        int width=thumbnailParamsVm.width()==null?originalWidth/2:thumbnailParamsVm.width();
        int height=thumbnailParamsVm.height()==null?originalHeight/2:thumbnailParamsVm.height();
        if(thumbnailParamsVm.ratio()!=null){
            width=originalWidth;
            height= (int) Math.floor(originalWidth/thumbnailParamsVm.ratio());
        }
        return Thumbnails.of(image).size(width,height).keepAspectRatio(false);
    }

}
