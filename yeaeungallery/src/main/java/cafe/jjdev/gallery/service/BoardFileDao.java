package cafe.jjdev.gallery.service;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository//service���� Autowired�Ҳ��� �̸���ü�� ��������־���ϴϱ�???
public class BoardFileDao {
	@Autowired
	private SqlSessionTemplate sqlSession;
	final String NS="cafe.jjdev.gallery.service.BoardFileMapper";
	
	public int updateBoardFile(BoardFile boardFile){
		return sqlSession.update(NS+".updateBoardFile",boardFile);
	}
	
	public int deleteBoardFileByFK(int boardArticleNo){
		return sqlSession.delete(NS+".deleteBoardFileByFK",boardArticleNo);
	}
	//���� �����Ѱ� 
	public List<BoardFile> selectBoardFileByFK(int boardArticleNo){
		return sqlSession.selectList(NS+".selectBoardFileByFK",boardArticleNo);
	}
	public int insertBoardFile(BoardFile boardFile){
		//mapper���� ������ db�� ���ִ°� sqlSession�̰� sqlSessionFactory�� ������..�ְ�
		//sqlSessionFactory�� cafe/jjdev/gallery/service/ *.xml�� mapper�� �ν���...��..
		return sqlSession.insert(NS+".insertBoardFile",boardFile);
	}

}
