package cafe.jjdev.gallery.service;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository//service에서 Autowired할껀데 미리객체가 만들어져있어야하니까???
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
	//내가 변경한것 
	public List<BoardFile> selectBoardFileByFK(int boardArticleNo){
		return sqlSession.selectList(NS+".selectBoardFileByFK",boardArticleNo);
	}
	public int insertBoardFile(BoardFile boardFile){
		//mapper내의 쿼리를 db로 쏴주는게 sqlSession이고 sqlSessionFactory랑 연관이..있고
		//sqlSessionFactory가 cafe/jjdev/gallery/service/ *.xml을 mapper로 인식해...서..
		return sqlSession.insert(NS+".insertBoardFile",boardFile);
	}

}
