package com.huawei.vi.login.service.impl;

import com.huawei.utils.NumConstant;
import com.huawei.vi.login.service.DrawImageService;
import com.jovision.jaws.common.config.redis.RedisOperatingService;
import com.jovision.jaws.common.constant.TokenConstant;
import com.jovision.jaws.common.util.CsrfTokenManager;
import com.jovision.jaws.common.util.RestResult;
import com.jovision.jaws.common.util.ServiceCommonConst;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class DrawImageServiceImpl implements DrawImageService {

    private static final Logger LOG = LoggerFactory.getLogger(DrawImageServiceImpl.class);

    private static final String DRAW_IMAGE_ID = "DrawImageId";

    /**
     * 生成的图片的宽度
     */
    private static final int WIDTH = 120;

    /**
     * 生成的图片的高度
     */
    private static final int HEIGHT = 30;

    private final byte[] seeds = SecureRandom.getSeed(NumConstant.NUM_20);

    @Autowired
    private RedisOperatingService redisOperatingService;

    @Override
    public RestResult getImage(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String drawImageId = request.getParameter(DRAW_IMAGE_ID);
        // 1.在内存中创建一张图片
        BufferedImage bufferedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        // 2.得到图片
        Graphics graphic = bufferedImage.getGraphics();
        // 3.设置图片的背影色
        setBackGround(graphic);
        // 4.设置图片的边框
        setBorder(graphic);
        // 5.在图片上画干扰线
        drawRandomLine(graphic);
        // 6.写在图片上随机数
        String random = drawRandomNum((Graphics2D) graphic);
        // 7.将随机数存在session中
        Base64 encoder = new Base64();
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ImageIO.write(bufferedImage, "png", out);
            out.flush();
            byte[] byteArray = out.toByteArray();
            String code = encoder.encodeToString(byteArray).trim();
            if (drawImageId == null) {
                drawImageId = CsrfTokenManager.getRandom();
            }
            resultMap.put("DrawImage", code);
            resultMap.put(DRAW_IMAGE_ID, drawImageId);
            resultMap.put("random", random);
            redisOperatingService.setTokenByTime(drawImageId,random, TokenConstant.DRAW_IMAGE_EXPIRE,TimeUnit.SECONDS);
        } catch (IOException e) {
            LOG.error("getImage error:{}", e.getMessage());
        }
        return RestResult.generateRestResult(ServiceCommonConst.CODE_SUCCESS, null, resultMap);
    }

    /**
     * 设置图片的背景色
     *
     * @param graphics 需要被设置的图片
     */
    private void setBackGround(Graphics graphics) {
        SecureRandom random = new SecureRandom(seeds);

        // 设置颜色
        graphics.setColor(Color.BLUE);

        // 填充区域
        graphics.fillRect(0, 0, WIDTH, HEIGHT);
        random.nextBytes(seeds);
    }

    /**
     * 设置图片的边框
     *
     * @param graphics 需要被设置的图片
     */
    private void setBorder(Graphics graphics) {
        // 设置边框颜色
        graphics.setColor(Color.BLUE);

        // 边框区域
        graphics.drawRect(1, 1, WIDTH - NumConstant.NUM_2, HEIGHT - NumConstant.NUM_2);
    }

    /**
     * 在图片上画随机线条
     *
     * @param graphics 需要被设置的图片
     */
    private void drawRandomLine(Graphics graphics) {
        SecureRandom random = new SecureRandom(seeds);

        // 设置颜色
        graphics.setColor(Color.getHSBColor(random.nextInt(NumConstant.NUM_255), random.nextInt(NumConstant.NUM_255),
                random.nextInt(NumConstant.NUM_255)));

        // 设置线条个数并画线
        for (int line = 0; line < random.nextInt(NumConstant.NUM_10); line++) {
            graphics.drawLine(random.nextInt(WIDTH), random.nextInt(HEIGHT), random.nextInt(WIDTH),
                    random.nextInt(HEIGHT));
        }
        random.nextBytes(seeds);
    }

    /**
     * 画随机字符
     *
     * @param graphics 可生成的字符
     * @return String... createTypeFlag是可变参数， Java1.5增加了新特性：可变参数：适用于参数个数不确定，类型确定的情况，java把可变参数当做数组处理。注意：可变参数必须位于最后一项
     */
    private String drawRandomNum(Graphics2D graphics) {
        SecureRandom random = new SecureRandom(seeds);

        // 设置颜色
        graphics.setColor(Color.getHSBColor(random.nextInt(NumConstant.NUM_255), random.nextInt(NumConstant.NUM_255),
                random.nextInt(NumConstant.NUM_255)));
        random.nextBytes(seeds);

        // 设置字体
        graphics.setFont(new Font("宋体", Font.BOLD, NumConstant.NUM_20));

        // 数字和字母的组合
        String baseNumLetter = "0123456789";

        // 默认截取数字和字母的组合
        return createRandomChar(graphics, baseNumLetter);
    }

    /**
     * 创建随机字符
     *
     * @param graphics 可生成的字符
     * @param baseChar 可生成的数字
     * @return 随机字符
     */
    private String createRandomChar(Graphics2D graphics, String baseChar) {
        SecureRandom random = new SecureRandom(seeds);
        StringBuilder stringBuilder = new StringBuilder(NumConstant.EXPECTED_BUFFER_DATA);
        int angle = NumConstant.NUM_5;
        String ch = "";

        // 控制字数
        for (int rotate = 0; rotate < NumConstant.NUM_4; rotate++) {
            // 设置字体旋转角度
            int degree = random.nextInt() % NumConstant.NUM_30;
            ch = Character.toString(baseChar.charAt(random.nextInt(baseChar.length())));
            stringBuilder.append(ch);

            // 正向角度
            graphics.rotate(degree * Math.PI / NumConstant.NUM_180, angle, NumConstant.NUM_20);
            graphics.drawString(ch, angle, NumConstant.NUM_20);

            // 反向角度
            graphics.rotate(-degree * Math.PI / NumConstant.NUM_180, angle, NumConstant.NUM_20);
            angle += NumConstant.NUM_30;
        }
        random.nextBytes(seeds);
        return stringBuilder.toString();
    }
}
