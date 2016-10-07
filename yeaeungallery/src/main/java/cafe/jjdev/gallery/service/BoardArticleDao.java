package cafe.jjdev.gallery.service;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository //�̸���ü�� �����η���! 
public class BoardArticleDao {
	private static final Logger logger = LoggerFactory.getLogger(BoardArticleDao.class);
	@Autowired
	private SqlSessionTemplate sqlSession;//query�� db�� ���ߵ�
	final String NS ="cafe.jjdev.gallery.service.BoardArticleMapper";
	//mapper�� NameSpace : ����ÿ� ����
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
	
	//BoardArticle��ü�� sqlSession�� �ָ� ��ü���� �ʵ尪�� get�ؼ� ���������ϴ°���
	//���ϰ��� ....��.
	public int insertBoardArticle(BoardArticle boardArticle){
		logger.info(boardArticle.toString());
		logger.info(sqlSession.toString());
		return sqlSession.insert(NS+".insertBoardArticle",boardArticle);
	
	}

}
