package cafe.jjdev.gallery.web;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import cafe.jjdev.gallery.service.BoardRequest;
import cafe.jjdev.gallery.service.BoardService;

@Controller
public class BoardController {
	private static final Logger logger = LoggerFactory.getLogger(BoardController.class);
	@Autowired private BoardService boardService;
	final String imgDir = "D:\\LeeYeaEun\\jjdevBoard\\yeaeungallery\\src\\main\\webapp\\upload";
	
	/*@RequestMapping(value="/")
	public String home(){
		return "index";
	}*/
	
	/***************      ����ó��      ***************/
	@RequestMapping(value="/boardUpdate" ,method=RequestMethod.POST)
	public String boardUpdate(BoardRequest boardRequest, int boardArticleNo){
		logger.info(" �̿��� boardRequest :{}",boardRequest);
		boardService.updateBoard(boardRequest,boardArticleNo,imgDir);
		return "redirect:/boardList";
		//������ boardDetail?boardArticleNo="+boardArticleNo �� �̵��ϰ� �;��µ� 
		//�׷��� �ϸ� ���οø��̹����� ���� �ε��� �ȵǰ� �����̶�.. �Ф� 
				
	}
	
	
	/***************      ����ȭ��      ***************/
	@RequestMapping(value="/boardUpdate")
	public String boardUpdate(Model model, int boardArticleNo){
		Map<String,Object> map = boardService.getBoardDetail(boardArticleNo);
		//view���� boardArticle�� boardFile�� �и���Ŵ
		model.addAttribute("map",map);
		/*model.addAttribute("boardArticle",map.get("boardArticle"));
		model.addAttribute("boardFile",map.get("boardFile"));*/
		return "boardUpdate";
	}
	
	/***************      ����      ***************/
	@RequestMapping(value="/boardDelete")
	public String boardDelete(int boardArticleNo){
		logger.info("boardArticleNo>>>>>>>>>>>{}",boardArticleNo);
		boardService.deleteBoardArticleAndFile(boardArticleNo);
		return "redirect:/boardList";
	}
	
	
	/***************      �󼼺���      ***************/
	@RequestMapping(value="/boardDetail", method=RequestMethod.GET)
	public String boardDetail(Model model, int boardArticleNo){
		Map<String,Object> map = boardService.getBoardDetail(boardArticleNo);
		//view���� boardArticle�� boardFile�� �и���Ŵ
		model.addAttribute("map",map);
		/*model.addAttribute("boardArticle",map.get("boardArticle"));
		model.addAttribute("boardFile",map.get("boardFile"));*/
		return "boardDetail";
	}
	
	/***************      ����Ʈ      ***************/
	@RequestMapping(value="/")
	public String boardList(Model model ,
			@RequestParam(value = "page", defaultValue = "1") int page, 
			// word��  �ȳѾ�͵� �ȴ���
			@RequestParam(value = "word", required = false) String word){
		logger.info("word>>>>>>>>>>>>{}",word);
		model.addAttribute("boardArticleList",boardService.getBoardArticleList(page,word)); 
		
		int nextPage=((int)((double)page/10+0.9)+10);
		int preView=((int)((double)page/10+0.9)*10-19);
		
		int[] countPage = boardService.returnPage();
		logger.info("countPage[0]>>>>>>>>>>>{}",countPage[0]);
		logger.info("boardService.getLastPage()>>>>>>>>>>>{}",boardService.getLastPage());
		logger.info("page>>>>>>>>>>>{}",page);
		
		model.addAttribute("preView", preView);
		model.addAttribute("nextPage", nextPage);
		model.addAttribute("maxpage", countPage[0]);
		model.addAttribute("startpage", countPage[1]);
		model.addAttribute("endpage", countPage[2]);
		model.addAttribute("page", page);
		model.addAttribute("lastPage", boardService.getLastPage());
		
		return "boardList";
	}
	
	/***************      �Է�ȭ��      ***************/
	//�Է� �� ����
	@RequestMapping(value="/boardAdd", method=RequestMethod.GET)
	public String boardAdd(){
		logger.info("boardAddform����");
		return "boardAdd";
	}
	
	/***************      �Է�ó��      ***************/
	//�Է� ��ó�� ���� 
	@RequestMapping(value="/boardAdd", method=RequestMethod.POST)
	public String boardAdd(Model model,BoardRequest boardRequest){//ȭ�鿡�� �Է¹����� ���� ��ü��������
		logger.info("�Ű����� Ȯ�� : {} ",boardRequest.toString());
		//������ �ְ� ������Ÿ���� ��ġ���������� boardAdd ������ ����
		//�̷��� �� ������ �޼���� ���� ȣ���ؾ� �Ǵ°��̿���
		//�̷� ��ȿ���˻簰�� �޼��带 ���� �δ� ��Ű��&Ŭ������ ��������
		//�ڵ� �и��� ���ϰ��� �ʿ����� ���θ� �����ؾ���
		List<MultipartFile> boardImgs = boardRequest.getBoardImg();
		if(boardImgs!=null){
			for(MultipartFile mf : boardImgs ){
				if(!boardRequest.getBoardImg().isEmpty()){
					if(!mf.getContentType().equals("image/jpeg")
					&&!mf.getContentType().equals("image/png")	
					&&!mf.getContentType().equals("image/gif")	){
					logger.info("������Ÿ�� ����ġ�� boardAdd�� forward");
					model.addAttribute("errorMsg","������Ÿ�� ����ġ�� boardAdd�� forward");
					return "boardAdd";
					}
				}
			}
		}
		//������ ������ �� *��������
		boardService.addBoard(boardRequest,imgDir);
		return "redirect:/boardList";
	}
}
