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
	
	@RequestMapping(value ="/multipart", method = RequestMethod.POST )//복수를 받을경우 list나 배열로 받음됨
	public String multipart(MultipartFile pic,HttpServletRequest request){//request.getContext해서 context불러오려고
		if(!pic.isEmpty()){
			//파일분석 
			logger.info("Original파일이름은 {} 이다 ",pic.getOriginalFilename());
			logger.info("현재 파일이름은 {} 이다 ",pic.getName());//지금이름
			logger.info("파일 사이즈는 {} 이다.",pic.getSize());
			logger.info("파일의 컨텐트 타입은 {} 이다", pic.getContentType());
			//파일저장위치
			//저장디렉토리
			String saveDir ="D:\\LeeYeaEun\\jjdevBoard\\gallery\\src\\main\\upload";
			logger.info("resources폴더의 실제위치 : {}",saveDir);
			/*			
			request.getServletContext().getRealPath("upload") 메서드의 경우 이클립스에서 실행시에는 실제경로와는 다른 경로가 나온다. 
			서비스 서버에 배포 후 실행시에는 정상적으로 경로가 나온다. 
			이클립스 테스트 할때는 메서드를 사용하지 말고 위와같이 워크스페이스 프로젝트의 실제 절대 경로를 코드에서 사용하자.
			 */
			 
			//파일저장이름
			//랜덤한 문자열을 생성하여 중복되지않는 파일이름을 만든다
			UUID uuid= UUID.randomUUID();//
			String saveFileName = uuid.toString().replace("-","");//replace~~~
			logger.info("생성된 파일이름 : {}",saveFileName);
			
			//저장확장자 
			//Original파일이름의 뒤에서부터 .의 위치값을 찾은후 +1하여 확장자만 구한다 
			String ext = pic.getOriginalFilename().substring(pic.getOriginalFilename().lastIndexOf(".")+1);
			
			//파일저장
			String fullFileName = saveDir +"\\"+saveFileName+"."+ext;
			logger.info("생성된 full이름 : {}",fullFileName);
			
			File saveFile = new File(fullFileName); 
			try {
			//pic가 saveFile로 이동
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
