package cafe.jjdev.gallery.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
// 트랜젝션을 이용해 BoardArticleDao가 실패하면 파일저장하는것도 롤백
// @Transactional아래에 있는것중 하나라도 예외가 발생하면 모두 롤백하는 애노테이션
// @Service 애노테이션 아래에만 사용할수있다.
public class BoardService {
	private static final Logger logger = LoggerFactory.getLogger(BoardService.class);
	private final int LINE_PER_PAGE = 10;// limit
	@Autowired
	private BoardArticleDao boardArticleDao;
	@Autowired
	private BoardFileDao boardFileDao;
	final String imgDir = "D:\\LeeYeaEun\\jjdevBoard\\yeaeungallery\\src\\main\\webapp\\upload";
	
	public void updateBoard(BoardRequest boardRequest, int boardArticleNo,String imgDir){
		BoardArticle boardArticle = new BoardArticle();
		boardArticle.setBoardArticleNo(boardArticleNo);
		boardArticle.setBoardArticleTitle(boardRequest.getBoardTitle());
		boardArticle.setBoardArticleContent(boardRequest.getBoardContent());
		logger.info("boardArticle : {}", boardArticle.toString());
		int updateResult=boardArticleDao.updateBoardArticle(boardArticle);
		
		//BoardFileUpdate
		// 기존파일을 삭제하는 코드  update시에 사용하는거
 		
		List<BoardFile> boardFiles = boardFileDao.selectBoardFileByFK(boardArticleNo);
		if(boardFiles != null){	
			for(BoardFile bf:boardFiles){
		    String updateFileName = imgDir + "//" + bf.getBoardFileName() + "." + bf.getBoardFileExt();
		    File file = new File(updateFileName);
		    
			    if(file.delete()){//directory안의 파일삭제에 성공하면 db파일정보도 지운다
		    		boardFileDao.deleteBoardFileByFK(boardArticleNo);
			    }
		    }
		}
		
		
		List<MultipartFile> boardImgs = boardRequest.getBoardImg();
		// 다수의 이미지를 입력하기위해 이미지를 리스트로 받아 
		if(boardImgs!=null){
			for (MultipartFile mf : boardImgs) {
	
				// MultipartFile img = boardRequest.getBoardImg(); 
				// 파일명 ,확장자, 사이즈, 타입
				UUID uuid = UUID.randomUUID();
				String saveFileName = uuid.toString().replace("-", "");
				String ext = mf.getOriginalFilename().substring(mf.getOriginalFilename().lastIndexOf(".") + 1);
				ext = ext.toLowerCase();
				long size = mf.getSize();
				String type = mf.getContentType();
				logger.info("생성된 파일이름 : {}", saveFileName);
				
				BoardFile boardFile = new BoardFile();
				
				boardFile.setBoardArticleNo(boardArticleNo);
				boardFile.setBoardFileName(saveFileName);
				boardFile.setBoardFileSize(size);
				boardFile.setBoardFileExt(ext);
				boardFile.setBoardFileType(type);
				logger.info("boardFile : {}", boardFile.toString());
	
				// file저장
			
				String fullFileName = imgDir + "\\" + saveFileName + "." + ext;
				logger.info("fullFileName : {}", fullFileName);
				File saveFile = new File(fullFileName);
				try {
					// img가 saveFile로 이동
					mf.transferTo(saveFile);
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				// BoardFileDao 호출
				boardFileDao.insertBoardFile(boardFile);
	
					
			}
		}	
	}

	public void deleteBoardArticleAndFile(int boardArticleNo) {

		// 만약 boardArticleNo에 해당하는 게시물이 BoardFileDao에 있으면?? 지워야하는데
		List<BoardFile> boardFiles = boardFileDao.selectBoardFileByFK(boardArticleNo);
		for (BoardFile bf : boardFiles) {
			if (boardFiles != null) {
				logger.info("boardFile.getBoardFileName>>>>>>>>>>>>{}", bf.getBoardFileName());
				logger.info("getBoardFileExt>>>>>>>>>>>>{}", bf.getBoardFileExt());
				String name = bf.getBoardFileName();
				String ext = bf.getBoardFileExt();
				String filePath = imgDir+"\\" + name + "." + ext;

				logger.info(">>>>>>>>>{}", "/upload/" + name + "." + ext);
				File file = new File(filePath);
				if (file.delete()) {
					System.out.println("파일 또는 디렉토리를 성공적으로 지웠습니다: " + file);
				} else {
					System.err.println("파일 또는 디렉토리 지우기 실패: " + file);
				}
			}
		}

		boardFileDao.deleteBoardFileByFK(boardArticleNo);

		boardArticleDao.deleteBoardArticle(boardArticleNo);

	}

	public int getLastPage() {
		return (int) (Math.ceil((double) boardArticleDao.selectTotalCount() / LINE_PER_PAGE));
	}

	int[] countPage;

	public List<BoardArticle> getBoardArticleList(int page, String word) {
		PageHelper pageHelper = new PageHelper(page, LINE_PER_PAGE);
		Map<String, Object> map = new HashMap<String, Object>();
		logger.info("word>>>>>>>>>>>>{}", word);
		map.put("pageHelper", pageHelper);
		map.put("word", word);

		int listcount = boardArticleDao.selectTotalCount();
		logger.info("listcount>>>>>>>>>>>>{}", listcount);
		int maxpage = (int) ((double) listcount / LINE_PER_PAGE + 0.95);
		int startpage = (((int) ((double) page / 10 + 0.9)) - 1) * 10 + 1;
		int endpage = startpage + 10 - 1;
		if (endpage > maxpage)
			endpage = maxpage;
		countPage = new int[] { maxpage, startpage, endpage };
		logger.info("maxpage>>>>>>>>>>>>{}", maxpage);
		logger.info("startpage>>>>>>>>>>>>{}", startpage);
		logger.info("endpage>>>>>>>>>>>>{}", endpage);
		logger.info("countPage>>>>>>>>>>>>{}", countPage.toString());

		return boardArticleDao.selectBoardArticleList(map);
	}

	public int[] returnPage() {

		return countPage;
	}

	public Map<String, Object> getBoardDetail(int boardArticleNo) {

		BoardArticle boardArticle = boardArticleDao.selectBoardArticleContentByKey(boardArticleNo);
		List<BoardFile> boardFiles = boardFileDao.selectBoardFileByFK(boardArticleNo);
		Map<String, Object> map = new HashMap<String, Object>();
	
		map.put("boardArticle", boardArticle);
		map.put("boardFile", boardFiles);
		
		return map;
	}

	public void addBoard(BoardRequest boardRequest, String saveDir) {
		logger.info("boardArticleDao : {}", boardArticleDao);
		// BoardRequest에 있는 값을 나누어 boardArticle과 boardFile을 만들것이다
		BoardArticle boardArticle = new BoardArticle();
		boardArticle.setBoardArticleTitle(boardRequest.getBoardTitle());
		boardArticle.setBoardArticleContent(boardRequest.getBoardContent());
		logger.info("boardArticle : {}", boardArticle.toString());

		// BoardArticleDao 호출
		// boardArticle객체에 담긴 필드값으로 db에 insert하는거고
		boardArticleDao.insertBoardArticle(boardArticle);
		logger.info("boardArticleDao.insertBoardArticle 메서드내 : ", boardArticle);
		// insert후에 자동증가로 생성된 primary값 getBoardArticleNo()로 가져온다.
		int boardArticleNo = boardArticle.getBoardArticleNo();

		List<MultipartFile> boardImgs = boardRequest.getBoardImg();
		// 다수의 이미지를 입력하기위해 이미지를 리스트로 받아 반복하여 insert한다
		if(boardImgs!=null){
			for (MultipartFile mf : boardImgs) {
	
				/* MultipartFile img = boardRequest.getBoardImg(); */
				// 파일명 ,확장자, 사이즈, 타입
				UUID uuid = UUID.randomUUID();
				String saveFileName = uuid.toString().replace("-", "");
				String ext = mf.getOriginalFilename().substring(mf.getOriginalFilename().lastIndexOf(".") + 1);
				ext = ext.toLowerCase();
				long size = mf.getSize();
				String type = mf.getContentType();
				logger.info("생성된 파일이름 : {}", saveFileName);
	
				BoardFile boardFile = new BoardFile();
				// insertBoardArticle메서드실행후 생성된 boardArticleNo값 셋팅
				boardFile.setBoardArticleNo(boardArticleNo);
				boardFile.setBoardFileName(saveFileName);
				boardFile.setBoardFileSize(size);
				boardFile.setBoardFileExt(ext);
				boardFile.setBoardFileType(type);
				logger.info("boardFile : {}", boardFile.toString());
	
				// file저장
				if (!boardRequest.getBoardImg().isEmpty()) {
					String fullFileName = saveDir + "\\" + saveFileName + "." + ext;
					logger.info("fullFileName : {}", fullFileName);
					File saveFile = new File(fullFileName);
					try {
						// img가 saveFile로 이동
						mf.transferTo(saveFile);
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					// BoardFileDao 호출
					boardFileDao.insertBoardFile(boardFile);
	
				}
			}
	
		}
	}

}
