/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.main.dao.ibatis;

import java.util.List;

import javax.inject.Named;

import uk.me.phillsacre.lyricdisplay.main.dao.SettingsDAO;
import uk.me.phillsacre.lyricdisplay.main.entities.Setting;

/**
 * 
 * @author Phill
 * @since 30 Oct 2010
 */
@Named("settingsDAO")
public class IbatisSettingsDAO extends GenericIbatisDAO implements SettingsDAO
{

    @Override
    public Setting getSetting(String name)
    {
	return (Setting) getSqlMapClientTemplate().queryForObject(
	        "Setting.selectByName", name);
    }

    @Override
    public List<Setting> getSettings()
    {
	return getSqlMapClientTemplate().queryForList("Setting.selectAll");
    }

    @Override
    public void saveSetting(Setting setting)
    {
	if (null == setting.getId())
	{
	    Integer id = (Integer) getSqlMapClientTemplate().insert(
		    "Setting.insert", setting);
	    setting.setId(id);
	}
	else
	{
	    getSqlMapClientTemplate().update("Setting.update", setting);
	}
    }

}
