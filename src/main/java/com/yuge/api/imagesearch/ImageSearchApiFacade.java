package com.yuge.api.imagesearch;

import com.yuge.api.imagesearch.model.ImageSearchResult;
import com.yuge.api.imagesearch.sub.GetImageFirstUrlApi;
import com.yuge.api.imagesearch.sub.GetImageListApi;
import com.yuge.api.imagesearch.sub.GetImagePageUrlApi;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ImageSearchApiFacade {

    /**
     * 搜索图片
     * @param imageUrl
     * @return
     */
    public static List<ImageSearchResult> searchImage(String imageUrl) {
        String imagePageUrl = GetImagePageUrlApi.getImagePageUrl(imageUrl);
        String imageFirstUrl = GetImageFirstUrlApi.getImageFirstUrl(imagePageUrl);
        List<ImageSearchResult> imageList = GetImageListApi.getImageList(imageFirstUrl);
        return imageList;
    }

    public static void main(String[] args) {
        List<ImageSearchResult> imageList = searchImage("https://bxzd1170-1499956584.cos.ap-shanghai.myqcloud.com/public/2109593078964285441/2026-10-12_xiyZrWxdHmVNMvdx.webp");
        System.out.println("结果列表" + imageList);
    }
}
