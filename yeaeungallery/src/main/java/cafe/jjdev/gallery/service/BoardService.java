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
// Ʈ�������� �̿��� BoardArticleDao�� �����ϸ� ���������ϴ°͵� �ѹ�
// @Transactional�Ʒ��� �ִ°��� �ϳ��� ���ܰ� �߻��ϸ� ��� �ѹ��ϴ� �ֳ����̼�
// @Service �ֳ����̼� �Ʒ����� ����Ҽ��ִ�.
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
		// ���������� �����ϴ� �ڵ�  update�ÿ� ����ϴ°�
 		
		List<BoardFile> boardFiles = boardFileDao.selectBoardFileByFK(boardArticleNo);
		if(boardFiles != null){	
			for(BoardFile bf:boardFiles){
		    String updateFileName = imgDir + "//" + bf.getBoardFileName() + "." + bf.getBoardFileExt();
		    File file = new File(updateFileName);
		    
			    if(file.delete()){//directory���� ���ϻ����� �����ϸ� db���������� �����
		    		boardFileDao.deleteBoardFileByFK(boardArticleNo);
			    }
		    }
		}
		
		
		List<MultipartFile> boardImgs = boardRequest.getBoardImg();
		// �ټ��� �̹����� �Է��ϱ����� �̹����� ����Ʈ�� �޾� 
		if(boardImgs!=null){
			for (MultipartFile mf : boardImgs) {
	
				// MultipartFile img = boardRequest.getBoardImg(); 
				// ���ϸ� ,Ȯ����, ������, Ÿ��
				UUID uuid = UUID.randomUUID();
				String saveFileName = uuid.toString().replace("-", "");
				String ext = mf.getOriginalFilename().substring(mf.getOriginalFilename().lastIndexOf(".") + 1);
				ext = ext.toLowerCase();
				long size = mf.getSize();
				String type = mf.getContentType();
				logger.info("������ �����̸� : {}", saveFileName);
				
				BoardFile boardFile = new BoardFile();
				
				boardFile.setBoardArticleNo(boardArticleNo);
				boardFile.setBoardFileName(saveFileName);
				boardFile.setBoardFileSize(size);
				boardFile.setBoardFileExt(ext);
				boardFile.setBoardFileType(type);
				logger.info("boardFile : {}", boardFile.toString());
	
				// file����
			
				String fullFileName = imgDir + "\\" + saveFileName + "." + ext;
				logger.info("fullFileName : {}", fullFileName);
				File saveFile = new File(fullFileName);
				try {
					// img�� saveFile�� �̵�
					mf.transferTo(saveFile);
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				// BoardFileDao ȣ��
				boardFileDao.insertBoardFile(boardFile);
	
					
			}
		}	
	}

	public void deleteBoardArticleAndFile(int boardArticleNo) {

		// ���� boardArticleNo�� �ش��ϴ� �Խù��� BoardFileDao�� ������?? �������ϴµ�
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
					System.out.println("���� �Ǵ� ���丮�� ���������� �������ϴ�: " + file);
				} else {
					System.err.println("���� �Ǵ� ���丮 ����� ����: " + file);
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
		// BoardRequest�� �ִ� ���� ������ boardArticle�� boardFile�� ������̴�
		BoardArticle boardArticle = new BoardArticle();
		boardArticle.setBoardArticleTitle(boardRequest.getBoardTitle());
		boardArticle.setBoardArticleContent(boardRequest.getBoardContent());
		logger.info("boardArticle : {}", boardArticle.toString());

		// BoardArticleDao ȣ��
		// boardArticle��ü�� ��� �ʵ尪���� db�� insert�ϴ°Ű�
		boardArticleDao.insertBoardArticle(boardArticle);
		logger.info("boardArticleDao.insertBoardArticle �޼��峻 : ", boardArticle);
		// insert�Ŀ� �ڵ������� ������ primary�� getBoardArticleNo()�� �����´�.
		int boardArticleNo = boardArticle.getBoardArticleNo();

		List<MultipartFile> boardImgs = boardRequest.getBoardImg();
		// �ټ��� �̹����� �Է��ϱ����� �̹����� ����Ʈ�� �޾� �ݺ��Ͽ� insert�Ѵ�
		if(boardImgs!=null){
			for (MultipartFile mf : boardImgs) {
	
				/* MultipartFile img = boardRequest.getBoardImg(); */
				// ���ϸ� ,Ȯ����, ������, Ÿ��
				UUID uuid = UUID.randomUUID();
				String saveFileName = uuid.toString().replace("-", "");
				String ext = mf.getOriginalFilename().substring(mf.getOriginalFilename().lastIndexOf(".") + 1);
				ext = ext.toLowerCase();
				long size = mf.getSize();
				String type = mf.getContentType();
				logger.info("������ �����̸� : {}", saveFileName);
	
				BoardFile boardFile = new BoardFile();
				// insertBoardArticle�޼�������� ������ boardArticleNo�� ����
				boardFile.setBoardArticleNo(boardArticleNo);
				boardFile.setBoardFileName(saveFileName);
				boardFile.setBoardFileSize(size);
				boardFile.setBoardFileExt(ext);
				boardFile.setBoardFileType(type);
				logger.info("boardFile : {}", boardFile.toString());
	
				// file����
				if (!boardRequest.getBoardImg().isEmpty()) {
					String fullFileName = saveDir + "\\" + saveFileName + "." + ext;
					logger.info("fullFileName : {}", fullFileName);
					File saveFile = new File(fullFileName);
					try {
						// img�� saveFile�� �̵�
						mf.transferTo(saveFile);
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					// BoardFileDao ȣ��
					boardFileDao.insertBoardFile(boardFile);
	
				}
			}
	
		}
	}

}
