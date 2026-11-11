package com.yuge.api.aliyunai.model;

import cn.hutool.core.annotation.Alias;
import lombok.Data;

import java.io.Serializable;

/**
 * 创建扩图任务请求
 * <p>
 * 对接阿里云百炼「图像编辑-万相2.1」（wanx2.1-imageedit）的 expand（扩图）功能。
 * 官方文档：https://help.aliyun.com/zh/model-studio/wanx-image-edit
 */
@Data
public class CreateOutPaintingTaskRequest implements Serializable {

    /**
     * 模型名称
     * <p>
     * 注意：旧模型 image-out-painting 已下线，现在统一使用 wanx2.1-imageedit
     */
    private String model = "wanx2.1-imageedit";

    /**
     * 输入图像信息
     */
    private Input input;

    /**
     * 图像处理参数
     */
    private Parameters parameters;

    @Data
    public static class Input implements Serializable {

        /**
         * 必选，图像编辑功能，扩图固定为 expand
         */
        private String function = "expand";

        /**
         * 必选，提示词，描述扩图后期望看到的完整场景内容
         */
        private String prompt;

        /**
         * 必选，输入图像的 URL，必须公网可访问
         */
        @Alias("base_image_url")
        private String baseImageUrl;
    }

    @Data
    public static class Parameters implements Serializable {

        /**
         * 可选，图像上方扩展比例，默认 1.0，范围 [1.0, 3.0]
         */
        @Alias("top_scale")
        private Float topScale;

        /**
         * 可选，图像下方扩展比例，默认 1.0，范围 [1.0, 3.0]
         */
        @Alias("bottom_scale")
        private Float bottomScale;

        /**
         * 可选，图像左侧扩展比例，默认 1.0，范围 [1.0, 3.0]
         */
        @Alias("left_scale")
        private Float leftScale;

        /**
         * 可选，图像右侧扩展比例，默认 1.0，范围 [1.0, 3.0]
         */
        @Alias("right_scale")
        private Float rightScale;

        /**
         * 可选，生成图片数量，默认 1，取值范围 [1, 4]
         */
        private Integer n;
    }
}
