package cafe.jjdev.gallery.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class BoardRequest {
	private String boardTitle;
	private String boardContent;
	private List<MultipartFile> boardImg;
	
	
	public String getBoardTitle() {
		return boardTitle;
	}


	public void setBoardTitle(String boardTitle) {
		this.boardTitle = boardTitle;
	}


	public String getBoardContent() {
		return boardContent;
	}


	public void setBoardContent(String boardContent) {
		this.boardContent = boardContent;
	}


	public List<MultipartFile> getBoardImg() {
		return boardImg;
	}


	public void setBoardImg(List<MultipartFile> boardImg) {
		this.boardImg = boardImg;
	}


	@Override
	public String toString() {
		return "BoardRequest [boardTitle=" + boardTitle + ", boardContent=" + boardContent + ", boardImg=" + boardImg
				+ "]";
	}


}
