package swjtu.zkd.toutiao.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.util.Map;
import java.util.Properties;

@Service
public class MailSender implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailSender.class);

    private JavaMailSenderImpl mailSender;

    public boolean sendWithHtmlTemplate(String to, String subject, String template, Map<String, Object> model) {
        try {
            String nick = MimeUtility.encodeText("中级项目课");
            InternetAddress from = new InternetAddress("dasdas");
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            String text = "<html>hello</html>";
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(text, true);
            mailSender.send(mimeMessage);
            return true;
        } catch (Exception e) {
            LOGGER.error("发送邮件失败", e);
            return false;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        mailSender = new JavaMailSenderImpl();
        mailSender.setUsername("slfjs");
        mailSender.setPassword("lsfdfj");
        mailSender.setHost("smtp.exmail.qq.com");

        mailSender.setPort(456);
        mailSender.setProtocol("smtps");
        mailSender.setDefaultEncoding("utf-8");
        Properties properties = new Properties();
        properties.put("mail.smtp.ssl.enable", true);
        mailSender.setJavaMailProperties(properties);
    }
}
