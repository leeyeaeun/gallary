package cafe.jjdev.gallery.service;

public class BoardArticle {
	private int boardArticleNo;
	private String boardArticleTitle;
	private String boardArticleContent;
	
	public int getBoardArticleNo() {
		return boardArticleNo;
	}
	public void setBoardArticleNo(int boardArticleNo) {
		this.boardArticleNo = boardArticleNo;
	}
	public String getBoardArticleTitle() {
		return boardArticleTitle;
	}
	public void setBoardArticleTitle(String boardArticleTitle) {
		this.boardArticleTitle = boardArticleTitle;
	}
	public String getBoardArticleContent() {
		return boardArticleContent;
	}
	public void setBoardArticleContent(String boardArticleContent) {
		this.boardArticleContent = boardArticleContent;
	}
	@Override
	public String toString() {
		return "BoardArticle [boardArticleNo=" + boardArticleNo + ", boardArticleTitle=" + boardArticleTitle
				+ ", boardArticleContent=" + boardArticleContent + "]";
	}
	
	
}
