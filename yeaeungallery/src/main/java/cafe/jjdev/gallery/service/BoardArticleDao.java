package cafe.jjdev.gallery.service;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository //미리객체를 만들어두려고! 
public class BoardArticleDao {
	private static final Logger logger = LoggerFactory.getLogger(BoardArticleDao.class);
	@Autowired
	private SqlSessionTemplate sqlSession;//query를 db에 쏴야됨
	final String NS ="cafe.jjdev.gallery.service.BoardArticleMapper";
	//mapper의 NameSpace : 변경시에 용이
	public int updateBoardArticle(BoardArticle boardArticle){
		return sqlSession.update(NS+".updateBoardArticle",boardArticle);
	}
	
	
	public int deleteBoardArticle(int boardArticleNo){
		logger.info("boardArticleNo>>>>>>>>>>>{}",boardArticleNo);
		logger.info("sqlSession>>>>>>>>>>>{}",sqlSession.toString());
		return sqlSession.delete(NS+".deleteBoardArticle",boardArticleNo);
	}
	
	public int selectTotalCount(){
		 return sqlSession.selectOne(
	                NS+".selectTotalCount");
	}
	
	public BoardArticle selectBoardArticleContentByKey(int boardArticleNo){
		return sqlSession.selectOne(NS+".selectBoardArticleContentByKey",boardArticleNo);
	}
	public List<BoardArticle> selectBoardArticleList(Map<String, Object> map){
		
		return sqlSession.selectList(NS+".selectBoardArticleList", map);
	}
	
	//BoardArticle객체를 sqlSession에 주면 객체내부 필드값을 get해서 쿼리실행하는거임
	//리턴값은 ....흠.
	public int insertBoardArticle(BoardArticle boardArticle){
		logger.info(boardArticle.toString());
		logger.info(sqlSession.toString());
		return sqlSession.insert(NS+".insertBoardArticle",boardArticle);
	
	}

}
