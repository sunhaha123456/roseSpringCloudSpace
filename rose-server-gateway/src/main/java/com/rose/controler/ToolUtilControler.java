package com.rose.controler;

import com.rose.data.constant.SystemConstant;
import com.rose.common.data.response.StringResponse;
import com.rose.common.repository.RedisRepositoryCustom;
import com.rose.common.util.IdUtil;
import com.rose.common.util.Md5Util;
import com.rose.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 功能：工具controller
 * @author sunpeng
 * @date 2019
 */
@Slf4j
@RestController
@RequestMapping("/tool")
public class ToolUtilControler {

    @Inject
    private RedisRepositoryCustom redisRepositoryCustom;

    /**
     * 功能：产生唯一标志
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/getOnlySign")
    public StringResponse getOnlySign() {
        return new StringResponse(IdUtil.getID());
    }

    @GetMapping(value = "/randImg/getLoginCode")
    public void getLoginCode(HttpServletResponse resp, @RequestParam String key) throws Exception {
        if (StringUtil.isEmpty(key) || key.trim().length() != 32) {
            return;
        }
        randImg(resp, key);
    }

    /**
     * 功能	：产生四位数随机整数的验证码
     * 		  并把验证码的值放入redis中设置生命周期后保存，供后续验证
     * 		  注意，放入的是经过md5后的密文，所以在取出表单提交的验证码，进行比对时，需要比对加密后的
     * 备注 ：
     * @param resp
     * @param codeKey 唯一标志
     */
    private void randImg(HttpServletResponse resp, String codeKey) throws Exception {
        //验证码图片的宽度。
        int width=80;
        //验证码图片的高度。
        int height=30;
        BufferedImage buffImg=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        Graphics2D g=buffImg.createGraphics();
        //创建一个随机数生成器类。
        Random random=new Random();
        g.setColor(Color.WHITE);
        g.fillRect(0,0,width,height);
        //创建字体，字体的大小应该根据图片的高度来定。
        Font font=new Font("Times New Roman",Font.PLAIN,22);
        //设置字体。
        g.setFont(font);
        //画边框。
        g.setColor(Color.BLACK);
        g.drawRect(0,0,width-1,height-1);
        //随机产生160条干扰线，使图象中的认证码不易被其它程序探测到。
        g.setColor(new Color(160,160,160));
        for (int i=0;i<320;i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x,y,x+xl,y+yl);
        }

        //randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
        StringBuffer randomCode=new StringBuffer();
        int red=0,green=0,blue=0;

        //随机产生4位数字的验证码。
        for (int i=0;i<4;i++) {
            //得到随机产生的验证码数字。
            String strRand=String.valueOf(random.nextInt(10));

            //产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同。
            red=random.nextInt(255);
            green=random.nextInt(10);
            blue=random.nextInt(10);

            //用随机产生的颜色将验证码绘制到图像中。
            g.setColor(new Color(red,green,blue));
            g.drawString(strRand,13*i+12,23);

            //将产生的四个随机数组合在一起。
            randomCode.append(strRand);
        }
        //将四位数字的验证码保存到redis中。
        redisRepositoryCustom.saveMinutes(SystemConstant.LOGIN_CODE_PREFIX + codeKey, Md5Util.MD5Encode(randomCode.toString()), SystemConstant.LOGIN_CODE_SAVE_TIME);

        //禁止图像缓存。
        resp.setHeader("Pragma","no-cache");
        resp.setHeader("Cache-Control","no-cache");
        resp.setDateHeader("Expires", 0);

        resp.setContentType("image/jpeg");
        //将图像输出到Servlet输出流中。
        ServletOutputStream sos = resp.getOutputStream();
        ImageIO.write(buffImg, "jpeg", sos);
        sos.close();
    }
}