package cafe.jjdev.gallery.web;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@RequestMapping(value ="/multipart", method = RequestMethod.POST )//������ ������� list�� �迭�� ������
	public String multipart(MultipartFile pic,HttpServletRequest request){//request.getContext�ؼ� context�ҷ�������
		if(!pic.isEmpty()){
			//���Ϻм� 
			logger.info("Original�����̸��� {} �̴� ",pic.getOriginalFilename());
			logger.info("���� �����̸��� {} �̴� ",pic.getName());//�����̸�
			logger.info("���� ������� {} �̴�.",pic.getSize());
			logger.info("������ ����Ʈ Ÿ���� {} �̴�", pic.getContentType());
			//����������ġ
			//������丮
			String saveDir ="D:\\LeeYeaEun\\jjdevBoard\\gallery\\src\\main\\upload";
			logger.info("resources������ ������ġ : {}",saveDir);
			/*			
			request.getServletContext().getRealPath("upload") �޼����� ��� ��Ŭ�������� ����ÿ��� ������οʹ� �ٸ� ��ΰ� ���´�. 
			���� ������ ���� �� ����ÿ��� ���������� ��ΰ� ���´�. 
			��Ŭ���� �׽�Ʈ �Ҷ��� �޼��带 ������� ���� ���Ͱ��� ��ũ�����̽� ������Ʈ�� ���� ���� ��θ� �ڵ忡�� �������.
			 */
			 
			//���������̸�
			//������ ���ڿ��� �����Ͽ� �ߺ������ʴ� �����̸��� �����
			UUID uuid= UUID.randomUUID();//
			String saveFileName = uuid.toString().replace("-","");//replace~~~
			logger.info("������ �����̸� : {}",saveFileName);
			
			//����Ȯ���� 
			//Original�����̸��� �ڿ������� .�� ��ġ���� ã���� +1�Ͽ� Ȯ���ڸ� ���Ѵ� 
			String ext = pic.getOriginalFilename().substring(pic.getOriginalFilename().lastIndexOf(".")+1);
			
			//��������
			String fullFileName = saveDir +"\\"+saveFileName+"."+ext;
			logger.info("������ full�̸� : {}",fullFileName);
			
			File saveFile = new File(fullFileName); 
			try {
			//pic�� saveFile�� �̵�
				pic.transferTo(saveFile);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return "redirect:/";
	}
	@RequestMapping(value ="/multipart", method = RequestMethod.GET )
	public String multipart(){
		return "multipart";
	}

	@RequestMapping(value = "/111", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "home";
	}
	
}
