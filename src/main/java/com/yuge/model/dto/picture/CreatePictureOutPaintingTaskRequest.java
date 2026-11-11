package com.yuge.model.dto.picture;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建扩图任务请求（前端传参）
 * <p>
 * 注意：这里的 Parameters 是「前端视角」的参数，与调用阿里云 API 的
 * {@link com.yuge.api.aliyunai.model.CreateOutPaintingTaskRequest.Parameters}
 * 解耦。服务层会把 xScale / yScale 映射为阿里云新接口的
 * left_scale / right_scale / top_scale / bottom_scale。
 */
@Data
public class CreatePictureOutPaintingTaskRequest implements Serializable {

    /**
     * 图片 id
     */
    private Long pictureId;

    /**
     * 扩图参数
     */
    private Parameters parameters;

    @Data
    public static class Parameters implements Serializable {

        /**
         * 水平方向扩展比例（映射为 left_scale / right_scale），默认 1.5
         */
        private Float xScale;

        /**
         * 垂直方向扩展比例（映射为 top_scale / bottom_scale），默认 1.5
         */
        private Float yScale;

        /**
         * 可选，提示词。不传则使用默认提示词
         */
        private String prompt;

        private static final long serialVersionUID = 1L;
    }

    private static final long serialVersionUID = 1L;
}
