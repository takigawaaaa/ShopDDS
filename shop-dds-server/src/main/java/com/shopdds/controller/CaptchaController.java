package com.shopdds.controller;

import com.shopdds.common.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Map;
import java.util.Random;

/**
 * 验证码控制器
 * <p>
 * 生成简易图形验证码，以 base64 返回，并把答案存入 session 供登录校验。
 * （本版本登录未强制校验验证码，前端可选实现；后续可在 AuthService 中接入。）
 */
@RestController
@RequestMapping("/auth/captcha")
@RequiredArgsConstructor
public class CaptchaController {

    private static final String CHARS = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";
    private static final int WIDTH = 90;
    private static final int HEIGHT = 32;

    @GetMapping
    public Result<Map<String, String>> captcha(HttpServletRequest request) throws Exception {
        Random rnd = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            code.append(CHARS.charAt(rnd.nextInt(CHARS.length())));
        }
        String captcha = code.toString();

        HttpSession session = request.getSession(true);
        session.setAttribute("captcha", captcha.toLowerCase());

        BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 22));
        for (int i = 0; i < captcha.length(); i++) {
            g.setColor(new Color(rnd.nextInt(150), rnd.nextInt(150), rnd.nextInt(150)));
            g.drawString(String.valueOf(captcha.charAt(i)), 8 + i * 20, 24);
        }
        // 干扰线
        for (int i = 0; i < 6; i++) {
            g.setColor(new Color(rnd.nextInt(200), rnd.nextInt(200), rnd.nextInt(200)));
            g.drawLine(rnd.nextInt(WIDTH), rnd.nextInt(HEIGHT), rnd.nextInt(WIDTH), rnd.nextInt(HEIGHT));
        }
        g.dispose();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(img, "png", bos);
        String base64 = "data:image/png;base64," + Base64.getEncoder().encodeToString(bos.toByteArray());
        String sessionId = "C-" + Integer.toHexString(session.getId().hashCode());
        return Result.success(Map.of("image", base64, "sessionId", sessionId, "captcha", captcha));
    }
}