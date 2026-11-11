package com.yuge;

import com.yuge.api.imagesearch1.model.BaiduItem;
import com.yuge.api.imagesearch1.model.BaiduResponse;
import com.yuge.api.imagesearch1.sub.BaiduImageSearch;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * 百度以图搜图 API 测试类
 */
@SpringBootTest
@Slf4j
class BaiduImageSearchTest {

    /**
     * 测试用的公网图片 URL
     * 建议换成一张真实可访问的图片
     */
    private static final String TEST_IMAGE_URL =
            "https://bxzd1170-1499956584.cos.ap-shanghai.myqcloud.com/public/2109593078964285441/2026-10-12_xiyZrWxdHmVNMvdx.webp";

    /**
     * 本地测试图片路径（可选）
     */
    private static final String LOCAL_IMAGE_PATH = "src/test/resources/test.jpg";

    /**
     * 测试方式一：通过图片 URL 搜索
     */
    @Test
    void testSearchByUrl() {
        log.info("开始测试：通过 URL 搜索图片");
        log.info("测试图片 URL: {}", TEST_IMAGE_URL);

        BaiduResponse response = BaiduImageSearch.search(TEST_IMAGE_URL);

        Assertions.assertNotNull(response, "响应结果不应为 null");
        Assertions.assertNotNull(response.getUrl(), "结果页 URL 不应为 null");

        log.info("结果页 URL: {}", response.getUrl());
        printResults(response);

        // 至少应该有结果页 URL
        Assertions.assertFalse(response.getUrl().isEmpty(), "结果页 URL 不应为空");
    }

    /**
     * 测试方式二：通过图片字节数组搜索（模拟文件上传）
     */
    @Test
    void testSearchByBytes() throws IOException {
        log.info("开始测试：通过字节数组搜索图片");

        // 从 URL 下载图片字节
        byte[] imageBytes = downloadImage(TEST_IMAGE_URL);
        Assertions.assertNotNull(imageBytes, "下载的图片字节不应为 null");
        Assertions.assertTrue(imageBytes.length > 0, "图片字节长度应大于 0");

        log.info("图片字节大小: {} bytes", imageBytes.length);

        BaiduResponse response = BaiduImageSearch.search(imageBytes);

        Assertions.assertNotNull(response, "响应结果不应为 null");
        log.info("结果页 URL: {}", response.getUrl());
        printResults(response);
    }

    /**
     * 测试方式三：从本地文件读取图片并搜索
     * 需要提前在 src/test/resources/ 下放置 test.jpg
     */
    @Test
    void testSearchByLocalFile() throws IOException {
        log.info("开始测试：通过本地文件搜索图片");

        java.io.File file = new java.io.File(LOCAL_IMAGE_PATH);
        if (!file.exists()) {
            log.warn("本地测试图片不存在，跳过该测试: {}", LOCAL_IMAGE_PATH);
            return;
        }

        byte[] imageBytes = Files.readAllBytes(Paths.get(LOCAL_IMAGE_PATH));
        log.info("本地图片大小: {} bytes", imageBytes.length);

        BaiduResponse response = BaiduImageSearch.search(imageBytes);

        Assertions.assertNotNull(response, "响应结果不应为 null");
        log.info("结果页 URL: {}", response.getUrl());
        printResults(response);
    }

    /**
     * 测试：无效 URL 应抛出异常
     */
    @Test
    void testInvalidUrl() {
        log.info("开始测试：无效 URL 应抛出异常");

        String invalidUrl = "https://invalid.example.com/not-exist.jpg";

        Assertions.assertThrows(RuntimeException.class, () -> {
            BaiduImageSearch.search(invalidUrl);
        }, "无效 URL 应抛出 RuntimeException");

        log.info("正确抛出异常");
    }

    /**
     * 打印搜索结果
     */
    private void printResults(BaiduResponse response) {
        List<BaiduItem> exactMatches = response.getExactMatches();
        List<BaiduItem> similarMatches = response.getRaw();

        log.info("========== 搜索结果 ==========");
        log.info("精确匹配（原图/来源）数量: {}", exactMatches == null ? 0 : exactMatches.size());
        if (exactMatches != null) {
            for (int i = 0; i < exactMatches.size(); i++) {
                log.info("  [精确 {}] {}", i + 1, exactMatches.get(i));
            }
        }

        log.info("相似图片数量: {}", similarMatches == null ? 0 : similarMatches.size());
        if (similarMatches != null) {
            int limit = Math.min(similarMatches.size(), 5);
            for (int i = 0; i < limit; i++) {
                log.info("  [相似 {}] {}", i + 1, similarMatches.get(i));
            }
            if (similarMatches.size() > limit) {
                log.info("  ... 还有 {} 条相似结果未显示", similarMatches.size() - limit);
            }
        }

        log.info("结果页 URL: {}", response.getUrl());
        log.info("==============================");
    }

    /**
     * 下载图片字节的辅助方法
     */
    private byte[] downloadImage(String imageUrl) throws IOException {
        java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .uri(java.net.URI.create(imageUrl))
                .GET()
                .build();
        try {
            java.net.http.HttpResponse<byte[]> response =
                    client.send(request, java.net.http.HttpResponse.BodyHandlers.ofByteArray());
            if (response.statusCode() == 200) {
                return response.body();
            }
            throw new IOException("下载图片失败，状态码: " + response.statusCode());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("下载图片被中断", e);
        }
    }
}