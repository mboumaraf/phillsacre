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
 * @since 29 Nov 2011
 */
@Named("settingsDAO")
public class IbatisSettingsDAO extends GenericIbatisDAO implements SettingsDAO
{

    public Setting getSetting(String name)
    {
	return (Setting) getSqlMapClientTemplate().queryForObject(
	        "Setting.selectByName", name);
    }

    @SuppressWarnings("unchecked")
    public List<Setting> getSettings()
    {
	return getSqlMapClientTemplate().queryForList("Setting.selectAll");
    }

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

    public void saveSetting(String name, String value)
    {
	Setting setting = getSetting(name);

	if (null == setting)
	{
	    setting = new Setting();
	    setting.setName(name);
	}

	setting.setValue(value);

	saveSetting(setting);
    }
}
