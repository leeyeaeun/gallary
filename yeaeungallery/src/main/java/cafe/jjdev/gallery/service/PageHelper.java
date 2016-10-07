package cafe.jjdev.gallery.service;

public class PageHelper {
	private int startRow;
	private int linePerPage;

	public PageHelper(int page, int linePerPage) {
		this.linePerPage = linePerPage;
		this.startRow = (page - 1) * linePerPage; 
		// startRow´Â linePerPage X(page-1)													
	}
	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public int getLinePerPage() {
		return linePerPage;
	}

	public void setLinePerPage(int linePerPage) {
		this.linePerPage = linePerPage;
	}
}
