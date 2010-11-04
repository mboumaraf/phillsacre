/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.main.dao.ibatis;

import javax.inject.Inject;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * 
 * @author Phill
 * @since 30 Oct 2010
 */
public abstract class GenericIbatisDAO extends SqlMapClientDaoSupport
{
    @Inject
    public void setLyricSqlMapClient(SqlMapClient sqlMapClient)
    {
	super.setSqlMapClient(sqlMapClient);
    }
}
