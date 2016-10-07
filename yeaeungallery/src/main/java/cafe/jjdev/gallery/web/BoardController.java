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
	
	/***************      수정처리      ***************/
	@RequestMapping(value="/boardUpdate" ,method=RequestMethod.POST)
	public String boardUpdate(BoardRequest boardRequest, int boardArticleNo){
		logger.info(" 이예은 boardRequest :{}",boardRequest);
		boardService.updateBoard(boardRequest,boardArticleNo,imgDir);
		return "redirect:/boardList";
		//디테일 boardDetail?boardArticleNo="+boardArticleNo 로 이동하고 싶었는데 
		//그렇게 하면 새로올린이미지가 빨리 로딩이 안되고 엑박이뜸.. ㅠㅠ 
				
	}
	
	
	/***************      수정화면      ***************/
	@RequestMapping(value="/boardUpdate")
	public String boardUpdate(Model model, int boardArticleNo){
		Map<String,Object> map = boardService.getBoardDetail(boardArticleNo);
		//view에서 boardArticle과 boardFile을 분리시킴
		model.addAttribute("map",map);
		/*model.addAttribute("boardArticle",map.get("boardArticle"));
		model.addAttribute("boardFile",map.get("boardFile"));*/
		return "boardUpdate";
	}
	
	/***************      삭제      ***************/
	@RequestMapping(value="/boardDelete")
	public String boardDelete(int boardArticleNo){
		logger.info("boardArticleNo>>>>>>>>>>>{}",boardArticleNo);
		boardService.deleteBoardArticleAndFile(boardArticleNo);
		return "redirect:/boardList";
	}
	
	
	/***************      상세보기      ***************/
	@RequestMapping(value="/boardDetail", method=RequestMethod.GET)
	public String boardDetail(Model model, int boardArticleNo){
		Map<String,Object> map = boardService.getBoardDetail(boardArticleNo);
		//view에서 boardArticle과 boardFile을 분리시킴
		model.addAttribute("map",map);
		/*model.addAttribute("boardArticle",map.get("boardArticle"));
		model.addAttribute("boardFile",map.get("boardFile"));*/
		return "boardDetail";
	}
	
	/***************      리스트      ***************/
	@RequestMapping(value="/")
	public String boardList(Model model ,
			@RequestParam(value = "page", defaultValue = "1") int page, 
			// word가  안넘어와도 된다함
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
	
	/***************      입력화면      ***************/
	//입력 폼 맵핑
	@RequestMapping(value="/boardAdd", method=RequestMethod.GET)
	public String boardAdd(){
		logger.info("boardAddform실행");
		return "boardAdd";
	}
	
	/***************      입력처리      ***************/
	//입력 폼처리 맵핑 
	@RequestMapping(value="/boardAdd", method=RequestMethod.POST)
	public String boardAdd(Model model,BoardRequest boardRequest){//화면에서 입력받은값 가진 객체받으려고
		logger.info("매개변수 확인 : {} ",boardRequest.toString());
		//파일이 있고 콘텐츠타입이 일치하지않으면 boardAdd 폼으로 리턴
		//이렇게 긴 로직은 메서드로 만들어서 호출해야 되는것이에요
		//이런 유효성검사같은 메서드를 따로 두는 패키지&클래스를 만들어야함
		//코드 분리시 리턴값이 필요한지 여부를 생각해야함
		List<MultipartFile> boardImgs = boardRequest.getBoardImg();
		if(boardImgs!=null){
			for(MultipartFile mf : boardImgs ){
				if(!boardRequest.getBoardImg().isEmpty()){
					if(!mf.getContentType().equals("image/jpeg")
					&&!mf.getContentType().equals("image/png")	
					&&!mf.getContentType().equals("image/gif")	){
					logger.info("콘텐츠타입 불일치로 boardAdd로 forward");
					model.addAttribute("errorMsg","콘텐츠타입 불일치로 boardAdd로 forward");
					return "boardAdd";
					}
				}
			}
		}
		//파일을 저장할 곳 *역슬러시
		boardService.addBoard(boardRequest,imgDir);
		return "redirect:/boardList";
	}
}
